package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class ShopWindow {

    public static void show(User user, Runnable onUpgrade) {
        Stage shopStage = new Stage();
        shopStage.setTitle("🏪 PokéShop");

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #fff5e6, #ffd480); -fx-padding: 20;");

        Label title = new Label("🏪 PokéShop Upgrades");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label coinsLabel = new Label("Your Coins: " + user.getBalance());
        coinsLabel.setStyle("-fx-font-size: 16px;");

        // --- Upgrade 1: Training Gloves ---
        Button glovesButton = new Button("🧤 Buy Training Gloves (+1 Click Power) - 200 Coins");
        glovesButton.setStyle("-fx-font-size: 14px;");
        glovesButton.setOnAction(e -> {
            if (user.getBalance() >= 200) {
                user.spendCoins(200);
                user.increaseClickPower();
                coinsLabel.setText("Your Coins: " + user.getBalance());
                onUpgrade.run(); // updates main balance label
                glovesButton.setDisable(true);
            } else {
                coinsLabel.setText("Not enough coins!");
            }
        });

        // --- Upgrade 2: Shiny Charm ---
        Button charmButton = new Button("✨ Buy Shiny Charm (Increase shiny odds) - 500 Coins");
        charmButton.setStyle("-fx-font-size: 14px;");
        charmButton.setOnAction(e -> {
            if (user.getBalance() >= 500 && !user.hasShinyCharm()) {
                user.spendCoins(500);
                user.buyShinyCharm();
                coinsLabel.setText("Your Coins: " + user.getBalance());
                onUpgrade.run();
                charmButton.setDisable(true);
            } else if (user.hasShinyCharm()) {
                coinsLabel.setText("You already own this!");
            } else {
                coinsLabel.setText("Not enough coins!");
            }
        });

        // Disable already purchased upgrades
        if (user.hasShinyCharm()) charmButton.setDisable(true);

        root.getChildren().addAll(title, coinsLabel, glovesButton, charmButton);

        Scene scene = new Scene(root, 400, 300);
        shopStage.setScene(scene);
        shopStage.show();
    }
}
