package logic;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Field extends StackPane {
    Map <Pair, Cell> cellsCollection = new HashMap<>();
    private int size;
    private double cellSide;
    private double cellWidth;
    private double cellHeight;
    public Field(int size, Cell cell) {
        this.size = size;
        this.cellSide = cell.getSide();
        cellWidth = cell.getPrefWidth();
        cellHeight = cell.getPrefHeight();
        this.setPrefSize( cellWidth* size , cellHeight * size);
        createCells();
        int a = 0;
    }

    private void createCells () {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double x = j * cellWidth;
                double y = i * cellHeight;
                Color color = j == size - 1? Color.YELLOW: Color.AQUA;
                Cell cell = new Cell(x, y, cellSide, color);
                cell.setLayoutX(x);
                cell.setLayoutY(y);
                this.getChildren().add(cell);
                System.out.println(cell.getLayoutX());
            }
        }
    }
}
