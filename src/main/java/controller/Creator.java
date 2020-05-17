package controller;

import core.CellCore;
import core.Economy;
import core.FieldCore;
import view.Visibility;
import core.buildings.AbstractBuilding;
import javafx.application.Platform;
import view.CellView;
import view.FieldView;
import view.buildings.*;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Creator {
    private static TimerTask timerTask;
    private static TimerTask timerMoveTask;

    public static FieldCore createField() {
        FieldCore fieldCore = new FieldCore();
        FieldView fieldView = new FieldView();
        fieldCore.addView(fieldView);
        FieldController fieldController = new FieldController(fieldView, fieldCore);
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

    public static void startTimer(FieldCore fieldCore, Timer timer, Set<KeyboardButtons> curBtnPressed, Set<KeyboardButtons> newBtnPressed) {
        timerMoveTask = new TimerTask() {
            @Override
            public void run() {
                double dy = 0;
                double dx = 0;
                for (KeyboardButtons key: curBtnPressed) {
                    dx += key.dx;
                    dy += key.dy;
                }
                fieldCore.getView().move(dx, dy);
            }
        };

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Economy.changeTime(500);
                });
            }


        };
        timer.schedule(timerMoveTask, 0, 20);
        timer.schedule(timerTask, 500, 500);
    }

    public static void stopTimer() {
        timerMoveTask.cancel();
        timerTask.cancel();
    }

    public static void buildBuilding (AbstractBuilding buildingGhost, FieldCore fieldCore)  {
        AbstractBuilding building = buildingGhost.copy();
        AbstractBuildingView buildingView = buildingGhost.getView().copy();
        building.addView(buildingView);
        building.setVisibility(Visibility.VISIBLE);

        fieldCore.removeBuildingGhost();
        fieldCore.addBuilding(building);
        fieldCore.setBuildingForArea(building);
    }


   /* private static void setBuildingMod(AbstractBuilding building) {
        mod = Mod.BUILDING_MOD;
        setChosenGhost(building);
        buildingGhost.draw();
        enteredCell = null;
        setChosenGhost(building);
        //возвращаем фокус на игровое поле
        focusOnField();
        chosenField.makeBuildingsClickable(false);
    }*/

    public static void removeBuilding() {

    }
}