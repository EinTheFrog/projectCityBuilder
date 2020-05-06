package controller;

import core.AbstractBuilding;
import core.CellCore;
import core.FieldCore;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import render.EnemyMenu;
import render.GameApplication;
import render.Menu;

import java.util.*;

public class Controller {
    private enum Mod {
        BUILDING_MOD, CHOOSING_MOD, MENU_MOD, ENEMY_MOD
    }

    private enum KeyboardButtons {
        W, A, S, D;
    }

    private static Map<KeyboardButtons, Integer> curBtnPressed = new HashMap<>();
    private static Map <KeyboardButtons, Integer> newBtnPressed = new HashMap<>();

    private static double dx = 0.0;
    private static double dy = 0.0;
    public static final double moveSpeedDenom = 8.0; //постоянная, отвечающая за скорость перемещения камеры (делитель)

    //параметры для работы с мышью
    private static double cursorX = 0.0;
    private static double cursorY = 0.0;
    public static final double BASE_SCROLL = 100;

    private static Timer timer = new Timer(true);
    private static TimerTask timerMoveTask;
    private static TimerTask timerGainTask;
    private static TimerTask timerTimeTask;
    private static TimerTask timerEnemyTask;
    private static int timeBeforeGain = 0;
    private static int timeBeforeEnemy = 0;
    private static double time = 0;

    public static Mod mod = Mod.CHOOSING_MOD;
    private static CellCore enteredCell;
    private static AbstractBuilding buildingGhost;
    private static AbstractBuilding chosenBuilding;
    private static FieldCore chosenField;

    //запрещаем создавать объекты класса Controller
    private Controller() { }

    //методы для запуска и остановки таймера
    public static void startTimer() {
        timerMoveTask = new TimerTask() {
            @Override
            public void run() {
                //для передвижения
                dy = (curBtnPressed.getOrDefault(KeyboardButtons.W, 0)
                        + curBtnPressed.getOrDefault(KeyboardButtons.S, 0)) * chosenField.getMoveRange();
                dx = (curBtnPressed.getOrDefault(KeyboardButtons.A, 0)
                        + curBtnPressed.getOrDefault(KeyboardButtons.D, 0)) * chosenField.getMoveRange();
                chosenField.move(dx, dy);
                moveCursor(cursorX - dx * chosenField.getScale(), cursorY - dy * chosenField.getScale());
            }
        };
        timerTimeTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    time += 0.5;
                    GameApplication.updateTime((int) time);
                });
            }
        };
        timerGainTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    timeBeforeGain -= 500;
                    if (timeBeforeGain < 0) timeBeforeGain = 2_000;
                    if (timeBeforeGain == 0) {
                        timeBeforeGain = 2_000;
                        chosenField.gainResources();
                    }
                });
            }
        };
        timerEnemyTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    timeBeforeEnemy -= 500;
                    if (timeBeforeEnemy < 0) timeBeforeEnemy = 20_000;
                    if (timeBeforeEnemy == 0) {
                        timeBeforeEnemy = 20_000;
                        showEnemy();
                    }
                });
            }
        };
        timer.schedule(timerMoveTask, 0, 20);
        timer.schedule(timerTimeTask, 500, 500);
        timer.schedule(timerGainTask, 500, 500);
        timer.schedule(timerEnemyTask, 500, 500);
    }

    public static void stopTimer() {
        timerTimeTask.cancel();
        timerMoveTask.cancel();
        timerGainTask.cancel();
        timerEnemyTask.cancel();
    }

    //появление кочевников
    private static void showEnemy () {
        GameApplication.pause();
        EnemyMenu.open();
        EnemyMenu.move(GameApplication.getX(), GameApplication.getY());
        mod = Mod.ENEMY_MOD;
    }

    //методы для field
    //обработка нажатия клавиш для перемещеная камеры
    public static void keyPressed(KeyCode code) {
        boolean playerMovesCam = false;
        switch (code) {
            case W:
                //добавляем в мапу значение true для W, теперь мы знаем, что клавиша W уже нажата (1 будет споильзована
                // в дальнейшем для вычисления изменения положения камеры)
                newBtnPressed.put(KeyboardButtons.W, 1);
                //говорим, что игрок двигает камеру
                playerMovesCam = true;
                break;
            case A:
                newBtnPressed.put(KeyboardButtons.A, 1);
                playerMovesCam = true;
                break;
            case D:
                newBtnPressed.put(KeyboardButtons.D, -1);
                playerMovesCam = true;
                break;
            case S:
                newBtnPressed.put(KeyboardButtons.S, -1);
                playerMovesCam = true;
                break;
            case ESCAPE:
                if (mod == Mod.CHOOSING_MOD) {
                    if (chosenBuilding != null) {
                        setChosenBuilding(null);
                    } else {
                        openMenu();
                        moveMenu(GameApplication.getX(), GameApplication.getY());
                    }
                } else {
                    if (mod == Mod.MENU_MOD) {
                        GameApplication.resume();
                        Menu.close();
                    } else {
                        setChoosingMod();
                    }
                }
                break;
        }
        //если игрок двигает камеру, то вызываем метод для перемещения камеры
        if (playerMovesCam)startCameraMovement();
    }

    public static void keyReleased(KeyCode code, FieldCore fieldCore) {
        switch (code) {
            case W:
                //обнуляем расстояние, нак оторое камера должна пермещаться по оси OY
                dy -= fieldCore.getMoveRange();
                //удаляем кнопку из списка зажатых сейчас
                newBtnPressed.remove(KeyboardButtons.W);
                break;
            case A:
                dx -= fieldCore.getMoveRange();
                newBtnPressed.remove(KeyboardButtons.A);
                break;
            case D:
                dx += fieldCore.getMoveRange();
                newBtnPressed.remove(KeyboardButtons.D);
                break;
            case S:
                dy += fieldCore.getMoveRange();
                newBtnPressed.remove(KeyboardButtons.S);
                break;
        }
        stopCameraMovement();
    }

    //обработчик приближения на колесико мыши
    public static void zoom (double deltaY, FieldCore fieldCore) {
        fieldCore.zoom(deltaY);
        moveCursor(cursorX  - (fieldCore.getX() * (fieldCore.getScale() - 1)), cursorY - (fieldCore.getY() * (fieldCore.getScale() - 1)));
    }

    //событие которое обрабатывает движение курсора, когда мышка не двигается (при движении камеры или при приближении)
    public static void moveCursor (double x, double y) {
        setCursorCoords(x, y);
        if (mod != Mod.BUILDING_MOD) return;
        CellCore targetCell = chosenField.findCell(cursorX, cursorY);
        //если клетка с такими координатами существует пытаемся построить на ней здание
        if (targetCell != null) {
            showBuilding(targetCell);
        } else cursorLeftField();
    }
    public static void setCursorCoords(double x, double y) {
        cursorX = x;
        cursorY = y;
    }

    //методы для запуска и остановки таймера во время движения
    private static void startCameraMovement() {
        curBtnPressed.putAll(newBtnPressed);
    }

    private static void stopCameraMovement() {
        curBtnPressed.clear();
        curBtnPressed.putAll(newBtnPressed);
    }


    //методы для cell
    //метод для добавления здания
    public static void buildBuilding ()  {
        if (mod != Mod.BUILDING_MOD || buildingGhost.getGoldCost() > chosenField.getGold() ||
                chosenField.getPeople() + buildingGhost.getPeopleChange() < 0) return;
        //проверяем, что площадь для постройки свободна
        int numOfNeighbours = chosenField.getCellsUnderBuilding(enteredCell, buildingGhost).size();
        if (enteredCell.neighboursFree(buildingGhost) && numOfNeighbours == buildingGhost.getCellArea()) {
            //перемещаем здание в выбранную клетку
            buildingGhost.move(enteredCell.getX(), enteredCell.getY());
            buildingGhost.setOpacity(1);
            AbstractBuilding newBuilding = buildingGhost.copy();
            newBuilding.draw();
            newBuilding.setCellArea(chosenField.getCellsUnderBuilding(enteredCell, newBuilding));
            newBuilding.setCellsInAura(buildingGhost.getCellsInAura());
            buildingGhost.delete();
            newBuilding.setClickable(true);
            //добавляем здание в лист зданий поля и перерисоваем все здания перед ним
            chosenField.addBuilding(newBuilding);
            //если здание занимает больше 1 клетки говорим соседним клеткам, что на них теперь тоже находится здание
            enteredCell.setBuildingForArea(newBuilding);

            chosenField.buyBuilding(newBuilding);
            setChoosingMod();
            GameApplication.hideBuildingInfo();
        }
    }

    //метод для создания призрака здания на клетке
    public static void showBuilding (CellCore cellCore) {
        if (mod == Mod.BUILDING_MOD && enteredCell != cellCore) {
            if (enteredCell == null) {
                buildingGhost.setOpacity(0.5);
                buildingGhost.setCellsInAura(chosenField.getCellsInAura(cellCore, buildingGhost));
                for (CellCore cell : buildingGhost.getCellsInAura()) {
                    cell.addAuraColor(buildingGhost.getOwnAura().getColor());
                }
            } else {
                for (CellCore cell : buildingGhost.getCellsInAura()) {
                    cell.removeAuraColor();
                }
                buildingGhost.setCellsInAura(chosenField.getCellsInAura(cellCore, buildingGhost));
                for (CellCore cell : buildingGhost.getCellsInAura()) {
                    cell.addAuraColor(buildingGhost.getOwnAura().getColor());
                }
            }
        }
        buildingGhost.move(cellCore.getX(), cellCore.getY());
        enteredCell = cellCore;
    }

    //метод для удаления призрака здания c клетки
    private static void cursorLeftField () {
        if (buildingGhost != null) {
            buildingGhost.setOpacity(0);
            if (buildingGhost.getCellsInAura() != null) {
                for (CellCore cell: buildingGhost.getCellsInAura()) {
                    cell.removeAuraColor();
                }
                buildingGhost.getCellsInAura().clear();
            }
            enteredCell = null;
        }
    }

    //методы для Building
    //метод для выбора здания
    public static void clickOnBuilding (AbstractBuilding building) {
        if (chosenBuilding != null) chosenBuilding.highlight(false);
        GameApplication.showBuildingInfo(building.getName(), building.getGoldCost(), building.getPeopleChange());
        chosenBuilding = building;
        chosenBuilding.highlight(true);
        chosenBuilding.highlightAura(true);
    }

    //для mainPane
    public static void openMenu() {
        Menu.open();
    }

    //событие, закрывающее меню, когда игрок щелкает мимо него
    public static void closeMenuOnClick (Event event) {
        if (mod == Mod.MENU_MOD) {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED || event.getEventType() == MouseEvent.MOUSE_DRAGGED){
                GameApplication.resume();
                Menu.close();
            }
            event.consume();
        }
        if (mod == Mod.ENEMY_MOD) event.consume();
    }

    public static void moveMenu (double x, double y) {
        if (mod == Mod.MENU_MOD) {
            Menu.move(x, y);
        }
    }

    //для toolsPane
    public static void pressOnBuildingButton(FieldCore fieldCore, AbstractBuilding building) {
        chooseField(fieldCore);
        setBuildingMod(building);
    }

    public static void pressOnChooseButton(FieldCore fieldCore) {
        chooseField(fieldCore);
        setChoosingMod();
    }

    // ставим фокус на поле, на котором сейчас находится пользователь
    public static void chooseField (FieldCore fieldCore) {
        chosenField = fieldCore;
        chosenField.getOutput().requestFocus();
    }


    private static void setChosenBuilding(AbstractBuilding newBuilding) {
        if (chosenBuilding != null) {
            chosenBuilding.highlight(false);
            chosenBuilding.highlightAura(false);
        }
        if (buildingGhost != null) {
            buildingGhost.delete();
        }
        buildingGhost = newBuilding;
    }

    //методы для изменения режима взаимодействия с пользователем
    public static void setChoosingMod() {
        mod = Mod.CHOOSING_MOD;
        //возвращаем фокус на игровое поле
        focusOnField();
        for (AbstractBuilding b: chosenField.getBuildingsList()) {
            b.setOpacity(1);
            b.setClickable(true);
        }
        if (buildingGhost != null) buildingGhost.delete();
        buildingGhost = null;
    }

    private static void setBuildingMod(AbstractBuilding building) {
        building.draw();
        GameApplication.showBuildingInfo (building.getName(), building.getGoldCost(), building.getPeopleChange());
        mod = Mod.BUILDING_MOD;
        enteredCell = null;
        setChosenBuilding(building);
        //возвращаем фокус на игровое поле
       focusOnField();
        for (AbstractBuilding b: chosenField.getBuildingsList()) {
            b.setOpacity(0.5);
            b.setClickable(false);
        }
    }

    public static void setMenuMod() {
        mod = Mod.MENU_MOD;
    }

    public static void setEnemyMod() {
        mod = Mod.ENEMY_MOD;
    }

    //для buildingPane
    public static void destroyBuilding (AbstractBuilding building) {
        chosenField.removeBuilding(building);
        for (CellCore c:building.getCells()) {
            c.removeBuilding();
        }
        building.delete();

    }

    public static void focusOnField() { chosenField.getOutput().requestFocus();}

    public static FieldCore getChosenField() { return chosenField; }
    public static AbstractBuilding getChosenBuilding() { return chosenBuilding; }
}
