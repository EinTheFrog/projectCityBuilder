package core;

import output.CellOutput;
import output.FieldOutput;

public class FieldCore {
    CellOutput[][] cellsArray;
    private double fieldX;
    private double fieldY;
    private double size = 0; //кол-во клеток на одной стороне игрвого поля
    private double cellSide;
    private double moveRange;
    private final double moveSpeedDenom = 8.0; //постоянная, отвечающая за скорость перемещения камеры (делитель)
    private double scaleValue = 1.0;
    private FieldOutput output;

    public FieldCore (int size, double cellSide, FieldOutput output, double indent) {
        //задаем параметры поля
        fieldX = indent;
        fieldY = indent;
        this.output = output;
        this.size = size;
        this.cellSide = cellSide;
        moveRange = cellSide / moveSpeedDenom;

        //создаем массив для хранения клеток
        cellsArray = new CellOutput[size][size];
        //создаем клетки
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
        // переменные, которые отвечают за расположение клеток на поле
        double indentX = 0;
        double indentY = 0;
        //вспомогательная переменная
        final double cellHeight = cellSide * Math.sin(Math.PI / 6);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double x = j * cellSide + indentX;
                double y = i * cellHeight + indentY;
                //создаем клетку
                CellOutput cell = new CellOutput(x, y, cellSide, output.getCellColor(), this.getOutput());
                cellsArray[j][i] = cell; //добавляем ее в массив
                output.add(cell); // отрисовывем ее
            }
            indentX += cellSide * Math.cos(Math.PI / 6);
        }
    }

    //метод для нахождения клетки по координатам (используется в Controller.clickOnBuilding)
    public CellCore findCell(double x, double y) {
        int indX = (int) ((x - (1 / Math.tan(Math.PI / 6)) * y) / output.getCellSide());
        double reqY = 0;
        double cellHeight = cellSide * Math.sin(Math.PI / 6);
        int indY = 0;
        while (y > reqY) {
            reqY += cellHeight;
            indY++;
        }
        indY--;
        if (indX >= 0 && indY >= 0 && indX < size && indY < size) return cellsArray[indX][indY].getCore();
        else return null;
    }

    public FieldOutput getOutput() { return output;}

    public double getMoveRange() {return moveRange;}
}
