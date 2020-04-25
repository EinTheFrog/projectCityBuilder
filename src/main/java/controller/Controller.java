package controller;

import core.AbstractBuilding;
import core.CellCore;
import core.FieldCore;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import logic.KeyboardButtons;
import logic.Mod;
import render.GameApplication;
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
    private static double cursorX = 0.0;
    private static double cursorY = 0.0;
/*    private static double screenCenterX;
    private static double screenCenterY;*/
    public static final double baseScroll = 100;
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
                    moveCursor(cursorX - dx, cursorY - dy);
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
    public static void buildBuilding ()  {
        //System.out.println("BUILD");
        if (mod != Mod.BUILDING_MOD) return;
        //проверяем, что клетка свободна
        int numOfNeighbours = enteredCell.getField().getNeighbours(enteredCell, enteredCell.getBuildingGhost()).size();
        if (enteredCell.neighboursFree(enteredCell.getBuildingGhost()) && numOfNeighbours == enteredCell.getBuildingGhost().getCellArea()) {
            //перемещаем здание в выбранную клетку
            chosenBuilding.move(enteredCell.getX(), enteredCell.getY());
            chosenBuilding.setOpacity(1);
            AbstractBuilding newBuilding = chosenBuilding.copy();
            enteredCell.getField().addBuilding(newBuilding);
            //перерисовываем здания, находящиеся по перспективе ближе к игроку поверх нового,
            // чтобы новое здание их не перекрывало
            enteredCell.getField().redrawCloserBuildings(enteredCell.getIndices());
            //если здание занимает больше 1 клетки говорим соседним клеткам, что на них теперь тоже находится здание
            enteredCell.setBuildingForArea(newBuilding);

            setChoosingMod();
        }
    }

    //метод для создания призрака здания на клетке
    public static void showBuilding (CellCore cellCore) {
        if (mod == Mod.BUILDING_MOD && enteredCell != cellCore) {
            if (enteredCell == null) chosenBuilding.setOpacity(0.5);
            else enteredCell.removeGhostForArea();
            chosenBuilding.move(cellCore.getX(), cellCore.getY());
            enteredCell = cellCore;
            enteredCell.setBuildingGhostForArea(chosenBuilding);
        }
    }

    //метод для удаления призрака здания c клетки
    private static void cursorLeftField () {
        if (chosenBuilding != null) {
            chosenBuilding.setOpacity(0);
            enteredCell = null;
        }
    }

    //методы для Building
    //строим здание на клетке,которую закрывает здание
    public static void clickOnBuilding (AbstractBuilding building, MouseEvent event) {
        //находим клетку по координатам щелчка мыши
        CellCore targetCell = building.getParentField().findCell(
                cursorX + event.getX(), cursorY + event.getY());
        //если клетка с такими координатами существует пытаемся построить на ней здание
        if (targetCell != null) buildBuilding();
    }
    //событие которое обрабатывает движение курсора, когда мышка не двигается (при движении камеры)
    // или когда курсор движется поверх здания
    public static void moveCursor (double x, double y) {
        setCursorCoords(x, y);
        if (mod != Mod.BUILDING_MOD) return;
        CellCore targetCell = chosenField.findCell(cursorX, cursorY);
        //если клетка с такими координатами существует пытаемся построить на ней здание
        if (targetCell != null) {
            showBuilding(targetCell);
        } else cursorLeftField();
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
        chooseField(fieldCore);
        setBuildingMod(building);
    }

    public static void pressOnChooseButton(FieldCore fieldCore) {
        chooseField(fieldCore);
        setChoosingMod();
    }

    public static void chooseField (FieldCore fieldCore)
    {
/*        screenCenterX = fieldCore.getWidth() / 2;
        screenCenterY = fieldCore.getHeight() / 2;*/
        chosenField = fieldCore;
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

    public static void setCenterCoords(double x, double y) {
/*        screenCenterX = x;
        screenCenterY = y;*/
    }

    public static void setCursorCoords(double x, double y) {
        cursorX = x;
        cursorY = y;
    }
}
