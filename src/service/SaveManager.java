package service;

import model.Pokemon;
import model.User;

import java.io.*;

public class SaveManager {

    // 💾 Save user data
    public static void saveUser(User user) {
        String fileName = user.getUsername() + "_save.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println(user.getUsername());
            writer.println(user.getPasswordHash());
            writer.println(user.getBalance());
            for (Pokemon p : user.getInventory()) {
                writer.printf("%s,%s,%d,%b%n", p.getName(), p.getType(), p.getLevel(), p.isShiny());
            }
            System.out.println("✅ Saved progress for " + user.getUsername() + "!");
        } catch (IOException e) {
            System.out.println("❌ Error saving game: " + e.getMessage());
        }
    }

    // 📂 Load user data (with password verification)
    public static User loadUser(String username, String inputPassword) {
        String fileName = username + "_save.txt";
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("🆕 No save found for '" + username + "'. Creating a new account...");
            return null; // Signal to create a new user
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String savedUsername = reader.readLine();
            String savedPasswordHash = reader.readLine();
            String balanceLine = reader.readLine();
            int balance = 0;
            try {
                balance = Integer.parseInt(balanceLine);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Save file is malformed — resetting balance to 0.");
            }

            User user = new User(savedUsername, savedPasswordHash);
            user.addCoins(balance);

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

            // Check password
            if (!savedPasswordHash.equals(inputPassword)) {
                System.out.println("❌ Incorrect password for user: " + username);
                return null;
            }

            System.out.println("✅ Login successful! Loaded save for " + username + ".");
            return user;

        } catch (IOException e) {
            System.out.println("❌ Error loading save for " + username + ": " + e.getMessage());
            return null;
        }
    }
}
