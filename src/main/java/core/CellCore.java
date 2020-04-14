package core;

import javafx.scene.paint.Color;
import output.CellOutput;
import output.FieldOutput;

public class CellCore {
    private double x;
    private double y;
    private double side;
    private CellOutput output;
    private FieldOutput field;

    //конструктор
    public CellCore (double x, double y, double side, Color color, FieldOutput field) {
        output = new CellOutput(x, y, side, color, field, this);
        this.x = x;
        this.y = y;
        this.side = side;
        this.field = field;
    }

    //метод для добавления здания
    public void buildBuilding () {
        BuildingCore house = new BuildingCore(x, y, side, 2 * side, field);
        output.getParentField().add(house.getOutput());
    }

    //getters
    public CellOutput getOutput() {return output;}
}
