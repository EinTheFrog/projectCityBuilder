package controller;

import javafx.scene.input.MouseEvent;
import view.buildings.AbstractBuildingView;

public abstract class BuildingController {

    public static void addEventHandlers(AbstractBuildingView buildingView) {
        buildingView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> buildingView.isChosen.setValue(true));
    }

}