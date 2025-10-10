package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String passwordHash;
    private List<Pokemon> inventory;
    private int balance;


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

    public void addPokemon(Pokemon pokemon) {
        inventory.add(pokemon);
    }

    public void addCoins(int amount) {
        balance += amount;
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



