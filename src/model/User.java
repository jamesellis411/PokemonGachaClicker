package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String passwordHash;
    private List<Pokemon> inventory;
    private int balance;

    private int clickPower = 1;
    private double shinyBoost = 1.0;
    private double coinMultiplier = 1.0;
    private boolean hasShinyCharm = false;
    private boolean hasTrainingGloves = false;
    private boolean hasLuckyIncense = false;
    private boolean hasSuperGloves = false;
    private boolean hasCoinMagnet = false;


    public User (String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.inventory = new ArrayList<>();
        this.balance = 0;
    }

    //Getters:
    public String getUsername() {
        return username;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public List<Pokemon> getInventory() {
        return inventory;
    }
    public int getBalance() {
        return balance;
    }
    public int getClickPower() {
        return clickPower;
    }
    public double getShinyBoost() {
        return shinyBoost;
    }
    public double getCoinMultiplier() {
        return coinMultiplier;
    }
    public int getCoinsPerClick() {
        int amount = (int) Math.round(clickPower * coinMultiplier);
        return Math.max(1, amount);
    }

    public void addPokemon(Pokemon pokemon) {
        inventory.add(pokemon);
    }

    public void addCoins(int amount) {
        balance += amount;
    }

    public void increaseClickPower() {
        clickPower++;
    }

    public void addClickPower(int amount) {
        clickPower += Math.max(0, amount);
    }

    public boolean hasShinyCharm() {
        return hasShinyCharm;
    }

    public void buyShinyCharm() {
        if (hasShinyCharm) {
            return;
        }
        hasShinyCharm = true;
        shinyBoost += 0.5;
    }

    public boolean hasTrainingGloves() {
        return hasTrainingGloves;
    }

    public void markTrainingGlovesOwned() {
        hasTrainingGloves = true;
    }

    public boolean hasLuckyIncense() {
        return hasLuckyIncense;
    }

    public void buyLuckyIncense() {
        if (hasLuckyIncense) {
            return;
        }
        hasLuckyIncense = true;
        shinyBoost += 0.5;
    }

    public boolean hasSuperGloves() {
        return hasSuperGloves;
    }

    public void buySuperGloves() {
        if (hasSuperGloves) {
            return;
        }
        hasSuperGloves = true;
        addClickPower(5);
    }

    public boolean hasCoinMagnet() {
        return hasCoinMagnet;
    }

    public void buyCoinMagnet() {
        if (hasCoinMagnet) {
            return;
        }
        hasCoinMagnet = true;
        coinMultiplier += 0.5;
    }

    public void setShinyBoost(double shinyBoost) {
        this.shinyBoost = shinyBoost;
    }

    public void setCoinMultiplier(double coinMultiplier) {
        this.coinMultiplier = coinMultiplier;
    }

    public void setHasShinyCharm(boolean hasShinyCharm) {
        this.hasShinyCharm = hasShinyCharm;
    }

    public void setHasTrainingGloves(boolean hasTrainingGloves) {
        this.hasTrainingGloves = hasTrainingGloves;
    }

    public void setHasLuckyIncense(boolean hasLuckyIncense) {
        this.hasLuckyIncense = hasLuckyIncense;
    }

    public void setHasSuperGloves(boolean hasSuperGloves) {
        this.hasSuperGloves = hasSuperGloves;
    }

    public void setHasCoinMagnet(boolean hasCoinMagnet) {
        this.hasCoinMagnet = hasCoinMagnet;
    }

    public boolean spendCoins(int amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void showInventory() {
        if (inventory.isEmpty()) {
            System.out.println(username + " has no Pokemon yet!");
            return;
        }
        System.out.println(username + "'s Pokemon:");
        for (int i=0; i < inventory.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.get(i));
        }
    }

}


