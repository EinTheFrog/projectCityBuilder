package core;

import javafx.scene.paint.Color;
import output.CellOutput;

import java.util.List;

public class CellCore {
    private final double x;
    private final double y;
    private final Color borderColor;
    private final Color fillColor;
    private final double side;
    private CellOutput output;
    private final FieldCore field;
    private final double width;
    private final double height;
    private AbstractBuilding building;
    private static List<Aura> auras;
    private final int indX;
    private final int indY;

    //конструктор
    public CellCore (double x, double y, double side, double width, double height, Color borderColor, Color fillColor, FieldCore field, int i, int j) {
        //задаем значения параметров
        this.x = x;
        this.y = y;
        this.side = side;
        this.field = field;
        this.width = width;
        this.height = height;
        this.borderColor = borderColor;
        this.fillColor = fillColor;
        indX = j;
        indY = i;
    }


    public void removeBuilding() {
        setBuilding(null);
    }

    public void addAura (Aura aura) {
        auras.add(aura);
    }

    public void addAuraColor(Color color) {
        output.setFill(color);
    }
    public void removeAuraColor() {
        output.setFill(fillColor);
    }

    //метод для установки здания на все клетки
    public void setBuildingForArea(AbstractBuilding building) {
        for (CellCore neighbour: field.getCellsUnderBuilding(this, building)) {
            neighbour.setBuilding(building);
        }
    }


    //проверка свободности соседей
    public boolean neighboursFree(AbstractBuilding building) {
        for (CellCore neighbour: field.getCellsUnderBuilding(this, building)) {
            if (neighbour.getBuilding() != null) return false;
        }
        return true;
    }


    //метод для присвоения клетке уже существующего здания (нужен, чтобы задать здание, занимающее более 1 клетки)
    private void setBuilding (AbstractBuilding building) {
        this.building = building;
    }


    public void draw() {
        //создаем графическую оболочку
        output = new CellOutput(this);
    }

    //getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getSide() { return side; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Color getBorderColor() { return borderColor;}
    public Color getFillColor() { return fillColor;}
    public FieldCore getField() {
        return field;
    }
    public AbstractBuilding getBuilding () {
        return building;
    }
    public int getIndX() { return indX; }
    public int getIndY() { return indY; }
    public CellOutput getOutput() {return output;}
}
