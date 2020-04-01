package logic;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Cell extends Polygon {
    private double x;
    private double y;
    private double side;
    private double pi = Math.PI;
    private Color color;

    //конструктор
    public Cell (double x, double y, double side, Color color) {
        this.x = x;
        this.y = y;
        this.side = side;
        this.color = color;
        this.getPoints().addAll(
                side * Math.cos(pi/ 6) , side * Math.sin(pi / 6),
                0.0, 0.0,
                side, 0.0,
                side * (1 + Math.cos(pi/ 6)), side * Math.sin(pi / 6)
        );
        this.setStroke(color);
    }

    public double getSide () {return side;}
}
