package service;

import model.Pokemon;
import java.util.Random;

public class PokemonGenerator {
    private static final String[] NAMES = {
            "Bulbasaur", "Charmander", "Squirtle", "Pidgey", "Eevee",
            "Pikachu", "Magikarp", "Gastly", "Onix", "Abra"
    };

    private static final String[] TYPES = {
            "Grass", "Fire", "Water", "Flying", "Electric", "Normal", "Ghost", "Rock", "Psychic"
    };

    private static final double SHINY_ODDS = 1.0 / 4096; //roughly 0.024414%

    private static final Random random = new Random();

    public static Pokemon generateRandomPokemon() {
        String name = NAMES[random.nextInt(NAMES.length)];
        String type = TYPES[random.nextInt(TYPES.length)];
        int level = random.nextInt(50) + 1; //random level 1-50
        boolean shiny = random.nextDouble() < SHINY_ODDS;

        return new Pokemon(name, type, level, shiny);
    }
}
