import model.User;
import model.Pokemon;
import service.PokemonGenerator;
import service.SaveManager;
import utils.PasswordHasher;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("🎮 Welcome to Pokémon Clicker!");
        System.out.println("Each username has one account. Use the same password to log in again.");

        System.out.print("Enter your username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine().trim();
        String passwordHash = PasswordHasher.hash(password);

        // Try to load existing user
        User currentUser = SaveManager.loadUser(username, passwordHash);

        if (currentUser == null) {
            System.out.println("Creating a new account for " + username + "...");
            currentUser = new User(username, passwordHash);
        }

        System.out.println("Welcome, " + currentUser.getUsername() + "!");
        printCommands();

        boolean running = true;
        while (running) {
            System.out.print("\nCommand: ");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "c" -> handleClick(currentUser);
                case "buy" -> handleBuy(currentUser);
                case "show" -> handleShow(currentUser);
                case "help" -> printCommands();
                case "exit" -> {
                    SaveManager.saveUser(currentUser);
                    System.out.println("👋 Goodbye, " + currentUser.getUsername() + "!");
                    running = false;
                }
                default -> System.out.println("❓ Unknown command. Try 'c', 'buy', 'show', or 'exit'.");
            }
        }

        scanner.close();
    }

    private static void handleClick(User user) {
        int coinsEarned = user.getCoinsPerClick();
        user.addCoins(coinsEarned);
        String suffix = coinsEarned == 1 ? " coin" : " coins";
        System.out.println("💰 +" + coinsEarned + suffix + "! Current balance: " + user.getBalance());
    }

    private static void handleBuy(User user) {
        if (user.spendCoins(100)) {
            Pokemon newMon = PokemonGenerator.generateRandomPokemon(1, user.getShinyBoost());
            user.addPokemon(newMon);
            System.out.println("🎉 You obtained a " + newMon);
        } else {
            System.out.println("❌ Not enough coins! You need 100.");
        }
    }

    private static void handleShow(User user) {
        System.out.println("\n==============================");
        user.showInventory();
        System.out.println("==============================");
        System.out.println("💰 Balance: " + user.getBalance());
    }

    private static void printCommands() {
        System.out.println("""
            Commands:
              c     → Click for coins (scales with upgrades)
              buy   → Spend 100 coins for a Pokémon
              show  → View your Pokémon collection
              help  → Show this list again
              exit  → Save & quit
            """);
    }
}
