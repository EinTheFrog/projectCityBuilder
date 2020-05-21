package core.buildings;

import core.Aura;
import view.buildings.AbstractBuildingView;


public class CasernCore extends AbstractBuilding {
    private final int GOLD_PROFIT = 0;
    private final int GOLD_COST = 50;
    private final int FORCE_PROFIT = 5;
    private final int PEOPLE_CHANGE = -10;
    private final String NAME = "Casern";

    public CasernCore(int x, int y, int width, int length, int size) {
        super(x, y, width, length, size);
    }

    @Override
    public AbstractBuilding copy() {
        CasernCore copy =  new CasernCore(x, y, width, length, size);
        return copy;
    }


    @Override
    public int getGoldProfit() {
        int profit = GOLD_PROFIT;
        return profit;
    }

    @Override
    public int getGoldCost() {
        return GOLD_COST;
    }

    @Override
    public int getForceProfit() {
        int profit = FORCE_PROFIT;
        if (alienAuras.contains(Aura.TAVERN)) profit -= 3;
        return profit;
    }

    @Override
    public int getPeopleChange() {
        return PEOPLE_CHANGE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void addView(AbstractBuildingView buildingView) {
        this.buildingView = buildingView;
    }

    @Override
    public Aura getOwnAura() {
        return Aura.NONE;
    }

}