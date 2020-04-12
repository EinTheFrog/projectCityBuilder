package core;

import output.BuildingOutput;
import output.FieldOutput;

public class BuildingCore {
    private double x;
    private double y;
    private double height;
    private double side;
    private BuildingOutput output;

    public BuildingCore (double x, double y, double height, double side) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.side = side;
    }
}
