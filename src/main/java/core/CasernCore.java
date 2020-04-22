package core;

import output.AbstractBuildingOutput;
import output.CasernOutput;

public class CasernCore extends AbstractBuilding {
    AbstractBuildingOutput output;
    public CasernCore(double x, double y, int width, int length, int scale, FieldCore field, double opacity) {
        super(x, y, width, length, scale, field, opacity);
    }

    @Override
    protected AbstractBuildingOutput getOutput() {
        if (output == null) output = new CasernOutput(this);
        return output;
    }
    @Override
    public CasernCore copy() {
        return new CasernCore(x, y, width, length, scale, parentField, opacity);
    }
}
