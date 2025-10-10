package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import model.Pokemon;
import model.User;
import service.PokemonGenerator;
import service.SaveManager;

public class ClickerApp extends Application {

    private User user;
    private Label balanceLabel;
    private TextArea inventoryArea;

    @Override
    public void start(Stage stage) {
        // ✅ Try loading the save file — fallback to new user if null or bad data
        user = SaveManager.loadUser("Ash", "default");
        if (user == null) {
            System.out.println("Creating new default user profile...");
            user = new User("Ash", "default");
        }

        // --- Layout setup ---
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f8ff, #a2c2e0); -fx-padding: 20;");

        Label title = new Label("🐉 Pokémon Clicker");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        balanceLabel = new Label("Coins: " + user.getBalance());
        balanceLabel.setStyle("-fx-font-size: 18px;");

        Button clickButton = new Button("💰 Click for Coins");
        clickButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20 10 20;");
        clickButton.setOnAction(e -> handleClick());

        Button buyButton = new Button("🎁 Buy Pokémon (100 Coins)");
        buyButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20 10 20;");
        buyButton.setOnAction(e -> handleBuy());

        inventoryArea = new TextArea();
        inventoryArea.setEditable(false);
        inventoryArea.setPrefHeight(200);
        updateInventory();

        Button saveButton = new Button("💾 Save & Exit");
        saveButton.setStyle("-fx-font-size: 14px;");
        saveButton.setOnAction(e -> {
            SaveManager.saveUser(user);
            stage.close();
        });

        root.getChildren().addAll(title, balanceLabel, clickButton, buyButton, inventoryArea, saveButton);

        Scene scene = new Scene(root, 400, 500);
        stage.setTitle("Pokémon Clicker");
        stage.setScene(scene);
        stage.show();
    }

    private void handleClick() {
        user.addCoins(1);
        balanceLabel.setText("Coins: " + user.getBalance());
    }

    private void handleBuy() {
        if (user.spendCoins(100)) {
            Pokemon newMon = PokemonGenerator.generateRandomPokemon();
            user.addPokemon(newMon);
            updateInventory();
            showAlert("You got a new Pokémon!", newMon.toString());
        } else {
            showAlert("Not Enough Coins", "You need 100 coins to buy a Pokémon.");
        }
        balanceLabel.setText("Coins: " + user.getBalance());
    }

    private void updateInventory() {
        StringBuilder sb = new StringBuilder();
        for (Pokemon p : user.getInventory()) {
            sb.append(p).append("\n");
        }
        inventoryArea.setText(sb.isEmpty() ? "No Pokémon yet!" : sb.toString());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
