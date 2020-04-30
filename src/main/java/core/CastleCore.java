package core;

import output.AbstractBuildingOutput;
import output.CastleOutput;

public class CastleCore extends AbstractBuilding {
    private AbstractBuildingOutput output;
    private static final double PIC_WIDTH = 368.0;
    private static final double PIC_HEIGHT = 345.0;
    public CastleCore(double x, double y, int width, int length, int scale, FieldCore field, double opacity) {
        super(x, y, width, length, scale, field, opacity);
    }

    @Override
    protected AbstractBuildingOutput getOutput() {
        if (output == null) output = new CastleOutput(this);
        return output;
    }

    @Override
    public AbstractBuilding copy() {
        return new CastleCore(x, y, width, length, scale, field, opacity);
    }

    @Override
    public int getGoldProfit() {
        return 0;
    }

    @Override
    public int getGoldCost() {
        return 200;
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
