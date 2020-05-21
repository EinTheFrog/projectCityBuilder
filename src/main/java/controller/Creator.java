package controller;

import core.CellCore;
import core.Economy;
import core.FieldCore;
import view.Visibility;
import core.buildings.AbstractBuilding;
import view.CellView;
import view.FieldView;
import view.buildings.*;

import java.util.Timer;

/**
 * вспомогательный класс для создания сущностей
 */
public abstract class Creator {

    public static FieldCore createField() {
        FieldCore fieldCore = new FieldCore();
        FieldView fieldView = new FieldView();
        fieldCore.addView(fieldView);
        FieldController fieldController = new FieldController(fieldCore, fieldView);
        fieldController.addEventHandlers();
        return fieldCore;
    }

    public static void createCellsForField(FieldCore fieldCore) {
        for (int i = 0; i < fieldCore.SIZE; i++) {
            for (int j = 0; j < fieldCore.SIZE; j++) {
                CellCore cellCore = new CellCore(j, i);
                cellCore.addView(new CellView());
                CellController cellController = new CellController(cellCore, cellCore.getView());
                cellController.addEventHandlers();
                fieldCore.addCell(cellCore);
            }
        }
    }

    public static AbstractBuilding createBuildingGhost(FieldView fieldView, AbstractBuilding buildingCore) {
        AbstractBuildingView buildingView;
        switch (buildingCore.getName()) {
            case "House": buildingView = new HouseView(buildingCore.getSize(), Visibility.INVISIBLE); break;
            case "Casern": buildingView = new CasernView(buildingCore.getSize(), Visibility.INVISIBLE); break;
            case "Tavern": buildingView = new TavernView(buildingCore.getSize(), Visibility.INVISIBLE); break;
            default: buildingView = new CastleView(buildingCore.getSize(), Visibility.INVISIBLE); break;
        }
        buildingCore.addView(buildingView);
        fieldView.addBuilding(buildingView);
        return buildingCore;
    }

    public static Timer createTimer() {
        return new Timer(true);
    }

    public static void buildBuilding (AbstractBuilding buildingGhost, FieldCore fieldCore)  {
        AbstractBuilding building = buildingGhost.copy();
        AbstractBuildingView buildingView = buildingGhost.getView().copy();
        Economy.buyBuilding(building.getGoldCost(), building.getPeopleChange());
        building.addView(buildingView);
        BuildingController controller = new BuildingController(building, buildingView);
        controller.addEventHandlers();
        building.setVisibility(Visibility.VISIBLE);

        fieldCore.removeBuildingGhost();
        fieldCore.checkAreaForAuras(building);
        fieldCore.setAuraForArea(building);
        fieldCore.setBuildingForArea(building);
        fieldCore.addBuilding(building);
    }
}