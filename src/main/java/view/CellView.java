package view;

import controller.Controller;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import render.GameApp;


public class CellView extends Polygon{
    Color fillColor = Color.rgb(10, 106, 84);
    Color borderColor = Color.rgb(220, 220, 220);

    public static DoubleProperty widthProperty = new SimpleDoubleProperty(GameApp.CELL_SIDE * Math.cos(Math.PI / 6) * 2);
    public static DoubleProperty heightProperty =new SimpleDoubleProperty( GameApp.CELL_SIDE * Math.sin(Math.PI / 6) * 2);

    public CellView () {
        double width = widthProperty.getValue();
        double height = heightProperty.getValue();
        this.getPoints().addAll(
                0.0 , 0.0,
                - width / 2, - height / 2,
                0.0 , - height,
                width / 2, - height / 2
        );
        //отрисовывем клетку
        this.setStroke(borderColor);
        this.setFill(fillColor);
    }


    public void clearAuraColor() {
        this.setFill(fillColor);
    }
}
