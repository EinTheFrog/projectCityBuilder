package core;

import javafx.scene.paint.Color;
import logic.Pair;
import output.CellOutput;
import output.FieldOutput;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CellCore {
    private double x;
    private double y;
    private double side;
    private CellOutput output;
    private FieldCore field;
    private double width;
    private double height;
    private BuildingCore building;
    private int depth;
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
    public void buildBuilding () throws FileNotFoundException {
        this.building = new BuildingCore(x, y, width,  height, field.getOutput());
    }

/*    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return depth;
    }*/

    public FieldCore getField() {
        return field;
    }

    public BuildingCore getBuilding () {
        return building;
    }
    public Pair<Integer> getIndices() {
        return new Pair<>(indX, indY);
    }

    //getters
    public CellOutput getOutput() {return output;}
}
