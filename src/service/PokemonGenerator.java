package service;

import model.Pokemon;
import java.util.Random;

public class PokemonGenerator {

    private static final String[] NAMES = {
            "Bulbasaur", "Charmander", "Squirtle", "Pidgey", "Eevee",
            "Pikachu", "Magikarp", "Gastly", "Onix", "Abra"
    };

    private static final String[] TYPES = {
            "Grass", "Fire", "Water", "Flying", "Normal",
            "Electric", "Water", "Ghost", "Rock", "Psychic"
    };

    // ✨ 1 in 1000 shiny odds
    private static final double SHINY_ODDS = 1.0 / 1000.0;

    private static final Random random = new Random();

    public static Pokemon generateRandomPokemon() {
        int index = random.nextInt(NAMES.length);
        String name = NAMES[index];
        String type = TYPES[index];
        int level = random.nextInt(50) + 1; // random level 1–50

        boolean shiny = random.nextDouble() < SHINY_ODDS;

        return new Pokemon(name, type, level, shiny);
    }
}
