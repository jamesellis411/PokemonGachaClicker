package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Minimal helper for reading Pokemon typing data from https://pokeapi.co/.
 * Keeps a static in-memory cache so repeated spins avoid extra HTTP calls.
 */
public final class PokeApiClient {

    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final Map<String, String> TYPE_CACHE = new ConcurrentHashMap<>();
    private static final Pattern TYPE_PATTERN = Pattern.compile("\\\"type\\\"\\s*:\\s*\\{\\s*\\\"name\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");

    private PokeApiClient() {
        // Utility class
    }

    public static String fetchTypeDisplay(String resourceName) {
        return TYPE_CACHE.computeIfAbsent(resourceName, PokeApiClient::loadTypesFromApi);
    }

    private static String loadTypesFromApi(String resourceName) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BASE_URL + sanitizeName(resourceName));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
            connection.setReadTimeout((int) Duration.ofSeconds(5).toMillis());
            connection.setRequestProperty("Accept", "application/json");

            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                System.out.println("⚠️ PokeAPI call failed for " + resourceName + " (status " + status + ")");
                return "Unknown";
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder body = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
                return parseTypes(body.toString());
            }

        } catch (IOException e) {
            System.out.println("⚠️ Unable to reach PokeAPI for " + resourceName + ": " + e.getMessage());
            return "Unknown";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String parseTypes(String json) {
        Matcher matcher = TYPE_PATTERN.matcher(json);
        List<String> types = new ArrayList<>();
        while (matcher.find()) {
            String rawType = matcher.group(1);
            if (!rawType.isBlank()) {
                types.add(capitalize(rawType));
            }
        }
        if (types.isEmpty()) {
            return "Unknown";
        }
        return String.join("/", types);
    }

    private static String sanitizeName(String resourceName) {
        return resourceName.toLowerCase();
    }

    private static String capitalize(String text) {
        if (text.isBlank()) {
            return text;
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
