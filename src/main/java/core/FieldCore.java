package core;

import controller.Controller;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import output.FieldOutput;
import render.GameApplication;
import java.util.ArrayList;
import java.util.List;


public class FieldCore {
    CellCore[][] cellsArray;
    private double fieldX;
    private double fieldY;
    private double fieldMoveX;
    private double fieldMoveY;
    private int size; //кол-во клеток на одной стороне игрвого поля
    private double cellSide;
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
    private int gold;
    //для каждого поля у нас своё положение камеры, а значит у каждого поля должны быть свои параметры scale
    // и скорости перемщения камеры
    private double moveRange;
    private double scaleValue = 1.0;

    public FieldCore (int size, double cellSide, double fieldSide, Color cellColor, Pane parentPane, double indent) {
        //задаем параметры поля
        fieldMoveX = 0;
        fieldMoveY = 0;
        this.indent = indent;
        this.size = size;
        this.cellSide = cellSide;
        this.fieldSide = fieldSide;
        this.parentPane = parentPane;
        //раситываем параметры
        cellHeight = 2 * cellSide * Math.sin(Math.PI / 6);
        cellWidth = 2 * cellSide * Math.cos(Math.PI / 6);
        this.cellColor = cellColor;
        moveRange = cellSide / Controller.moveSpeedDenom;
        this.width = 2 * fieldSide * Math.cos(Math.PI / 6);
        this.height = 2 * fieldSide * Math.sin(Math.PI / 6);
        gold = 10000;
        //вычитываем координаты поля для его отрисовки
        fieldX = (fieldMoveX + indent - GameApplication.mainWindowWidth / 2) * scaleValue + GameApplication.mainWindowWidth / 2;
        fieldY = (fieldMoveY + indent - GameApplication.mainWindowHeight / 2) * scaleValue + GameApplication.mainWindowHeight / 2;
        //создаем графическую оболочку и перемещаем ее на начальную позицию
        output = new FieldOutput(this);
        move(0, 0);
        //создаем список для хранения построенных зданий
        buildingList = new ArrayList<>();
        //создаем массив для хранения клеток
        cellsArray = new CellCore[size][size];
        //создаем клетки
        createCells();
    }

    //метод для приближения камеры
    public void zoom (double scrollValue) {
        //увеличивая значение scale создаем эфект приближения камеры
        if (scaleValue + scrollValue / Controller.BASE_SCROLL > 0 && scaleValue + scrollValue / Controller.BASE_SCROLL < 20)
            scaleValue += scrollValue / Controller.BASE_SCROLL;
        output.zoom(scaleValue);

        //вычисляем новую ширину и высоту
        width = GameApplication.paneWidth * scaleValue;
        height = GameApplication.paneHeight * scaleValue;

        //перемещаем поле таким образом, чтобы поле не перемещалось относительно камеры при масштабировании
        move(0, 0);

        //изменяем скорость перемщения камеры, чтобы при сильном приближении камера не двигалась слишком быстро
        moveRange = cellSide * scaleValue / Controller.moveSpeedDenom;
    }

    //метод для перемщения камеры (самого поля относительно камеры)
    public void move(double dx, double dy) {
        fieldMoveX += dx;
        fieldMoveY += dy;
        //вычитываем координаты поля для его отрисовки
        fieldX = (fieldMoveX + indent - GameApplication.mainWindowWidth / 2) * scaleValue + GameApplication.mainWindowWidth / 2;
        fieldY = (fieldMoveY + indent - GameApplication.mainWindowHeight / 2) * scaleValue + GameApplication.mainWindowHeight / 2;
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
            }
            cellIndentX += (cellWidth / 2);
            cellIndentY += (cellHeight / 2);
        }
    }

    //метод для нахождения клетки по координатам (используется в Controller.clickOnBuilding)
    public CellCore findCell(double x, double y) {
        //изменяем координаты курсора так, чтобы его положение правильно вопронималось относительно клеток при масштабировании поля
        double cursorX = (x - GameApplication.mainWindowWidth / 2) / scaleValue + GameApplication.mainWindowWidth / 2 - indent;
        double cursorY = (y - GameApplication.mainWindowHeight / 2) / scaleValue + GameApplication.mainWindowHeight / 2 - indent;
        //ищем клетку
        int j = -1;
        cursorX -= cellWidth / 2;
        cursorY -= fieldSide * Math.sin(Math.PI / 6) + cellHeight / 2;
        while (cursorY <= Math.tan(Math.PI / 6) * cursorX - j * cellHeight) { j++; }
        j--;
        int i = -1;
        while (cursorY >= - Math.tan(Math.PI / 6) * cursorX + i * cellHeight) { i++; }
        if (i >= 0 && j >= 0 && i < size && j < size) return cellsArray[j][i];
        else return null;
    }


    //метод для перерисовки зданий на переднем плане
    public void redrawCloserBuildings (int x, int y, int buildingWidth, int buildingLength) {
        int startX = x;
        int startY = y + 1 - buildingLength;
        while (startX > 0 && startY > 0) {
            startX--;
            startY--;
        }
        while (startX > 0) {
            for (int i = 0; i + startX < size; i ++) {
                if (cellsArray[startX + i][i].getBuilding() != null) {
                    //вспомогательные перменные и вычисления (не работает)
                    AbstractBuilding b = cellsArray[startX + i][i].getBuilding();
                    CellCore c = b.getCells().get(0);
                    int bx = c.getIndX();
                    int by = c.getIndY();
                    if (startX > bx && startY <= by || startY < by && startX >= bx) b.draw();
                }
            }
            startX--;
        }
        while (startY < size) {
            for (int i = 0; i + startY < size; i ++) {
                if (cellsArray[i][startY + i].getBuilding() != null) cellsArray[i][startY + i].getBuilding().draw();
            }
            startY++;
        }
    }


    //метод для получения соседей клетки
    public List<CellCore> getNeighbours(CellCore cell, AbstractBuilding building) {
        List<CellCore> neighbours = new ArrayList<>();
        int cellX = cell.getIndX();
        int cellY = cell.getIndY();
        for (int i = cellY + 1 - building.getSize() * building.getLength(); i <= cellY; i ++) {
            for(int j = cellX; j <= cellX - 1 + building.getSize() * building.getWidth(); j ++) {
                if (i >= 0 && j >= 0 && i < size && j < size ) {
                    neighbours.add(cellsArray[j][i]);
                }
            }
        }
        return neighbours;
    }

    public void gainGold () {
        for (AbstractBuilding building: buildingList) {
            gold += building.getGoldProfit();
        }
        GameApplication.writeGold(gold);
    }

    public void buyBuilding (AbstractBuilding building) {
        gold -= building.getGoldCost();
        GameApplication.writeGold(gold);
    }

    //метод для добавления новго здания в список зданий
    public void addBuilding(AbstractBuilding building) {
        buildingList.add(building);
    }

    public void removeBuilding(AbstractBuilding building) {
        buildingList.remove(building);
    }

    //getters
    public FieldOutput getOutput() { return output;}
    public double getMoveRange() {return moveRange;}
    public double getX() {return fieldMoveX;}
    public double getY() {return  fieldMoveY;}
    public Pane getParentPane() {return  parentPane;}
    public double getIndent() {return indent;}
    public double getWidth() {return width;}
    public double getScale() {return scaleValue;}
    public double getHeight() {return height;}
    public double getCellWidth() {return cellWidth;}
    public double getCellHeight() {return cellHeight;}
    public double getCellScale() {return cellsArray[0][0].getOutput().getScaleX();}
    public List<AbstractBuilding> getBuildingsList() { return buildingList;}
    public int getGold() {return gold;}
}
