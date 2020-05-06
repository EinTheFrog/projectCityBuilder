package core;

import output.AbstractBuildingOutput;
import output.CastleOutput;

public class CastleCore extends AbstractBuilding {
    private AbstractBuildingOutput output;
    private static final double PIC_WIDTH = 320.0;
    private static final double PIC_HEIGHT = 345.0;
    public CastleCore(double x, double y, int width, int length, int size, FieldCore field, double opacity) {
        super(x, y, width, length, size, field, opacity);
    }

    @Override
    protected AbstractBuildingOutput getOutput() {
        if (output == null) output = new CastleOutput(this);
        return output;
    }

    @Override
    public AbstractBuilding copy() {
        return new CastleCore(x, y, width, length, size, field, opacity);
    }

    @Override
    public int getGoldProfit() {
        return 0;
    }

    @Override
    public int getGoldCost() {
        return 300;
    }

    @Override
    public int getForceProfit() {
        return 0;
    }

    @Override
    public int getPeopleChange() {
        return -20;
    }

    @Override
    public double getPicHeight() {
        return PIC_HEIGHT * field.getCellWidth()/ PIC_WIDTH * size;
    }

    @Override
    public double getPicWidth() {
        return field.getCellWidth() * size;
    }

    @Override
    public String getName() {
        return "Castle";
    }

    @Override
    public Aura getOwnAura() {
        return Aura.CASTLE;
    }
}
