package core.buildings;

import core.Aura;
import view.buildings.AbstractBuildingView;


public class HouseCore extends AbstractBuilding {
    private final int GOLD_PROFIT = 5;
    private final int GOLD_COST = 25;
    private final int FORCE_PROFIT = 0;
    private final int PEOPLE_CHANGE = 5;
    private final int TAVERN_GOLD_BOOST = 5;
    private final String NAME = "House";

    public HouseCore(int x, int y, int width, int length, int size) {
        super(x, y, width, length, size);
    }

    @Override
    public AbstractBuilding copy() {
       HouseCore copy =  new HouseCore(x, y, width, length, size);
       return copy;
    }


    @Override
    public int getGoldProfit() {
        int profit = GOLD_PROFIT;
        if (alienAuras.contains(Aura.TAVERN)) profit += TAVERN_GOLD_BOOST;;
        return profit;
    }

    @Override
    public int getGoldCost() {
        return GOLD_COST;
    }

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
    public Aura getOwnAura() {
        return Aura.NONE;
    }

}
