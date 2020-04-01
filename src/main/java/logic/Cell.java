package logic;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Cell extends Pane {
    private double x;
    private double y;
    private double side;
    private Polygon cellForm = new Polygon();
    private double pi = Math.PI;
    private Color color;

    //конструктор
    public Cell (double x, double y, double side, Color color) {
        this.x = x;
        this.y = y;
        this.side = side;
        this.color = color;
        cellForm.getPoints().addAll(
                side * Math.cos(pi/ 6) , side * Math.sin(pi / 6),
                0.0, 0.0,
                side, 0.0,
                side * (1 + Math.cos(pi/ 6)), side * Math.sin(pi / 6)
        );
        cellForm.setStroke(color);
        this.setPrefSize(side * (1 + 2 * Math.cos(pi/ 6)), side * Math.sin(pi / 6) );
        this.getChildren().add(cellForm);
    }

    public double getSide () {return side;}
}
