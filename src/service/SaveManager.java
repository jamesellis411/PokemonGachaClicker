package service;

import model.Pokemon;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    private static final String SAVE_FILE = "save.txt";

    // 💾 Save user data to file
    public static void saveUser(User user) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE))) {
            writer.println(user.getUsername());
            writer.println(user.getBalance());
            for (Pokemon p : user.getInventory()) {
                // Format: name,type,level,shiny
                writer.printf("%s,%s,%d,%b%n", p.getName(), p.getType(), p.getLevel(), p.isShiny());
            }
            System.out.println("✅ Game saved successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error saving game: " + e.getMessage());
        }
    }

    // 📂 Load user data from file
    public static User loadUser() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            System.out.println("No previous save found — starting a new game!");
            return new User("Ash", "hashedpassword123");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String username = reader.readLine();
            int balance = Integer.parseInt(reader.readLine());

            User user = new User(username, "hashedpassword123");
            user.addCoins(balance); // set starting balance

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

            System.out.println("✅ Save data loaded!");
            return user;
        } catch (IOException e) {
            System.out.println("❌ Error loading save: " + e.getMessage());
            return new User("Ash", "hashedpassword123");
        }
    }
}
