package core;

import output.AbstractBuildingOutput;
import output.TavernOutput;

public class TavernCore extends AbstractBuilding {
    private AbstractBuildingOutput output;
    private static final double PIC_WIDTH = 128.0;
    private static final double PIC_HEIGHT = 158.0;

    public TavernCore(double x, double y, int width, int length, int scale, FieldCore field, double opacity) {
        super(x, y, width, length, scale, field, opacity);
    }

    @Override
    protected AbstractBuildingOutput getOutput() {
        if (output == null) output = new TavernOutput(this);
        return output;
    }

    @Override
    public AbstractBuilding copy() {
        return new TavernCore(x, y, width, length, scale, field, opacity);
    }

    @Override
    public double getPicWidth() {
        return field.getCellWidth() * scale;
    }

    @Override
    public double getPicHeight() {
        return PIC_HEIGHT * field.getCellWidth() / PIC_WIDTH * scale;
    }
}
