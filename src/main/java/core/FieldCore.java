package core;

import controller.Controller;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import logic.Pair;
import output.CellOutput;
import output.FieldOutput;
import render.GameApplication;
import render.Main;

import java.util.ArrayList;
import java.util.List;


public class FieldCore {
    CellCore[][] cellsArray;
    private double fieldX;
    private double fieldY;
    private double fieldMoveX;
    private double fieldMoveY;
    private double fieldCenterX;
    private double fieldCenterY;
/*    private double fieldScrollX;
    private double fieldScrollY;*/
/*    private double fieldRealX;
    private double fieldRealY;*/
    private int size = 0; //кол-во клеток на одной стороне игрвого поля
    private double cellSide;
    private double moveRange;
    private final double moveSpeedDenom = 8.0; //постоянная, отвечающая за скорость перемещения камеры (делитель)
    private double scaleValue = 1.0;
    private FieldOutput output;
    private Color cellColor;
    private double cellHeight;
    private double cellWidth;
    private double fieldSide;
    private double indent;
    private Pane parentPane;
    private double width;
    private double height;
    private double cellIndentX;
    private double cellIndentY;
    private List<AbstractBuilding> buildingList;

    public FieldCore (int size, double cellSide, double fieldSide, Color cellColor, Pane parentPane, double indent) {
        //задаем параметры поля
        fieldMoveX = indent;
        fieldMoveY = indent;
/*        fieldRealX = fieldX;
        fieldRealY = fieldY;*/
        this.indent = indent;
        this.size = size;
        this.cellSide = cellSide;
        this.fieldSide = fieldSide;
        this.parentPane = parentPane;
        cellHeight = 2 * cellSide * Math.sin(Math.PI / 6);
        cellWidth = 2 * cellSide * Math.cos(Math.PI / 6);
        this.cellColor = cellColor;
        moveRange = cellSide / moveSpeedDenom;
        this.width = 2 * fieldSide * Math.cos(Math.PI / 6);
        this.height = 2 * fieldSide * Math.sin(Math.PI / 6);
        output = new FieldOutput(this);
        buildingList = new ArrayList<>();
        //создаем массив для хранения клеток
        cellsArray = new CellCore[size][size];
        //создаем клетки
        createCells();
    }

    //метод для приближения камеры
    public void zoom (double scrollValue) {
        //высчитываеи относительные координаты курсора на поле
        double centerRelatX = fieldX / GameApplication.paneWidth;
        double centerRelatY = fieldY / GameApplication.paneHeight;
        //увеличивая значение scale создаем эфект приближения камеры
        if (scaleValue + scrollValue / Controller.baseScroll > 0 && scaleValue + scrollValue / Controller.baseScroll < 20)
            scaleValue += scrollValue / Controller.baseScroll;
        output.zoom(scaleValue);

        //вычисляем новую ширину и высоту
        width = GameApplication.paneWidth * scaleValue;
        height = GameApplication.paneHeight * scaleValue;

        move(0, 0);
        //высчитываеи относительные координаты курсора на поле после масштабирования поля
/*        double centerRelatNewX = fieldX / (GameApplication.paneWidth * scaleValue);
        double centerRelatNewY = fieldY / (GameApplication.paneHeight * scaleValue);*/
        //debug
/*        System.out.println("common  " + centerX + " " + centerY);
        System.out.println("old " + centerRelatX + " " + centerRelatY);
        System.out.println("new " + centerRelatNewX + " " + centerRelatNewY);*/
        //передвигаем поле так, чтобы относительные координаты курсора в итоге остались прежними
/*        double dx = (centerRelatX - centerRelatNewX) * width;
        double dy = (centerRelatY - centerRelatNewY) * height;
        output.move( fieldX- dx,fieldY - dy);
        fieldScrollY = dx;
        fieldScrollY = dy;*/
        //изменяем скорость перемщения камеры, чтобы при сильном приближении камера не двигалась слишком быстро
        moveRange = cellSide * scaleValue / moveSpeedDenom;
    }

    // метод для перемщения камеры (самого поля относительно камеры)
    public void move(double dx, double dy) {
        fieldMoveX += dx;
        fieldMoveY += dy;
        fieldX = (fieldMoveX - GameApplication.paneWidth / 2) * scaleValue + GameApplication.paneWidth / 2;
        fieldY = (fieldMoveY - GameApplication.paneHeight / 2) * scaleValue + GameApplication.paneHeight / 2;
        output.move(fieldX, fieldY);
    }

    // метод заполнения поля клетками
    private void createCells() {
        //вспомогательная переменные
        cellIndentX = cellWidth / 2;
        cellIndentY = fieldSide * Math.sin(Math.PI / 6) + cellHeight / 2;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double x = j * cellWidth / 2 + cellIndentX;
                double y = cellIndentY - j * cellHeight / 2;
                //создаем клетку
                CellCore cell = new CellCore(x, y, cellSide, cellWidth, cellHeight, cellColor, this, i, j);
                cellsArray[j][i] = cell; //добавляем ее в массив
                output.add(cell.getOutput()); // отрисовывем ее
            }
            cellIndentX += (cellWidth / 2);
            cellIndentY += (cellHeight / 2);
        }
    }

    //метод для нахождения клетки по координатам (используется в Controller.clickOnBuilding)
    public CellCore findCell(double x, double y) {
        int j = -1;
        x -= cellWidth / 2;
        y -= fieldSide * Math.sin(Math.PI / 6) + cellHeight / 2;
        while (y <= Math.tan(Math.PI / 6) * x - j * cellHeight) {
            j++;
        }
        j--;
        int i = -1;
        while (y >= - Math.tan(Math.PI / 6) * x + i * cellHeight) {
            i++;
        }
        if (i >= 0 && j >= 0 && i < size && j < size) return cellsArray[j][i];
        else return null;
    }

    //метод для перерисовки зданий на переднем плане
    public void redrawCloserBuildings (Pair<Integer> indices) {
        int startX = indices.first;
        int startY = indices.second;
        while (startX > 0 && startY > 0) {
            startX--;
            startY--;
        }
        while (startX > 0) {
            for (int i = 0; i + startX < size; i ++) {
                if (cellsArray[startX + i][i].getBuilding() != null) cellsArray[startX + i][i].getBuilding().redraw();
            }
            startX--;
        }
        while (startY < size) {
            for (int i = 0; i + startY < size; i ++) {
                if (cellsArray[i][startY + i].getBuilding() != null) cellsArray[i][startY + i].getBuilding().redraw();
            }
            startY++;
        }
    }


    //метод для получения соседей клетки
    public List<CellCore> getNeighbours(CellCore cell, AbstractBuilding building) {
        List<CellCore> neighbours = new ArrayList<>();
        int cellX = cell.getIndices().first;
        int cellY = cell.getIndices().second;
        for (int i = cellY + 1 - building.getScale() * building.getLength(); i <= cellY; i ++) {
            for(int j = cellX; j <= cellX - 1 + building.getScale() * building.getWidth(); j ++) {
                if (i >= 0 && j >= 0 && i < size && j < size ) {
                    neighbours.add(cellsArray[j][i]);
                }
            }
        }
        return neighbours;
    }

    //
    public void addBuilding(AbstractBuilding building) {
        buildingList.add(building);
    }

    //getters
    public FieldOutput getOutput() { return output;}
    public double getMoveRange() {return moveRange;}
    public double getX() {return fieldX;}
    public double getY() {return  fieldY;}
/*    public double getRealX() {return fieldRealX;}
    public double getRealY() {return  fieldRealY;}*/
    public Pane getParentPane() {return  parentPane;}
    public double getIndent() {return indent;}
    public double getWidth() {return width;}
    public double getHeight() {return height;}
    public double getCellWidth() {return cellWidth;}
    public double getCellHeight() {return cellHeight;}
    public double getCellScale() {return cellsArray[0][0].getOutput().getScaleX();}
    public List<AbstractBuilding> getBuildingsList() { return buildingList;}
}
