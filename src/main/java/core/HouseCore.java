package core;

import output.AbstractBuildingOutput;
import output.HouseOutput;

public class HouseCore extends AbstractBuilding {
    private AbstractBuildingOutput output;
    private static final double PIC_WIDTH = 128.0;
    private static final double PIC_HEIGHT = 128.0;
    public HouseCore(double x, double y, int width, int length, int scale, FieldCore field, double opacity) {
        super(x, y, width, length, scale, field, opacity);
    }

    @Override
    protected AbstractBuildingOutput getOutput() {
        if (output == null) output = new HouseOutput(this);
        return output;
    }

    @Override
    public AbstractBuilding copy() {
        return new HouseCore(x, y, width, length, scale, field, opacity);
    }

    @Override
    public int getGoldProfit() {
        return 5;
    }

    @Override
    public int getGoldCost() {
        return 20;
    }

    @Override
    public double getPicHeight() {
        return PIC_HEIGHT * field.getCellWidth()/ PIC_WIDTH * scale;
    }

    @Override
    public double getPicWidth() {
        return field.getCellWidth() * scale;
    }

}
