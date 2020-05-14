package core.buildings;

import core.Aura;
import core.FieldCore;
import output.AbstractBuildingOutput;
import output.CasernOutput;

public class CasernCore extends AbstractBuilding {
    private AbstractBuildingOutput output;
    private static final double PIC_WIDTH = 128.0;
    private static final double PIC_HEIGHT = 197.0;
    public CasernCore(double x, double y, int width, int length, int scale, FieldCore field, double opacity) {
        super(x, y, width, length, scale, field, opacity);
    }

    @Override
    protected AbstractBuildingOutput getOutput() {
        if (output == null) output = new CasernOutput(this);
        return output;
    }
    @Override
    public AbstractBuilding copy() {
        return new CasernCore(x, y, width, length, size, field, opacity);
    }

    @Override
    public int getGoldProfit() {
        return 0;
    }

    @Override
    public int getGoldCost() {
        return 50;
    }

    @Override
    public int getForceProfit() {
        int profit = 5;
        if (alienAuras.contains(Aura.TAVERN)) profit -= 2;
        return profit;
    }

    @Override
    public int getPeopleChange() {
        return -10;
    }

    @Override
    public Aura getOwnAura() {
        return Aura.NONE;
    }

    @Override
    public String getName() {
        return "Casern";
    }

    @Override
    public double getPicHeight() {
        return PIC_HEIGHT * field.getCellWidth()/ PIC_WIDTH * size;
    }

    @Override
    public String getPicPath() {
        return "/textures/casern.png";
    }

    @Override
    public double getPicWidth() {
        return field.getCellWidth() * size;
    }
}
