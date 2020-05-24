package core.buildings;

import core.Aura;
import view.buildings.AbstractBuildingView;

import java.util.List;


public class TavernCore extends AbstractBuilding {
    private final int GOLD_PROFIT = 10;
    private final int GOLD_COST = 100;
    private final int FORCE_PROFIT = 0;
    private final int PEOPLE_CHANGE = -5;
    private final String NAME = "Tavern";

    public TavernCore(int x, int y, int width, int length) {
        super(x, y, width, length);
    }

    public TavernCore(int x, int y, int width, int length, List<Aura> aurasList) {
        super(x, y, width, length);
        this.alienAuras = aurasList;
    }

    @Override
    public AbstractBuilding copy() {
        TavernCore copy =  new TavernCore(x, y, width, length, alienAuras);
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
    public Aura getOwnAura() {
        return Aura.TAVERN;
    }

}
