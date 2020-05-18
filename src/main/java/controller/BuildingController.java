package controller;

import core.CellCore;

import core.buildings.AbstractBuilding;
import javafx.scene.input.MouseEvent;
import view.CellView;
import view.buildings.AbstractBuildingView;

public class BuildingController {
    AbstractBuilding buildingCore;
    AbstractBuildingView buildingView;

    public BuildingController(AbstractBuilding buildingCore, AbstractBuildingView buildingView) {
        this.buildingCore = buildingCore;
        this.buildingView = buildingView;
    }
    public void addEventHandlers() {
        buildingView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

        });
    }



}