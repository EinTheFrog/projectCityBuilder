package logic;

import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.w3c.dom.events.Event;

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
        this.setFill(Color.rgb(0,0,0, 0));

        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println(x);
        });
    }

    public double getSide () {return side;}
    public double getX () {return x;}
    public double getY () {return y;}

}
