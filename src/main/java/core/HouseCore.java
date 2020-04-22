package core;

import output.AbstractBuildingOutput;
import output.HouseOutput;

public class HouseCore extends AbstractBuilding {
    AbstractBuildingOutput output;
    public HouseCore(double x, double y, int width, int length, int scale, FieldCore field, double opacity) {
        super(x, y, width, length, scale, field, opacity);
    }

    @Override
    protected AbstractBuildingOutput getOutput() {
        if (output == null) output = new HouseOutput(this);
        return output;
    }

    @Override
    public HouseCore copy() {
        return new HouseCore(x, y, width, length, scale, parentField, opacity);
    }

}
