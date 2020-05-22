package core;

import controller.CellController;
import controller.Creator;
import core.buildings.AbstractBuilding;
import core.buildings.HouseCore;
import org.junit.jupiter.api.Test;
import view.CellView;

import static org.junit.jupiter.api.Assertions.*;

class FieldCoreTest {

    @Test
    void addBuilding() {
        FieldCore fieldCore = new FieldCore();
        for (int i = 0; i < fieldCore.SIZE; i++) {
            for (int j = 0; j < fieldCore.SIZE; j++) {
                CellCore cellCore = new CellCore(j, i);
                fieldCore.addCell(cellCore);
            }
        }
        AbstractBuilding house = new HouseCore(0,0,1,1,2);
        fieldCore.addBuilding(house);
    }

    @Test
    void addGhost() {
    }

    @Test
    void removeBuilding() {
    }

    @Test
    void removeBuildingGhost() {
    }

    @Test
    void removeRandomBuilding() {
    }

    @Test
    void setBuildingMod() {
    }
}