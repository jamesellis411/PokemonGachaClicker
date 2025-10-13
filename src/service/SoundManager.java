package service;

import javafx.scene.media.AudioClip;

public class SoundManager {

    private static final String BASE_PATH = "/sounds/";

    private static final AudioClip CLICK_SOUND = loadSound("click.wav");
    private static final AudioClip CAPSULE_SOUND = loadSound("capsule.wav");
    private static final AudioClip SHINY_SOUND = loadSound("shiny.wav");

    private static AudioClip loadSound(String fileName) {
        try {
            return new AudioClip(SoundManager.class.getResource(BASE_PATH + fileName).toExternalForm());
        } catch (Exception e) {
            System.out.println("⚠️ Missing sound: " + fileName);
            return null;
        }
    }

    public static void playClick() {
        if (CLICK_SOUND != null) CLICK_SOUND.play();
    }

    public static void playCapsule() {
        if (CAPSULE_SOUND != null) CAPSULE_SOUND.play();
    }

    public static void playShiny() {
        if (SHINY_SOUND != null) SHINY_SOUND.play();
    }
}
