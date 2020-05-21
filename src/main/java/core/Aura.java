package core;


import javafx.scene.paint.Color;

public enum Aura {
    TAVERN (Color.rgb(200, 150, 50), 3), CASTLE(Color.rgb(100, 150, 100), 20),
    NONE(Color.rgb(200, 150, 50), 0);

    private final Color color;
    private final int radius;
    Aura (Color color, int radius){
        this.color = color;
        this.radius = radius;
    }
    public int getRadius() {
        return radius;
    }
    public Color getColor() {
        return color;
    }
}
