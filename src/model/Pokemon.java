package model;

public class Pokemon {
    private String name;
    private String type;
    private int level;
    private boolean shiny;

    public Pokemon (String name, String type, int level, boolean shiny) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.shiny = shiny;
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

    //Setters:
    public void setLevel(int level){
        this.level = level;
    }
    public void setShiny(boolean shiny){
        this.shiny = shiny;
    }

    @Override
    public String toString() {
        String shinyText = shiny ? "✨ Shiny" : "Normal";
        return name + " (Lv." + level + ", " + type + ", " + shinyText + ")";
    }
}

