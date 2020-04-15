package core;

import output.BuildingOutput;
import output.FieldOutput;

import java.io.FileNotFoundException;

public class BuildingCore {
    private double x;
    private double y;
    private double height;
    private double side;
    private BuildingOutput output;
    private FieldOutput parentField;

    public BuildingCore (double x, double y, double side, double height, FieldOutput field) throws FileNotFoundException {
        output = new BuildingOutput(x, y, side, height, field, this);
        this.x = x;
        this.y = y;
        this.height = height;
        this.side = side;
        parentField = field;
    }

    //getters
    public double getHeight() {return height;}
    public double getX() {return x;}
    public double getY() {return y;}
    public FieldOutput getParentField() {return parentField;}
    public BuildingOutput getOutput() {return output;}
}
