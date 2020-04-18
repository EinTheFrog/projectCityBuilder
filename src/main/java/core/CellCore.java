package core;

import javafx.scene.paint.Color;
import logic.BuildingTypes;
import logic.Pair;
import output.CellOutput;
import java.io.FileNotFoundException;

public class CellCore {
    private double x;
    private double y;
    private double side;
    private CellOutput output;
    private FieldCore field;
    private double width;
    private double height;
    private BuildingCore building;
    private int indX;
    private int indY;

    //конструктор
    public CellCore (double x, double y, double side, double width, double height, Color color, FieldCore field, int i, int j) {
        this.x = x;
        this.y = y;
        this.side = side;
        this.field = field;
        this.width = width;
        this.height = height;
        output = new CellOutput(x, y, width, height, color, field.getOutput(), this);
        indX = j;
        indY = i;
    }

    //метод для добавления здания
    public void buildBuilding (BuildingTypes type, int scale) throws FileNotFoundException {
        building = new BuildingCore(x, y, width,  height, field.getOutput(), type, scale);
    }

    //метод для присвоения клетке уже существующего здания (нужен, чтобы задать здание, занимающее более 1 клетки)
    public void setBuilding (BuildingCore building) {
        this.building = building;
    }

    //getters
    public FieldCore getField() {
        return field;
    }
    public BuildingCore getBuilding () {
        return building;
    }
    public Pair<Integer> getIndices() {
        return new Pair<>(indX, indY);
    }
    public CellOutput getOutput() {return output;}
}
