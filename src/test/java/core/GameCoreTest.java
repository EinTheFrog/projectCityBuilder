package core;

import core.buildings.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameCoreTest {

    @Test
    void addBuilding() {
        GameResources gameResources = new GameResources();
        FieldCore fieldCore = new FieldCore(20, gameResources);
        createCells(fieldCore);
        gameResources.chooseField(fieldCore);

        fieldCore.buildBuilding(new HouseCore(0,0,2,2));
        assertEquals(0, fieldCore.getBuildingsList().size());

        fieldCore.buildBuilding(new HouseCore(1,1,2,2));
        assertEquals(1, fieldCore.getBuildingsList().size());
        assertEquals(5, gameResources.getGoldIncome());

        fieldCore.buildBuilding(new CastleCore(8,3,4,4));
        fieldCore.buildBuilding(new CastleCore(8,10,5,5));
        assertEquals(2, fieldCore.getBuildingsList().size());
        assertEquals(0, gameResources.getForceIncome());

        gameResources.setGold(2000);
        for (int i = 11; i < 20; i ++) {
            fieldCore.buildBuilding(new HouseCore(10, i,1,1));
        }
        assertEquals(20, gameResources.getForceIncome());
        assertEquals(11, fieldCore.getBuildingsList().size());

        fieldCore.removeBuilding(1);
        assertEquals(10, fieldCore.getBuildingsList().size());

        gameResources.setGold(2000);
        final AbstractBuilding tavern = new TavernCore(3,3,2,2);
        fieldCore.buildBuilding(tavern);
        final AbstractBuilding houseCopy = fieldCore.getBuildingsList().get(0).copy();
        assertEquals(10, houseCopy.getGoldProfit());

        GameResources gameResources2 = new GameResources();
        FieldCore fieldCore2 = new FieldCore(2, gameResources2);
        createCells(fieldCore);
        gameResources.chooseField(fieldCore);

        fieldCore2.buildBuilding(new HouseCore(5,5,1,1));
        assertEquals(0, fieldCore2.getBuildingsList().size());

        fieldCore2.buildBuilding(new HouseCore(1,1,3,3));
        assertEquals(0, fieldCore2.getBuildingsList().size());
    }


    @Test
    void removeBuilding() {
        GameResources gameResources = new GameResources();
        FieldCore fieldCore = new FieldCore(20, gameResources);
        createCells(fieldCore);
        gameResources.chooseField(fieldCore);

        AbstractBuilding tavern  = new TavernCore(2,2,2,2);
        fieldCore.buildBuilding(tavern);
        assertEquals(1, fieldCore.getBuildingsList().size());

        final AbstractBuilding house = new HouseCore(1,1,2,2);
        fieldCore.buildBuilding(house);
        assertEquals(1, fieldCore.getBuildingsList().size());
        assertEquals(0, gameResources.getGoldIncome());

        fieldCore.removeBuilding(house);
        assertEquals(1, fieldCore.getBuildingsList().size());
        assertEquals(0, gameResources.getGoldIncome());

        fieldCore.buildBuilding(new HouseCore(4,1,2,2));
        assertEquals(2, fieldCore.getBuildingsList().size());
        assertEquals(20, gameResources.getGoldIncome());

        fieldCore.removeBuilding(tavern);
        assertEquals(1, fieldCore.getBuildingsList().size());
        assertEquals(5, gameResources.getGoldIncome());
    }

    @Test
    void beatEnemies() {
        GameResources gameResources = new GameResources();
        FieldCore fieldCore = new FieldCore(20, gameResources);
        createCells(fieldCore);
        gameResources.chooseField(fieldCore);

        final AbstractBuilding tavern = new TavernCore(1,2,2,1);
        fieldCore.buildBuilding(tavern);

        final AbstractBuilding house1 = new HouseCore(3,2,2,1);
        fieldCore.buildBuilding(house1);
        gameResources.changeTime(5_000);
        assertEquals(275, gameResources.getGold());

        for (int i = 5; i < 20; i += 4) {
            final AbstractBuilding house2 = new HouseCore(i,1,2,2);
            fieldCore.buildBuilding(house2);
        }

        for (int i = 5; i < 20; i += 4) {
            final AbstractBuilding casern = new CasernCore(i,10,2,2);
            fieldCore.buildBuilding(casern);
        }
        assertEquals(10, gameResources.getForceIncome());
        gameResources.changeTime(5_000);

        assertEquals(50, gameResources.getForce());
        gameResources.beatEnemies();
    }

    @Test
    void payEnemies() {
        GameResources gameResources = new GameResources();
        gameResources.payEnemies();
        gameResources.payEnemies();
        gameResources.payEnemies();
        assertEquals(true, gameResources.userLost());
    }

    /**
     * Вспомогательный метод для создания клеток на поле
     * @param fieldCore
     */
    private void createCells(FieldCore fieldCore) {
        for (int i = 0; i < fieldCore.SIZE; i++) {
            for (int j = 0; j < fieldCore.SIZE; j++) {
                CellCore cellCore = new CellCore(j, i);
                fieldCore.addCell(cellCore);
            }
        }
    }

}