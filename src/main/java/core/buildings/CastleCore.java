package core.buildings;

import core.Aura;

import java.util.List;


public class CastleCore extends AbstractBuilding {
    private final int GOLD_PROFIT = 0;
    private final int GOLD_COST = 150;
    private final int FORCE_PROFIT = 20;
    private final int PEOPLE_CHANGE = -20;
    private final String NAME = "Castle";

    public CastleCore(int x, int y, int width, int length) {
        super(x, y, width, length);
    }

    public CastleCore(int x, int y, int width, int length, List<Aura> aurasList) {
        super(x, y, width, length);
        this.alienAuras = aurasList;
    }

    @Override
    public AbstractBuilding copy() {
        CastleCore copy =  new CastleCore(x, y, width, length, alienAuras);
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
    public Aura getOwnAura() {
        return Aura.NONE;
    }

}
