package ui;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Pokemon;
import model.User;
import service.PokemonGenerator;
import service.SaveManager;
import service.SoundManager;

public class ClickerApp extends Application {

    private User user;
    private Label balanceLabel;
    private TextArea inventoryArea;
    private ImageView pokemonImage;
    private Button spinButton;
    private int selectedGeneration = 1;

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

        Label title = new Label("🎰 PokéGacha");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        balanceLabel = new Label("Coins: " + user.getBalance());
        balanceLabel.setStyle("-fx-font-size: 18px;");

        // --- Buttons ---
        Button clickButton = new Button("💰 Click for Coins");
        clickButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20 10 20;");
        clickButton.setOnAction(e -> handleClick());

        spinButton = new Button();
        spinButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20 10 20;");
        updateSpinButtonLabel();
        spinButton.setOnAction(e -> handleBuy());

        Button pickCapsuleButton = new Button("🎯 Pick Capsule");
        pickCapsuleButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20 10 20;");
        pickCapsuleButton.setOnAction(e -> openCapsulePicker(stage));

        Button shopButton = new Button("🏪 Open Shop");
        shopButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20 10 20;");
        shopButton.setOnAction(e -> ShopWindow.show(user, () -> balanceLabel.setText("Coins: " + user.getBalance())));

        // --- Pokémon Sprite Display ---
        pokemonImage = new ImageView();
        pokemonImage.setFitHeight(150);
        pokemonImage.setPreserveRatio(true);
        VBox.setMargin(pokemonImage, new Insets(10, 0, 10, 0));

        // --- Inventory Area ---
        inventoryArea = new TextArea();
        inventoryArea.setEditable(false);
        inventoryArea.setPrefHeight(350);
        inventoryArea.setPrefWidth(350);
        VBox.setMargin(inventoryArea, new Insets(10, 0, 0, 0));
        updateInventory();

        Button saveButton = new Button("💾 Save & Exit");
        saveButton.setStyle("-fx-font-size: 14px;");
        saveButton.setOnAction(e -> {
            SaveManager.saveUser(user);
            stage.close();
        });

        // --- Add everything to layout ---
        root.getChildren().addAll(
                title,
                balanceLabel,
                clickButton,
                pickCapsuleButton,
                spinButton,
                shopButton,
                pokemonImage,
                inventoryArea,
                saveButton
        );

        // 🪟 Make window taller to fit bigger inventory
        Scene scene = new Scene(root, 400, 650);
        stage.setTitle("PokéGacha");
        stage.setScene(scene);
        stage.show();
    }

    private void handleClick() {
        SoundManager.playClick();
        int coinsEarned = user.getCoinsPerClick();
        user.addCoins(coinsEarned);
        balanceLabel.setText("Coins: " + user.getBalance());
    }

    private void updateSpinButtonLabel() {
        spinButton.setText("🎁 Spin Gen " + selectedGeneration + " Capsule (100 Coins)");
    }

    private void handleBuy() {
        if (user.spendCoins(100)) {
            SoundManager.playCapsule();
            Pokemon newMon = PokemonGenerator.generateRandomPokemon(selectedGeneration, user.getShinyBoost());

            try {
                // Determine correct image file
                String baseFileName = newMon.getResourceName();
                String imageFile = newMon.isShiny()
                        ? baseFileName + "_shiny.png"
                        : baseFileName + ".png";

                String imagePath = "/images/Gen " + selectedGeneration + "/" + imageFile;
                System.out.println("🖼 Loading sprite: " + imagePath); // debug helper

                Image image = new Image(getClass().getResource(imagePath).toExternalForm());
                pokemonImage.setImage(image);

                // Fade-in animation when new Pokémon appears
                FadeTransition fadeIn = new FadeTransition(Duration.millis(600), pokemonImage);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();

            } catch (Exception e) {
                System.out.println("⚠️ No sprite found for " + newMon.getName());
                pokemonImage.setImage(null);
            }

            user.addPokemon(newMon);
            updateInventory();
            inventoryArea.positionCaret(inventoryArea.getText().length()); // auto-scroll to bottom

            showAlert("You got a new Pokémon!", newMon.toString());

            if (newMon.isShiny()) {
                SoundManager.playShiny();
                showAlert("✨ SHINY Pokémon!", "You found a shiny " + newMon.getName() + "!");
            }

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

    private void openCapsulePicker(Stage owner) {
        Stage pickerStage = new Stage();
        pickerStage.initOwner(owner);
        pickerStage.initModality(Modality.APPLICATION_MODAL);
        pickerStage.setTitle("Select Capsule Generation");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        for (int gen = 1; gen <= 9; gen++) {
            Button genButton = new Button("Gen " + gen);
            genButton.setMaxWidth(Double.MAX_VALUE);
            int selectedGen = gen;
            genButton.setOnAction(e -> {
                selectedGeneration = selectedGen;
                updateSpinButtonLabel();
                pickerStage.close();
            });
            layout.getChildren().add(genButton);
        }

        Scene pickerScene = new Scene(layout, 220, 400);
        pickerStage.setScene(pickerScene);
        pickerStage.showAndWait();
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
