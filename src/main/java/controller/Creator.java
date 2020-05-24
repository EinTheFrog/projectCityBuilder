package controller;

import core.CellCore;
import core.FieldCore;
import core.Resources;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.input.KeyEvent;
import view.Visibility;
import core.buildings.AbstractBuilding;
import view.CellView;
import view.FieldView;
import view.buildings.*;

/**
 * вспомогательный класс для создания сущностей
 */
public abstract class Creator {

    public static FieldView createField(int size, double indent, double fieldSide, ReadOnlyDoubleProperty paneWidth,
                                        ReadOnlyDoubleProperty paneHeight, GameAppController controller,
                                        Resources gameResources) {
        FieldCore fieldCore = new FieldCore(size, gameResources);
        final double moveSpeed = fieldSide / (4 * size);
        FieldView fieldView = new FieldView(fieldCore, controller, size, indent, fieldSide,
                paneWidth, paneHeight, moveSpeed);
        return fieldView;
    }

    public static void createCellsForField(FieldView fieldView) {
        for (int i = 0; i < fieldView.SIZE; i++) {
            for (int j = 0; j < fieldView.SIZE; j++) {
                CellCore cellCore = new CellCore(j, i);
                CellView cellView = new CellView(fieldView.getCellWidth(), fieldView.getCellHeight(), cellCore);
                CellController.addEventHandlers(cellView);
                fieldView.addCell(cellView);
            }
        }
    }

    public static AbstractBuildingView createBuildingGhost(FieldView fieldView, AbstractBuilding buildingCore) {
        AbstractBuildingView buildingView;
        switch (buildingCore.getName()) {
            case "House": buildingView = new HouseView(buildingCore,
                    buildingCore.getSize() * fieldView.getCellWidth(), Visibility.INVISIBLE);
            break;
            case "Casern": buildingView = new CasernView(buildingCore,
                    buildingCore.getSize() * fieldView.getCellWidth(), Visibility.INVISIBLE);
            break;
            case "Tavern": buildingView = new TavernView(buildingCore,
                    buildingCore.getSize() * fieldView.getCellWidth(), Visibility.INVISIBLE);
            break;
            default: buildingView = new CastleView(buildingCore,
                    buildingCore.getSize() * fieldView.getCellWidth(), Visibility.INVISIBLE);
            break;
        }
        fieldView.addGhost(buildingView);
        return buildingView;
    }
}