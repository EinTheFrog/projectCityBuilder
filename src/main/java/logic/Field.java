package logic;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Field extends Pane {
    Map <Pair, Cell> cellsCollection = new HashMap<>();
    private int size;
    private double cellSide;
    private double cellHeight;
    public Field(int size, Cell cell) {
        this.size = size;
        this.cellSide = cell.getSide();
        cellHeight = cellSide * Math.sin(Math.PI / 6);
        this.setPrefSize( cellSide * size + cellSide * Math.cos(Math.PI / 6), cellHeight * size);
        createCells();
    }

    private void createCells () {
        double indentX = 0;
        double indentY = 150;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double x = j * cellSide + indentX;
                double y = i * cellHeight + indentY;
                Color color = Color.AQUA;
                Cell cell = new Cell(x, y, cellSide, color);
                cell.relocate(x, y);
                this.getChildren().add(cell);
                System.out.println(cell.getLayoutX());
            }
            indentX += cellSide * Math.cos(Math.PI / 6);
        }
    }
}
