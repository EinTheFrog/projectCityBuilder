package view;

import core.Aura;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import render.GameApp;


public class CellView extends Polygon{

    private final Color TAVERN_AURA_COLOR = Color.rgb(173,255,47, 0.5);
    private final Color CASTLE_AURA_COLOR = Color.rgb(30,144,255, 0.5);
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
        setStroke(borderColor);
        setFill(new Color(0,0,0,0));
        //setFocusTraversable(false);
    }

    public void setAuraColor(Aura aura) {
        switch (aura) {
            case TAVERN: setFill(TAVERN_AURA_COLOR);
            case CASTLE: setFill(CASTLE_AURA_COLOR);
        }
        this.setFill(new Color(0,0,0,0));
    }

    public void clearAuraColor() {
        this.setFill(new Color(0,0,0,0));
    }
}
