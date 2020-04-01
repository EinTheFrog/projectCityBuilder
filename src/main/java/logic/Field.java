package logic;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Field extends Pane {
    ArrayList<Cell> cellsList= new ArrayList<>();
    private int size;
    private double cellSide;
    private double cellHeight;
    private double intend;
    public Field(int size, double x, double y, double cellSide, Color color, double intend) {
        this.intend = intend;
        this.size = size;
        this.cellSide = cellSide;
        cellHeight = cellSide * Math.sin(Math.PI / 6);
        this.setPrefSize( cellSide * size + cellSide * Math.cos(Math.PI / 6), cellHeight * size);
        this.setBackground(new Background(new BackgroundFill(Color.rgb(133, 106, 84  ), null, null)));
        createCells();
    }

    private void createCells () {
        double indentX = intend;
        double indentY = intend;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double x = j * cellSide + indentX;
                double y = i * cellHeight + indentY;
                Color color = Color.rgb(178, 178, 177 );
                Cell cell = new Cell(x, y, cellSide, color,this);
                cell.relocate(x, y);
                cellsList.add(cell);
                this.getChildren().add(cell);
            }
            indentX += cellSide * Math.cos(Math.PI / 6);
        }
    }

    public double getIntend() { return intend;}
}
