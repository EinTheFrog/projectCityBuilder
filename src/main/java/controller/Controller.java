package controller;

import core.AbstractBuilding;
import core.CellCore;
import core.FieldCore;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import logic.KeyboardButtons;
import logic.Mod;
import render.Menu;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    private static Map<KeyboardButtons, Integer> curBtnPressed = new HashMap<>();
    private static Map <KeyboardButtons, Integer> newBtnPressed = new HashMap<>();
    private static double dx = 0.0;
    private static double dy = 0.0;
    private static Timer timer = new Timer(true);
    public static Mod mod = Mod.CHOOSING_MOD;
    private static CellCore enteredCell;
    private static AbstractBuilding chosenBuilding;
    private static FieldCore chosenField;

    //запрещаем создавать объекты класса Controller
    private Controller() { }

    //методы для field
    //обработка нажатия клавиш для перемещеная камеры
    public static void keyPressed(KeyCode code, FieldCore fieldCore) {
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
                    Menu.open();
                    mod = Mod.MENU_MOD;
                }
                else setChoosingMod();
                break;
        }
        //если игрок двигает камеру, то вызываем метод для перемещения камеры
        if (playerMovesCam)startCameraMovement(fieldCore);
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
    }


    //методы для запуска и остановки таймера во время движения
    private static void startCameraMovement(FieldCore fieldCore) {
        if (curBtnPressed.isEmpty() && !newBtnPressed.isEmpty()) {
            timer = new Timer(true);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    dy = (curBtnPressed.getOrDefault(KeyboardButtons.W, 0)
                            + curBtnPressed.getOrDefault(KeyboardButtons.S, 0)) * fieldCore.getMoveRange();
                    dx = (curBtnPressed.getOrDefault(KeyboardButtons.A, 0)
                            + curBtnPressed.getOrDefault(KeyboardButtons.D, 0)) * fieldCore.getMoveRange();
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
    //метод для добавления здания
    public static void buildBuilding (CellCore cellCore)  {
        //System.out.println("BUILD");
        if (mod != Mod.BUILDING_MOD) return;
        //проверяем, что клетка свободна
        int numOfNeighbours = cellCore.getField().getNeighbours(cellCore, cellCore.getBuildingGhost()).size();
        if (cellCore.neighboursFree(cellCore.getBuildingGhost()) && numOfNeighbours == cellCore.getBuildingGhost().getCellArea()) {
            //перемещаем здание в выбранную клетку
            chosenBuilding.move(cellCore.getX(), cellCore.getY());
            chosenBuilding.setOpacity(1);
            AbstractBuilding newBuilding = chosenBuilding.copy();
            cellCore.getField().addBuilding(newBuilding);
            //перерисовываем здания, находящиеся по перспективе ближе к игроку поверх нового,
            // чтобы новое здание их не перекрывало
            cellCore.getField().redrawCloserBuildings(cellCore.getIndices());
            //если здание занимает больше 1 клетки говорим соседним клеткам, что на них теперь тоже находится здание
            cellCore.setBuildingForArea(newBuilding);

            setChoosingMod();
        }
    }

    //метод для создания призрака здания на клетке
    public static void showBuilding (CellCore cellCore) {
        if (mod == Mod.BUILDING_MOD && enteredCell != cellCore) {
            if (enteredCell == null)  chosenBuilding.setOpacity(0.5);
            else enteredCell.removeGhostForArea();
            chosenBuilding.move(cellCore.getX(), cellCore.getY());
            enteredCell = cellCore;
            enteredCell.setBuildingGhostForArea(chosenBuilding);
        }
    }

    //метод для удаления призрака здания c клетки
    public static void cursorLeftField () {
        if (chosenBuilding != null) {
            chosenBuilding.setOpacity(0);
            enteredCell = null;
        }
    }

    //методы для Building
    //строим здание на клетке,которую закрывает здание
    public static void clickOnBuilding (AbstractBuilding building, MouseEvent event) {
        //находим клетку по координатам щелчка мыши
        double x = building.getX();
        double y = building.getY();
        CellCore targetCell = building.getParentField().findCell(
                x + event.getX(), y + event.getY());
        //если клетка с такими координатами существует пытаемся построить на ней здание
        if (targetCell != null) buildBuilding(targetCell);
    }
    //показываем призрак здания на клетке,которую закрывает уже построенное здание
    public static void cursorOnBuildingInBuildingMod (AbstractBuilding building, MouseEvent event) {
        if (mod != Mod.BUILDING_MOD) return;
        //находим клетку по координатам курсора
        double x = building.getX();
        double y = building.getY();
        CellCore targetCell = building.getParentField().findCell(
                x + event.getX(), y + event.getY());
        //если клетка с такими координатами существует пытаемся построить на ней здание
        if (targetCell != null) showBuilding(targetCell);
    }

    //для mainPane
    //событие, закрывающее меню, когда игрок щелкает мимо него
    public static void closeMenu (Event event) {
        if (mod == Mod.MENU_MOD) {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) Menu.close();
            event.consume();
        }
    }

    //для кнопок на toolsPane
    public static void pressOnBuildingButton(FieldCore fieldCore, AbstractBuilding building) {
        chosenField = fieldCore;
        setBuildingMod(building);
    }

    public static void pressOnChooseButton(FieldCore fieldCore) {
        chosenField = fieldCore;
        setChoosingMod();
    }

    public static void chooseBuilding(AbstractBuilding newBuilding) {
        if (chosenBuilding != null) chosenBuilding.delete();
        chosenBuilding = newBuilding;
    }

    private static void setChoosingMod() {
        mod = Mod.CHOOSING_MOD;
        for (AbstractBuilding b: chosenField.getBuildingsList()) {
            b.setOpacity(1);
        }
        chosenBuilding.delete();
        chosenBuilding = null;
    }

    private static void setBuildingMod(AbstractBuilding building) {
        mod = Mod.BUILDING_MOD;
        chooseBuilding(building);
        //устанавливаем фокус на этом игровом поле
        chosenField.getOutput().requestFocus();
        for (AbstractBuilding b: chosenField.getBuildingsList()) {
            b.setOpacity(0.5);
        }
    }
}
