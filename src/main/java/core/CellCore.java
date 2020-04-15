package core;

import javafx.scene.paint.Color;
import output.CellOutput;
import output.FieldOutput;
import java.io.FileNotFoundException;

public class CellCore {
    private double x;
    private double y;
    private double side;
    private CellOutput output;
    private FieldOutput field;
    private boolean hasBuilding = false;

    //конструктор
    public CellCore (double x, double y, double side, double width, double height, Color color, FieldOutput field) {
        this.x = x;
        this.y = y;
        this.side = side;
        this.field = field;
        output = new CellOutput(x, y, width, height, color, field, this);
    }

    //метод для добавления здания
    public void buildBuilding () throws FileNotFoundException {
        BuildingCore house = new BuildingCore(x, y, side, 2 * side, field);
        hasBuilding = true;
        field.add(house.getOutput());
    }

    //getters
    public CellOutput getOutput() {return output;}
}
