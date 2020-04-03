package logic;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Field extends Pane {
    Cell[][] cellsArray;
    private int size;
    private double cellSide;
    private double cellHeight;
    private double intend;
    Color cellColor = Color.rgb(178, 178, 177 );
    public Field(int size, double x, double y, double cellSide, Color color, double intend) {
        cellsArray = new Cell[size][size];
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
                Cell cell = new Cell(x, y, cellSide, cellColor,this);
                cell.relocate(x, y);
                cellsArray[j][i] = cell;
                this.getChildren().add(cell);
            }
            indentX += cellSide * Math.cos(Math.PI / 6);
        }
    }

    public Cell findCell (double x1, double y1) {
        double x = x1 - intend;
        double y = y1 - intend;
        int indX = (int) ((x - (1/Math.tan(Math.PI/6)) * y) / cellSide);
        double reqY = 0;
        double cellHeight = cellSide * Math.sin(Math.PI / 6);
        int indY = 0;
        while (y > reqY) {
            reqY += cellHeight;
            indY++;
        }
        indY--;
        return cellsArray[indX][indY];
    }

    public double getIntend() { return intend;}


}
