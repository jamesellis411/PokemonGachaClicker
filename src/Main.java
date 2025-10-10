import model.User;
import model.Pokemon;
import service.PokemonGenerator;
import service.SaveManager;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // 📂 Load saved data if available
        User ash = SaveManager.loadUser();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome back, " + ash.getUsername() + "!");
        printCommands();

        boolean running = true;
        while (running) {
            System.out.print("\nCommand: ");
            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "c" -> handleClick(ash);
                case "buy" -> handleBuy(ash);
                case "show" -> handleShow(ash);
                case "help" -> printCommands();
                case "exit" -> {
                    SaveManager.saveUser(ash);
                    System.out.println("👋 Goodbye, Trainer!");
                    running = false;
                }
                default -> System.out.println("❓ Unknown command. Try 'c', 'buy', 'show', or 'exit'.");
            }
        }

        scanner.close();
    }

    private static void handleClick(User user) {
        user.addCoins(1);
        System.out.println("💰 +1 coin! Current balance: " + user.getBalance());
    }

    private static void handleBuy(User user) {
        if (user.spendCoins(100)) {
            Pokemon newMon = PokemonGenerator.generateRandomPokemon();
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
              c     → Click for +1 coin
              buy   → Spend 100 coins for a Pokémon
              show  → View your Pokémon collection
              help  → Show this list again
              exit  → Save & quit the game
            """);
    }
}

