package logic;

import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Cell extends Polygon {
    private double x;
    private double y;
    private double side;
    private double pi = Math.PI;
    private Color color;
    private Field parentField;

    //конструктор
    public Cell (double x, double y, double side, Color color, Field field) {
        parentField = field;
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
        this.setFill(Color.rgb(0,0,0, 0));

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("Cell");
            buildBuilding();
            event.consume();
        });
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            buildBuilding();
        });
    }

    public void buildBuilding () {
        Building house = new Building(side, 2 * side, parentField);
        house.relocate(x, y - house.getHeight());
        parentField.getChildren().add(house);
    }

    public double getSide () {return side;}
    public double getX () {return x;}
    public double getY () {return y;}

}
