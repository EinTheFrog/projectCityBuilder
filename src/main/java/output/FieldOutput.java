package output;

import controller.Controller;
import core.FieldCore;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class FieldOutput extends Pane{
    private Pane parentPane;
    private double cellSide;
    private double cellHeight;
    private double intend;
    private FieldCore fieldCore;
    private final Color cellColor = Color.rgb(178, 178, 177);

    public FieldOutput (int size, double cellSide, Pane parentPane, double intend) {
        this.setFocusTraversable(true);
        this.parentPane = parentPane;
        this.intend = intend;
        fieldCore = new FieldCore(size, cellSide, intend,this);
        this.cellSide = cellSide;
        cellHeight = cellSide * Math.sin(Math.PI / 6);
        this.setPrefSize(cellSide * size * (1 + Math.cos(Math.PI / 6)) + 2 * intend, cellHeight * size + 2 * intend);
        this.parentPane.setBackground(new Background(new BackgroundFill(Color.rgb(133, 106, 84), null, null)));
        this.parentPane.getChildren().add(this);


        this.addEventHandler(ScrollEvent.SCROLL, event -> {
            Controller.zoom(event.getDeltaY(), fieldCore);
        });
        this.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            Controller.keyPressed(event.getCode(), fieldCore);
        });
        this.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            Controller.keyReleased(event.getCode(), fieldCore);
        });
    }
    public void zoom (double scaleValue) {
        this.setScaleX(scaleValue);
        this.setScaleY(scaleValue);
    }

    public void move (double x, double y, double dw, double dh) {
        parentPane.setPrefSize(this.getPrefWidth() + dw, this.getPrefHeight() + dh);
        this.relocate(x, y);
    }



    public void add (Node node) {this.getChildren().add(node);}

    public double getCellSide () {return cellSide;}

    public double getIntend() { return intend; }

    public Color getCellColor() {return cellColor;}

    public FieldCore getCore() {return fieldCore;}
}
