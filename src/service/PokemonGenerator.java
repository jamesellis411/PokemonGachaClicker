package service;

import model.Pokemon;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class PokemonGenerator {

    // ✨ 1 in 1000 shiny odds
    private static final double SHINY_ODDS = 1.0 / 1000.0;

    private static final Random random = new Random();

    private static final Map<Integer, List<String>> generationPokemon = new HashMap<>();

    public static Pokemon generateRandomPokemon() {
        return generateRandomPokemon(1, 1.0);
    }

    public static Pokemon generateRandomPokemon(int generation) {
        return generateRandomPokemon(generation, 1.0);
    }

    public static Pokemon generateRandomPokemon(int generation, double shinyBoost) {
        List<String> names = generationPokemon.computeIfAbsent(
                generation,
                PokemonGenerator::loadPokemonNamesForGeneration
        );

        if (names.isEmpty()) {
            // Fallback to an always-available set so the app keeps working.
            names = generationPokemon.computeIfAbsent(0, ignored -> defaultPokemonPool());
        }

        String resourceName = names.get(random.nextInt(names.size()));
        String displayName = formatDisplayName(resourceName);
        String type = PokeApiClient.fetchTypeDisplay(resourceName);
        int level = random.nextInt(50) + 1; // random level 1–50

        double adjustedBoost = Math.max(1.0, shinyBoost);
        double effectiveOdds = Math.min(1.0, SHINY_ODDS * adjustedBoost);
        boolean shiny = random.nextDouble() < effectiveOdds;

        return new Pokemon(displayName, type, level, shiny, resourceName);
    }

    private static List<String> loadPokemonNamesForGeneration(int generation) {
        String resourceFolder = "/images/Gen " + generation;
        URL url = PokemonGenerator.class.getResource(resourceFolder);
        if (url == null) {
            System.out.println("⚠️ Missing sprite folder for " + resourceFolder);
            return Collections.emptyList();
        }

        try {
            if ("file".equals(url.getProtocol())) {
                return loadFromFileSystem(url);
            }
            if ("jar".equals(url.getProtocol())) {
                return loadFromJar(url, resourceFolder.substring(1));
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("⚠️ Failed loading Pokémon names for " + resourceFolder + ": " + e.getMessage());
        }

        return Collections.emptyList();
    }

    private static List<String> loadFromFileSystem(URL url) throws URISyntaxException, IOException {
        Path dir = Path.of(url.toURI());
        if (!Files.exists(dir)) {
            return Collections.emptyList();
        }

        try (var stream = Files.list(dir)) {
            return stream
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .filter(name -> name.endsWith(".png"))
                    .map(PokemonGenerator::extractResourceName)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    private static List<String> loadFromJar(URL url, String folder) throws IOException {
        JarURLConnection connection = (JarURLConnection) url.openConnection();
        try (JarFile jar = connection.getJarFile()) {
            Set<String> names = new HashSet<>();
            String prefix = folder.endsWith("/") ? folder : folder + "/";

            var entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (!entryName.startsWith(prefix) || entry.isDirectory()) {
                    continue;
                }
                if (entryName.endsWith(".png")) {
                    String fileName = entryName.substring(prefix.length());
                    if (fileName.contains("/")) {
                        continue; // Skip nested folders
                    }
                    names.add(extractResourceName(fileName));
                }
            }

            return names.stream().sorted().collect(Collectors.toList());
        }
    }

    private static String extractResourceName(String fileName) {
        String base = fileName;
        if (base.endsWith(".png")) {
            base = base.substring(0, base.length() - 4);
        }
        if (base.endsWith("_shiny")) {
            base = base.substring(0, base.length() - 6);
        }
        return base;
    }

    private static String formatDisplayName(String resourceName) {
        return Arrays.stream(resourceName.split("-"))
                .filter(part -> !part.isBlank())
                .map(PokemonGenerator::capitalizeWord)
                .collect(Collectors.joining(" "));
    }

    private static String capitalizeWord(String word) {
        if (word.isBlank()) {
            return word;
        }
        char first = word.charAt(0);
        return Character.toUpperCase(first) + word.substring(1);
    }

    private static List<String> defaultPokemonPool() {
        return List.of(
                "bulbasaur",
                "charmander",
                "squirtle",
                "pidgey",
                "eevee",
                "pikachu",
                "magikarp",
                "gastly",
                "onix",
                "abra"
        );
    }
}
