package controller;

import core.FieldCore;
import core.GameResources;
import javafx.beans.property.ReadOnlyDoubleProperty;
import view.Visibility;
import core.buildings.AbstractBuilding;
import view.FieldView;
import view.buildings.*;

/**
 * Вспомогательный класс для создания сущностей
 */
public abstract class Creator {

    public static FieldView createField(int size, double indent, double fieldSide, ReadOnlyDoubleProperty paneWidth,
                                        ReadOnlyDoubleProperty paneHeight, GameAppController controller,
                                        GameResources gameResources) {
        FieldCore fieldCore = new FieldCore(size, gameResources);
        final double moveSpeed = fieldSide / (4 * size);
        FieldView fieldView = new FieldView(fieldCore, controller, size, indent, fieldSide,
                paneWidth, paneHeight, moveSpeed);
        return fieldView;
    }

    public static AbstractBuildingView createBuildingGhost(FieldView fieldView, AbstractBuilding buildingCore) {
        AbstractBuildingView buildingView;
        switch (buildingCore.getName()) {
            case "House": buildingView = new HouseView(buildingCore,
                    buildingCore.getWidth() * fieldView.getCellWidth(), Visibility.INVISIBLE);
            break;
            case "Casern": buildingView = new CasernView(buildingCore,
                    buildingCore.getWidth() * fieldView.getCellWidth(), Visibility.INVISIBLE);
            break;
            case "Tavern": buildingView = new TavernView(buildingCore,
                    buildingCore.getWidth() * fieldView.getCellWidth(), Visibility.INVISIBLE);
            break;
            default: buildingView = new CastleView(buildingCore,
                    buildingCore.getWidth() * fieldView.getCellWidth(), Visibility.INVISIBLE);
            break;
        }
        fieldView.addGhost(buildingView);
        return buildingView;
    }
}