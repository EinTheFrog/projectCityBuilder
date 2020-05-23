package core;

import core.buildings.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldCoreTest {

    @Test
    void addBuilding() {
        Resources gameResources = new Resources();
        FieldCore fieldCore = new FieldCore(20, gameResources);
        for (int i = 0; i < fieldCore.SIZE; i++) {
            for (int j = 0; j < fieldCore.SIZE; j++) {
                CellCore cellCore = new CellCore(j, i);
                fieldCore.addCell(cellCore);
            }
        }

        final AbstractBuilding house1 = new HouseCore(0,0,1,1,2);
        fieldCore.buildBuilding(house1);
        assertEquals(0, fieldCore.getBuildingsList().size());

        final AbstractBuilding house2 = new HouseCore(1,1,1,1,2);
        fieldCore.buildBuilding(house2);
        assertEquals(1, fieldCore.getBuildingsList().size());
        assertEquals(5, gameResources.getGoldIncome());

        fieldCore.buildBuilding(new CastleCore(8,3,1,1,4));
        fieldCore.buildBuilding(new CastleCore(8,10,1,1,5));
        assertEquals(2, fieldCore.getBuildingsList().size());
        assertEquals(0, gameResources.getForceIncome());

        gameResources.setGold(2000);
        for (int i = 11; i < 20; i ++) {
            final AbstractBuilding house3 = new HouseCore(10, i,1,1,1);
            fieldCore.buildBuilding(house3);
        }
        assertEquals(20, gameResources.getForceIncome());
        assertEquals(11, fieldCore.getBuildingsList().size());

        fieldCore.removeBuilding(1);
        assertEquals(10, fieldCore.getBuildingsList().size());

        gameResources.setGold(2000);
        final AbstractBuilding tavern = new TavernCore(3,3,1,1,2);
        fieldCore.buildBuilding(tavern);
        final AbstractBuilding houseCopy = house2.copy();
        assertEquals(10, houseCopy.getGoldProfit());
    }


    @Test
    void removeBuilding() {
        Resources gameResources = new Resources();
        FieldCore fieldCore = new FieldCore(20, gameResources);
        for (int i = 0; i < fieldCore.SIZE; i++) {
            for (int j = 0; j < fieldCore.SIZE; j++) {
                CellCore cellCore = new CellCore(j, i);
                fieldCore.addCell(cellCore);
            }
        }

        final AbstractBuilding tavern = new TavernCore(2,2,1,1,2);
        fieldCore.buildBuilding(tavern);
        assertEquals(1, fieldCore.getBuildingsList().size());

        final AbstractBuilding house1 = new HouseCore(1,1,1,1,2);
        fieldCore.buildBuilding(house1);
        assertEquals(1, fieldCore.getBuildingsList().size());
        assertEquals(0, gameResources.getGoldIncome());

        fieldCore.removeBuilding(house1);
        assertEquals(1, fieldCore.getBuildingsList().size());
        assertEquals(0, gameResources.getGoldIncome());

        final AbstractBuilding house2 = new HouseCore(4,1,1,1,2);
        fieldCore.buildBuilding(house2);
        assertEquals(2, fieldCore.getBuildingsList().size());
        assertEquals(20, gameResources.getGoldIncome());

        fieldCore.removeBuilding(tavern);
        assertEquals(1, fieldCore.getBuildingsList().size());
        assertEquals(5, gameResources.getGoldIncome());
    }

    @Test
    void beatEnemies() {
        Resources resources = new Resources();
        FieldCore fieldCore = new FieldCore(20, resources);
        for (int i = 0; i < fieldCore.SIZE; i++) {
            for (int j = 0; j < fieldCore.SIZE; j++) {
                CellCore cellCore = new CellCore(j, i);
                fieldCore.addCell(cellCore);
            }
        }
        resources.chooseField(fieldCore);

        final AbstractBuilding tavern = new TavernCore(1,1,1,1,2);
        fieldCore.buildBuilding(tavern);

        final AbstractBuilding house1 = new HouseCore(3,1,1,1,2);
        fieldCore.buildBuilding(house1);
        resources.changeTime(5_000);
        assertEquals(275, resources.getGold());

        for (int i = 5; i < 20; i += 4) {
            final AbstractBuilding house2 = new HouseCore(i,1,1,1,2);
            fieldCore.buildBuilding(house2);
        }

        for (int i = 5; i < 20; i += 4) {
            final AbstractBuilding casern = new CasernCore(i,10,1,1,2);
            fieldCore.buildBuilding(casern);
        }
        assertEquals(10, resources.getForceIncome());
        resources.changeTime(5_000);

        assertEquals(50, resources.getForce());
        resources.beatEnemies();
    }

    @Test
    void payEnemies() {
        Resources resources = new Resources();
        resources.payEnemies();
        resources.payEnemies();
        resources.payEnemies();
        assertEquals(true, resources.userLost());
    }

}