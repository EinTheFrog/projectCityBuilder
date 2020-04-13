package controller;

import core.CellCore;
import core.FieldCore;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import logic.KeyboardButtons;
import output.BuildingOutput;
import render.Menu;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    private static Map<KeyboardButtons, Boolean> curBtnPressed = new HashMap<>();
    private static Map <KeyboardButtons, Boolean> newBtnPressed = new HashMap<>();
    private static double dx = 0.0;
    private static double dy = 0.0;
    private static Timer timer = new Timer(true);
    public static boolean inMenu = false;

    //запрещаем создавать объекты класса Controller
    private Controller() { }

    //методы для field
    public static void keyPressed(KeyCode code, FieldCore fieldCore) {
        boolean playerMovesCam = false;
        switch (code) {
            case W:
                dy = fieldCore.getMoveRange();
                newBtnPressed.put(KeyboardButtons.W, true);
                playerMovesCam = true;
                break;
            case A:
                dx = fieldCore.getMoveRange();
                newBtnPressed.put(KeyboardButtons.A, true);
                playerMovesCam = true;
                break;
            case D:
                dx = -fieldCore.getMoveRange();
                newBtnPressed.put(KeyboardButtons.D, true);
                playerMovesCam = true;
                break;
            case S:
                dy = -fieldCore.getMoveRange();
                newBtnPressed.put(KeyboardButtons.S, true);
                playerMovesCam = true;
                break;
            case ESCAPE:
                Menu.open();
                inMenu = true;
                break;
        }
        if (playerMovesCam)startCameraMovement(fieldCore);
    }

    public static void keyReleased(KeyCode code, FieldCore fieldCore) {
        switch (code) {
            case W:
                dy = 0;
                newBtnPressed.remove(KeyboardButtons.W);
                break;
            case A:
                dx = 0;
                newBtnPressed.remove(KeyboardButtons.A);
                break;
            case D:
                dx = 0;
                newBtnPressed.remove(KeyboardButtons.D);
                break;
            case S:
                dy = 0;
                newBtnPressed.remove(KeyboardButtons.S);
                break;
        }
        stopCameraMovement();
    }

    public static void zoom (double deltaY, FieldCore fieldCore) {
        fieldCore.zoom(deltaY);
    }


    //методы для запуска и остановки таймера
    private static void startCameraMovement(FieldCore fieldCore) {
        if (curBtnPressed.isEmpty() && !newBtnPressed.isEmpty()) {
            timer = new Timer(true);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    fieldCore.move(dx, dy);
                }
            };
            timer.schedule(timerTask, 0, 20);
        }
        curBtnPressed.putAll(newBtnPressed);
    }

    private static void stopCameraMovement() {
        if (newBtnPressed.isEmpty()) {
            timer.cancel();
        }
        curBtnPressed.clear();
        curBtnPressed.putAll(newBtnPressed);
    }

    //методы для cell
    public static void buildBuilding (CellCore cellCore) {
        cellCore.buildBuilding();
    }

    //методы для Building
    public static void clickOnBuilding (BuildingOutput buildingOutput, MouseEvent event) {
        double x = buildingOutput.getX();
        double y = buildingOutput.getY();
        CellCore targetCell = buildingOutput.getParentField().getCore().findCell(
                x + event.getX(), y + event.getY() - buildingOutput.getHeight()); //вычисляем координату события
        if (targetCell != null) targetCell.buildBuilding();
    }

    //для mainPane
    public static void closeMenu (Event event) {
        if (inMenu) {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) Menu.close();
            event.consume();
        }
    }
}
