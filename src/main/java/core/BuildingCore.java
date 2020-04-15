package core;

import output.BuildingOutput;
import output.FieldOutput;

import java.io.FileNotFoundException;

public class BuildingCore {
    private double x;
    private double y;
    private BuildingOutput output;
    private FieldOutput parentField;
    private static double width = 128.0;
    private static double height = 128.0;

    public BuildingCore (double x, double y, double cellWidth, double cellHeight, FieldOutput field) throws FileNotFoundException {
        this.x = x;
        this.y = y;
        parentField = field;
        height *= cellWidth/ width;
        width = cellWidth;
        output = new BuildingOutput(x, y, width, height, cellWidth, cellHeight, field, this);
    }

    //getters
    public double getHeight() {return height;}
    public double getX() {return x;}
    public double getY() {return y;}
    public FieldOutput getParentField() {return parentField;}
    public BuildingOutput getOutput() {return output;}
}
