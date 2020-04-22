package core;

import logic.BuildingTypes;
import output.AbstractBuildingOutput;
import output.HouseOutput;

public class HouseCore extends AbstractBuilding {

    AbstractBuildingOutput output;
    public HouseCore(double x, double y, int width, int length, int scale, FieldCore field,
                     BuildingTypes type, double opacity) {
        super(x, y, width, length, scale, field, type, opacity);
        output = new HouseOutput(this);
        parentField.getOutput().add(output);
    }

    @Override
    protected AbstractBuildingOutput getOutput() {
        if (output == null) output = new HouseOutput(this);
        System.out.println("getOutput");
        return output;
    }

}
