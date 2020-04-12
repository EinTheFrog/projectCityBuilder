package core;

import output.CellOutput;
import output.FieldOutput;

public class FieldCore {
    CellOutput[][] cellsArray;
    private double fieldX = 0.0;
    private double fieldY = 0.0;
    private double size = 0;
    private double intend;
    private double cellSide;
    private double moveRange;
    private final double moveSpeedDenom = 8.0;
    private double scaleValue = 1.0;
    private FieldOutput output;

    public FieldCore (int size, double cellSide, double intend, FieldOutput output) {
        this.output = output;
        this.size = size;
        this.intend = intend;
        this.cellSide = cellSide;
        moveRange = cellSide / moveSpeedDenom;

        cellsArray = new CellOutput[size][size];
        createCells();
    }

    public void zoom (double scrollValue) {
        scaleValue += scrollValue / 100;
        output.zoom(scaleValue);
        moveRange = output.getCellSide() * scaleValue / moveSpeedDenom;
    }

    // метод для перемщения камеры (самого поля относительно камеры)
    public void move(double dx, double dy) {
        fieldX += dx;
        fieldY += dy;
        output.move(fieldX, fieldY, Math.abs(dx), Math.abs(dy));
    }

    // метод заполнения поля клетками
    private void createCells() {
        double indentX = intend;
        double indentY = intend;
        double cellHeight = cellSide * Math.sin(Math.PI / 6);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double x = j * cellSide + indentX;
                double y = i * cellHeight + indentY;
                CellOutput cell = new CellOutput(x, y, cellSide, output.getCellColor(), this.getOutput());
                cellsArray[j][i] = cell;
                output.add(cell);
            }
            indentX += cellSide * Math.cos(Math.PI / 6);
        }
    }

    //метод для нахождения клетки по координатам (используется в building)
    public CellCore findCell(double x1, double y1) {
        double x = x1 - intend;
        double y = y1 - intend;
        int indX = (int) ((x - (1 / Math.tan(Math.PI / 6)) * y) / output.getCellSide());
        double reqY = 0;
        double cellHeight = cellSide * Math.sin(Math.PI / 6);
        int indY = 0;
        while (y > reqY) {
            reqY += cellHeight;
            indY++;
        }
        indY--;
        return cellsArray[indX][indY].getCore();
    }

    public FieldOutput getOutput() { return output;}

    public double getMoveRange() {return moveRange;}
}
