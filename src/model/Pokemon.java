package model;

public class Pokemon {
    private String name;
    private String type;
    private int level;
    private boolean shiny;
    private String resourceName;

    public Pokemon(String name, String type, int level, boolean shiny) {
        this(name, type, level, shiny, defaultResourceName(name));
    }

    public Pokemon(String name, String type, int level, boolean shiny, String resourceName) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.shiny = shiny;
        this.resourceName = resourceName;
    }

    //Getters:
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public int getLevel(){
        return level;
    }
    public boolean isShiny(){
        return shiny;
    }
    public String getResourceName() {
        return resourceName;
    }

    //Setters:
    public void setLevel(int level){
        this.level = level;
    }
    public void setShiny(boolean shiny){
        this.shiny = shiny;
    }
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String toString() {
        String shinyText = shiny ? "✨ Shiny" : "Normal";
        return name + " (Lv." + level + ", " + type + ", " + shinyText + ")";
    }

    private static String defaultResourceName(String name) {
        return name.toLowerCase().replace(" ", "-");
    }
}
