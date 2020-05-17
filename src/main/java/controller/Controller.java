package controller;

import core.buildings.AbstractBuilding;
import core.CellCore;
import core.FieldCore;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import render.EnemyMenu;
import render.GameApp;
import render.Menu;

import java.util.*;

public class Controller {
    public static final double moveSpeedDenom = 8.0; //постоянная, отвечающая за скорость перемещения камеры (делитель)

    //параметры для работы с мышью
    private static double cursorX = 0.0;
    private static double cursorY = 0.0;
    public static final double BASE_SCROLL = 100;


    private static TimerTask timerMoveTask;
    private static TimerTask timerGainTask;
    private static TimerTask timerTimeTask;
    private static TimerTask timerEnemyTask;



    private static CellCore enteredCell;
    private static AbstractBuilding buildingGhost;
    private static AbstractBuilding chosenBuilding;
    private static FieldCore chosenField;

    //запрещаем создавать объекты класса Controller
    private Controller() { }

    //методы для запуска и остановки таймера




    //появление кочевников
   /* private static void showEnemy() {
        stopTimer();
        setChosenBuilding(null);
        EnemyMenu.open();
        mod = Mod.ENEMY_MOD;
    }*/

    //методы для field
    //обработка нажатия клавиш для перемещеная камеры



    //событие которое обрабатывает движение курсора, когда мышка не двигается (при движении камеры или при приближении)
/*    public static void moveCursor (double x, double y) {
        setCursorCoords(x, y);
        if (mod != Mod.BUILDING_MOD) return;
        CellCore targetCell = chosenField.getOutput().findCell(cursorX, cursorY);
        //если клетка с такими координатами существует пытаемся построить на ней здание
        if (targetCell != null) {
            showBuildingGhost(targetCell);
        } else cursorLeftField();
    }
    public static void setCursorCoords(double x, double y) {
        cursorX = x;
        cursorY = y;
    }*/


    //методы для Building
    //метод для выбора здания
/*    public static void setChosenBuilding (AbstractBuilding building) {
        setChoosingMod();
        if (chosenBuilding != null) {
            chosenBuilding.highlight(false);
            chosenBuilding.highlightAura(false);
            GameApp.getController().hideInfo();
        }
        if (building != null) {
            GameApp.getController().setInfo(building.getGoldCost(), building.getGoldProfit(),
                    building.getForceProfit(), building.getPeopleChange(), building.getPicPath());
            chosenBuilding = building;
            chosenBuilding.highlight(true);
            chosenBuilding.highlightAura(true);
        }
        chosenBuilding = building;
    }*/

    //для взаимодесйтвия с меню
    //открытие меню

    //событие, закрывающее меню, когда игрок щелкает мимо него
/*    public static void closeMenuOnClick (Event event) {
        if (mod == Mod.MENU_MOD) {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED || event.getEventType() == MouseEvent.MOUSE_DRAGGED){
                startTimer();
                Menu.close();
            }
            event.consume();
        }
        if (mod == Mod.ENEMY_MOD) event.consume();
    }*/

    //для toolsPane
/*    public static void pressOnBuildingButton(FieldCore fieldCore, AbstractBuilding building) {
        chooseField(fieldCore);
        setBuildingMod(building);
    }

    //для buildingPane
    public static void destroyBuilding (AbstractBuilding building) {
        building.delete();
        setChosenBuilding(null);
    }

    //ставим фокус на поле, на котором сейчас находится пользователь
    public static void chooseField (FieldCore fieldCore) {
        chosenField = fieldCore;
        focusOnField();
    }
    public static void focusOnField() { chosenField.getOutput().requestFocus();}*/


    //методы для изменения режима взаимодействия с пользователем
/*    public static void setChoosingMod() {
        GameApp.getController().hideInfo();
        mod = Mod.CHOOSING_MOD;
        if (buildingGhost != null) buildingGhost.delete();
        buildingGhost = null;
        //GameApplication.hideBuildingInfo();
        //возвращаем фокус на игровое поле
        focusOnField();
        chosenField.makeBuildingsClickable(true);
    }*/


/*
    public static void setMenuMod() { mod = Mod.MENU_MOD; }

    public static void setEnemyMod() { mod = Mod.ENEMY_MOD;}*/

    //getters
    public static FieldCore getChosenField() { return chosenField; }
    public static AbstractBuilding getChosenBuilding() { return chosenBuilding; }
}
