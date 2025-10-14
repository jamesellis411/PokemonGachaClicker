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

        VBox upgradesBox = new VBox(10);
        upgradesBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                title,
                coinsLabel,
                upgradesBox
        );

        refreshUpgradeButtons(user, upgradesBox, coinsLabel, onUpgrade);

        Scene scene = new Scene(root, 420, 360);
        shopStage.setScene(scene);
        shopStage.show();
    }

    private static void refreshUpgradeButtons(User user,
                                              VBox container,
                                              Label coinsLabel,
                                              Runnable onUpgrade) {
        container.getChildren().clear();

        // Click Power upgrade chain
        if (!user.hasTrainingGloves()) {
            Button glovesButton = new Button("🧤 Buy Training Gloves (+1 Click Power) - 200 Coins");
            glovesButton.setStyle("-fx-font-size: 14px;");
            glovesButton.setOnAction(e -> {
                if (user.spendCoins(200)) {
                    user.increaseClickPower();
                    user.markTrainingGlovesOwned();
                    coinsLabel.setText("Your Coins: " + user.getBalance());
                    onUpgrade.run();
                    refreshUpgradeButtons(user, container, coinsLabel, onUpgrade);
                } else {
                    coinsLabel.setText("Not enough coins! You have: " + user.getBalance());
                }
            });
            container.getChildren().add(glovesButton);
        } else if (!user.hasSuperGloves()) {
            Button superGlovesButton = new Button("💪 Buy Super Gloves (+5 Click Power) - 1000 Coins");
            superGlovesButton.setStyle("-fx-font-size: 14px;");
            superGlovesButton.setOnAction(e -> {
                if (user.spendCoins(1000)) {
                    user.buySuperGloves();
                    coinsLabel.setText("Your Coins: " + user.getBalance());
                    onUpgrade.run();
                    refreshUpgradeButtons(user, container, coinsLabel, onUpgrade);
                } else {
                    coinsLabel.setText("Not enough coins! You have: " + user.getBalance());
                }
            });
            container.getChildren().add(superGlovesButton);
        }

        // Shiny odds upgrades (independent purchases)
        if (!user.hasShinyCharm()) {
            Button charmButton = new Button("✨ Buy Shiny Charm (Increase shiny odds) - 500 Coins");
            charmButton.setStyle("-fx-font-size: 14px;");
            charmButton.setOnAction(e -> {
                if (user.spendCoins(500)) {
                    user.buyShinyCharm();
                    coinsLabel.setText("Your Coins: " + user.getBalance());
                    onUpgrade.run();
                    refreshUpgradeButtons(user, container, coinsLabel, onUpgrade);
                } else {
                    coinsLabel.setText("Not enough coins! You have: " + user.getBalance());
                }
            });
            container.getChildren().add(charmButton);
        }

        if (!user.hasLuckyIncense()) {
            Button incenseButton = new Button("🌸 Buy Lucky Incense (Boost shiny odds) - 800 Coins");
            incenseButton.setStyle("-fx-font-size: 14px;");
            incenseButton.setOnAction(e -> {
                if (user.spendCoins(800)) {
                    user.buyLuckyIncense();
                    coinsLabel.setText("Your Coins: " + user.getBalance());
                    onUpgrade.run();
                    refreshUpgradeButtons(user, container, coinsLabel, onUpgrade);
                } else {
                    coinsLabel.setText("Not enough coins! You have: " + user.getBalance());
                }
            });
            container.getChildren().add(incenseButton);
        }

        // Coin multiplier upgrade
        if (!user.hasCoinMagnet()) {
            Button coinMagnetButton = new Button("🧲 Buy Coin Magnet (+50% coins per click) - 1200 Coins");
            coinMagnetButton.setStyle("-fx-font-size: 14px;");
            coinMagnetButton.setOnAction(e -> {
                if (user.spendCoins(1200)) {
                    user.buyCoinMagnet();
                    coinsLabel.setText("Your Coins: " + user.getBalance());
                    onUpgrade.run();
                    refreshUpgradeButtons(user, container, coinsLabel, onUpgrade);
                } else {
                    coinsLabel.setText("Not enough coins! You have: " + user.getBalance());
                }
            });
            container.getChildren().add(coinMagnetButton);
        }

        if (container.getChildren().isEmpty()) {
            Label allBoughtLabel = new Label("🎉 All upgrades purchased!");
            allBoughtLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            container.getChildren().add(allBoughtLabel);
        }
    }
}
