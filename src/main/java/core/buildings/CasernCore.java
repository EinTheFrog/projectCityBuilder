package core.buildings;

import core.Aura;
import view.buildings.AbstractBuildingView;


public class CasernCore extends AbstractBuilding {
    private AbstractBuildingView output;
    private int GOLD_PROFIT = 0;
    private int GOLD_COST = 50;
    private int FORCE_PROFIT = 5;
    private int PEOPLE_CHANGE = -10;
    private String NAME = "Casern";

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
        if (alienAuras.contains(Aura.TAVERN)) profit += 5;
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
        return Aura.NONE;
    }

}