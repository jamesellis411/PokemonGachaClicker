package service;

import model.Pokemon;
import model.User;
import java.io.*;
import java.util.*;

public class SaveManager {

    private static final String SAVE_FILE = "save.txt";
    private static final String SAVE_VERSION = "1.1"; // ✨ current save format version

    // ✅ Save user data
    public static void saveUser(User user) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE))) {
            writer.println("VERSION:" + SAVE_VERSION);
            writer.println(user.getUsername());
            writer.println(user.getPasswordHash());
            writer.println(user.getBalance());
            writer.println(user.getClickPower());
            writer.println(user.hasShinyCharm());
            writer.println(user.getShinyBoost());

            for (Pokemon p : user.getInventory()) {
                writer.println(p.getName() + "," + p.getType() + "," + p.getLevel() + "," + p.isShiny());
            }

            System.out.println("💾 Saved progress for " + user.getUsername());
        } catch (IOException e) {
            System.out.println("⚠️ Error saving user: " + e.getMessage());
        }
    }

    // ✅ Load user data
    public static User loadUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            String firstLine = reader.readLine();
            String version = "1.0"; // default for older saves
            String savedUsername;
            String savedPassword;

            // 🔄 Detect version tag
            if (firstLine != null && firstLine.startsWith("VERSION:")) {
                version = firstLine.substring(8);
                savedUsername = reader.readLine();
            } else {
                // Old save (no version line)
                savedUsername = firstLine;
            }

            savedPassword = reader.readLine();

            if (!username.equals(savedUsername) || !password.equals(savedPassword)) {
                System.out.println("❌ Incorrect username or password.");
                return null;
            }

            int balance = Integer.parseInt(reader.readLine());
            int clickPower = 1;
            boolean hasShinyCharm = false;
            double shinyBoost = 1.0;

            // 🧠 Version-based parsing
            if (version.equals("1.1")) {
                clickPower = Integer.parseInt(reader.readLine());
                hasShinyCharm = Boolean.parseBoolean(reader.readLine());
                shinyBoost = Double.parseDouble(reader.readLine());
            }

            User user = new User(username, password);
            user.addCoins(balance);
            for (int i = 1; i < clickPower; i++) user.increaseClickPower();
            if (hasShinyCharm) user.buyShinyCharm();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    String type = parts[1];
                    int level = Integer.parseInt(parts[2]);
                    boolean shiny = Boolean.parseBoolean(parts[3]);
                    user.addPokemon(new Pokemon(name, type, level, shiny));
                }
            }

            System.out.println("✅ Loaded save for " + username + " (version " + version + ")");
            return user;

        } catch (IOException | NumberFormatException e) {
            System.out.println("⚠️ Save file malformed or missing — creating new user.");
            return null;
        }
    }
}
