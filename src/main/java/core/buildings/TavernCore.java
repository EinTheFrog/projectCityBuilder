package core.buildings;

import core.Aura;
import view.buildings.AbstractBuildingView;


public class TavernCore extends AbstractBuilding {
    private int GOLD_PROFIT = 10;
    private int GOLD_COST = 100;
    private int FORCE_PROFIT = 0;
    private int PEOPLE_CHANGE = -5;
    private String NAME = "Tavern";

    public TavernCore(int x, int y, int width, int length, int size) {
        super(x, y, width, length, size);
    }

    @Override
    public AbstractBuilding copy() {
        TavernCore copy =  new TavernCore(x, y, width, length, size);
        return copy;
    }


    @Override
    public int getGoldProfit() {
        int profit = GOLD_PROFIT;
        return profit;
    }

    @Override
    public int getGoldCost() { return GOLD_COST; }

    @Override
    public int getForceProfit() {
        return FORCE_PROFIT;
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
        return Aura.TAVERN;
    }

}
