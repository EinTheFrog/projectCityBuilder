package core;

import logic.BuildingTypes;
import output.BuildingOutput;
import output.FieldOutput;

import java.io.FileNotFoundException;

public class BuildingCore {
    private double x;
    private double y;
    private BuildingOutput output;
    private FieldOutput parentField;
    private BuildingTypes type;
    private static double width = 128.0;
    private static double height = 128.0;
    private static int scaleInCells;

    public BuildingCore (double x, double y, double cellWidth, double cellHeight, FieldOutput field, BuildingTypes type,
    int scaleInCells) throws FileNotFoundException {
        this.x = x;
        this.y = y;
        parentField = field;
        height *= cellWidth/ width * scaleInCells;
        width = cellWidth * scaleInCells;
        this.type = type;
        this.scaleInCells = scaleInCells;
        output = new BuildingOutput(x, y, width, height,
                cellWidth * scaleInCells, cellHeight * scaleInCells, type, this);
        parentField.add(output);
    }

    public void redraw() {
        parentField.add(output);
    }

    //getters
    public double getHeight() {return height;}
    public double getX() {return x;}
    public double getY() {return y;}
    public FieldOutput getParentField() {return parentField;}
    public BuildingOutput getOutput() {return output;}
}
