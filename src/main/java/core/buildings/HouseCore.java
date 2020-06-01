package core.buildings;

import core.Aura;

import java.util.List;


public class HouseCore extends AbstractBuilding {
    private final int GOLD_PROFIT = 5;
    private final int GOLD_COST = 25;
    private final int FORCE_PROFIT = 0;
    private final int PEOPLE_CHANGE = 5;
    private final int TAVERN_GOLD_BOOST = 5;
    private final int CASTLE_PEOPLE_BOOST = 2;
    private final String NAME = "House";

    public HouseCore(int x, int y, int width, int length) {
        super(x, y, width, length);
    }

    public HouseCore(int x, int y, int width, int length, List<Aura> aurasList) {
        super(x, y, width, length);
        this.alienAuras = aurasList;
    }

    @Override
    public AbstractBuilding copy() {
       HouseCore copy =  new HouseCore(x, y, width, length, alienAuras);
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
        int change = PEOPLE_CHANGE;
        if (alienAuras.contains(Aura.TAVERN)) change += CASTLE_PEOPLE_BOOST;;
        return change;
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
