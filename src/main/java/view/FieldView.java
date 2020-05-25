package view;

import controller.GameAppController;
import controller.Mod;
import core.CellCore;
import core.FieldCore;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import view.buildings.AbstractBuildingView;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс, отвечающий за графическое представление игрвого поля
 * Хранит все графические представления клеток и постпроенных зданий
 * Содержит обработчики нажатия клавиш
 */
public class FieldView extends Pane {
    private static final double BASE_SCROLL = 200;

    private FieldCore fieldCore;

    private double fieldX;
    private double fieldY;
    //переменные, отвечающие за то, насколько сдвинута наша панель, не зависят от scale
    private double fieldMoveX;
    private double fieldMoveY;

    private double width;
    private double height;
    public final int SIZE;
    private ReadOnlyDoubleProperty paneWidth;
    private ReadOnlyDoubleProperty paneHeight;
    public final double START_WIDTH;
    public final double START_HEIGHT;
    private double scaleValue = 1.0;
    private final Scale scale = new Scale();

    //на поле все клетки одинаковы
    private final CellView[][] cellsArray;
    private CellView chosenCell;
    private final double cellWidth;
    private final double cellHeight;

    private AbstractBuildingView buildingGhost;
    private  AbstractBuildingView chosenBuilding;
    private List<AbstractBuildingView> buildingsList;

    private GameAppController controller;

    // для каждого поля (сейчас в правилах игры у игрока может быть всего одно поле) у нас своё положение камеры,
    // а значит у каждого поля должны быть свои параметры scale и скорости перемещения камеры
    private double moveRange;
    private final double START_MOVE_RANGE;

    /**
     * @param size - кол-во клеток для одной стороны поля
     * @param indent - начальный отступ поля от границы экрана
     * @param side - длина стороны графического представления игрвого поля
     * @param paneWidth - ширина родительского layout-а, необходима для работы приближения камеры
     *                 (вычисления нового положения поля расчитываются относительно центра панели, на которй он находится)
     * @param paneHeight - высота родительского layout-а, необходима для работы приближения камеры
     *                  (вычисления нового положения поля расчитываются относительно центра панели, на которй он находится)
     * @param moveRange - скорость, с которой мы перемещаем поле при симуляции движения камеры
     */
    public FieldView (FieldCore fieldCore, GameAppController controller, int size, double indent, double side,
                      ReadOnlyDoubleProperty paneWidth, ReadOnlyDoubleProperty paneHeight, double moveRange) {
        //фокусируемся на графическом представлении игрвого поля
        setFocusTraversable(true);
        requestFocus();

        this.fieldCore = fieldCore;
        SIZE = size;
        width = 2 * side * Math.cos(Math.PI / 6);
        height = 2 * side * Math.sin(Math.PI / 6);
        START_WIDTH = width;
        START_HEIGHT = height;
        this.paneWidth = paneWidth;
        this.paneHeight = paneHeight;

        cellsArray = new CellView[size][size];
        cellWidth = width / SIZE;
        cellHeight = height / SIZE;

        buildingsList = new ArrayList<>();

        this.controller = controller;

        this.setPrefSize(width, height);

        //задаем параметры для перемещения поля
        this.moveRange = moveRange;
        START_MOVE_RANGE = moveRange;
        fieldMoveX = indent;
        fieldMoveY = indent;
        //вычитываем координаты поля для его отрисовки
        fieldX = (fieldMoveX - paneWidth.getValue() / 2) * scaleValue + paneWidth.getValue() / 2;
        fieldY = (fieldMoveY - paneHeight.getValue() / 2) * scaleValue + paneHeight.getValue() / 2;
        //задаем нужные параметры для преобразования scale и применяем его
        scale.setPivotX(0);
        scale.setPivotY(0);
        scale.setX(1);
        scale.setY(1);
        move(0,0);
        this.getTransforms().add(scale);

        addEventHandler(KeyEvent.KEY_PRESSED, event -> controller.keyPressed(event.getCode()));
        addEventHandler(KeyEvent.KEY_RELEASED, event -> controller.keyReleased(event.getCode()));

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                CellCore cellCore = new CellCore(j, i);
                CellView cellView = new CellView(getCellWidth(), getCellHeight(), cellCore);
                addCell(cellView);
            }
        }
    }

    /**
     * Метод для симуляции приближения камеры к игрвому полю
     */
    public void zoom (double scrollValue) {
        //увеличивая значение scale создаем эфект приближения камеры
        if (scaleValue + scrollValue / BASE_SCROLL > 0.1 && scaleValue + scrollValue / BASE_SCROLL < 10)
            scaleValue += scrollValue / BASE_SCROLL;
        setScale(scaleValue);
        //System.out.println(scaleValue);
        //вычисляем новую ширину и высоту
        width = START_WIDTH * scaleValue;
        height= START_HEIGHT * scaleValue;
        //перемещаем поле таким образом, чтобы поле не перемещалось относительно камеры при масштабировании
        move(0, 0);
        //изменяем скорость перемщения камеры, чтобы при сильном приближении камера не двигалась слишком быстро
        moveRange = START_MOVE_RANGE / scaleValue;
    }

    /**
     * Вспомогательный метод для zoom()
     * @param scaleValue
     */
    private void setScale (double scaleValue) {
        scale.setX(scaleValue);
        scale.setY(scaleValue);
    }

    /**
     * Метод для перемщения камеры (самого поля относительно камеры). В методе производятся вычисления, позовляющие
     * корректно перемещать здание при любом значении scale
     */
    public void move(double dx, double dy) {
        fieldMoveX += dx * moveRange;
        fieldMoveY += dy * moveRange;
        //вычитываем координаты поля для его отрисовки в зависимости от scale
        fieldX = (fieldMoveX - paneWidth.getValue() / 2) * scaleValue + paneWidth.getValue() / 2;
        fieldY = (fieldMoveY - paneHeight.getValue() / 2) * scaleValue + paneHeight.getValue() / 2;
        relocate(fieldX, fieldY);
    }

    /**
     * Метод добавляющий клетку. При добавлении на параметры isClicked и is Chosen клетки устанавливаются слушаетели,
     * чтобы поле могло реагировать, когда наводит курсор или клмикает по како-либо клетке. С помощью такой реализации
     * поле знает о клетках, которые находятся на нем, однако клетки не знают о поле, на котором находятся
     * @param cellView
     */
    private void addCell(CellView cellView) {
        int indX = cellView.getCore().getX();
        int indY = cellView.getCore().getY();

        cellsArray[indX][indY] = cellView;
        fieldCore.addCell(cellView.getCore());
        //координаты cellView (по какой-то причине) считаются так, будто бы это прямоуголник,
        // после создания все становится нормально
        final double FIRST_CELL_X = cellWidth / 2 * indY;
        final double FIRST_CELL_Y = height / 2 + cellHeight / 2 * indY - cellHeight / 2;
        double x = indX * cellWidth / 2 + FIRST_CELL_X;
        double y = FIRST_CELL_Y - indX * cellHeight / 2;
        cellView.relocate(x, y);
        getChildren().add(cellView);

        cellView.isChosen.addListener((obs, oldVal, newVal) -> {
            if (buildingGhost != null) {
                    if (newVal) {
                        if (chosenCell == null) {
                            buildingGhost.setVisibility(Visibility.GHOST);
                            highlightAura(buildingGhost, true);
                        }
                        chosenCell = cellView;
                    }
                    if (oldVal) chosenCell = null;
                    if (chosenCell != null) {
                        highlightAura(buildingGhost, false);
                        buildingGhost.getCore().setCords(chosenCell.getCore().getX(), chosenCell.getCore().getY());
                        highlightAura(buildingGhost, true);
                        moveBuilding(chosenCell, buildingGhost);
                    } else {
                        highlightAura(buildingGhost, false);
                        buildingGhost.setVisibility(Visibility.INVISIBLE);
                    }
            }

        });
        cellView.isClicked.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (controller.getMod() == Mod.BUILDING_MOD) buyBuilding();
                if (controller.getMod() == Mod.CHOOSING_MOD) setChosenBuilding(null);
                cellView.isClicked.setValue(false);
            }
        });
    }

    /**
     * Задает значение выбранного пользовтелем здания для поля. У выбранного здания включается подсветка (у всех
     * остальных зданий она выключается). Также поле вызывает метод GameAppController-а, который показывает пользователю
     * панель с информацией о выбранном здании
     * @param building
     */
    public void setChosenBuilding (AbstractBuildingView building) {
        if (chosenBuilding != null) {
            controller.hideInfo();
            chosenBuilding.highlight(false);
            highlightAura(chosenBuilding, false);
        }
        if (building != null) {
            controller.setInfo(building);
            controller.showInfo();
            chosenBuilding = building;
            chosenBuilding.highlight(true);
            highlightAura(chosenBuilding, true);
        }
        chosenBuilding = building;
    }

    /**
     * Включает/ выключает обработчики щелчков на зданиях поля, а также устанаваливает для них подходящую прозрачность
     * @param bool
     */
    private void makeBuildingsClickable (boolean bool) {
        for (AbstractBuildingView building: buildingsList) {
            if (bool) {
                building.setVisibility(Visibility.VISIBLE);
            }
            else building.setVisibility(Visibility.GHOST);
            building.setClickable(bool);
        }
    }

    /**
     * Включает/выключает режим постройки для поля. В режиме постройки мы не можем выбирать здания, стоящие на поле,
     * построенные здания становятся полупрозрачными, чтобы пользователю было удобнее строить здания,
     * а на выбранной курсором клетке появляется призрак здания, которое пользователь хочет  построить
     * @param bool
     */
    public void setBuildingMod(boolean bool) {
        if (!bool) {
            if (buildingGhost != null) removeGhost();
            makeBuildingsClickable(true);
        } else {
            makeBuildingsClickable(false);
        }
    }

    /**
     * Удаляет призрак здания (полупрозрачное здание, помогающие пользователю выбрать место для постройки реального здания)
     * с графического представления поля
     */
    private void removeGhost() {
        highlightAura(buildingGhost, false);
        getChildren().remove(buildingGhost);
        buildingGhost = null;
    }

    /**
     * Метод, удаляющйи случайное здание с поля (как с графической так и с логической реализации)
     * Включает проверку на пустоту списка зданий и создание случайной переменной
     */
    public void removeRandomBuilding() {
        if (buildingsList.size() == 0) return;
        int k;
        Random rnd = new Random();
        k = rnd.nextInt(buildingsList.size());
        removeBuilding(buildingsList.get(k));
    }

    /**
     * Метод для создания здания. Создает копию призрака здания (делая копию непрозрачной). Пытается добавить здания в
     * логическое прдеставление поля. Если удается это сделать - доавляет слушаетеля для параметра isChosen для новго
     * здания (это нужно, чтобы здание не хранило информации о поле, на котором расположено) и включает choosingMod
     * с помощью экземпляра GameAppController
     */
    private void buyBuilding() {
        AbstractBuildingView buildingView = buildingGhost.copy();
        boolean wasBuilt = fieldCore.buildBuilding(buildingView.getCore());
        if (wasBuilt) {
            buildingView.isChosen.addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    setChosenBuilding(buildingView);
                    buildingView.isChosen.setValue(false);
                }
            });
            buildingsList.add(buildingView);
            getChildren().add(buildingView);
            updateBuildingsDisposal(buildingView);

            controller.updateResources();
            controller.setChoosingMod();
        }
    }

    /**
     * Удаляет призрак здания (полупрозрачное здание, помогающие пользователю выбрать место для постройки реального здания)
     * с графического представления поля
     */
    public void addGhost(AbstractBuildingView buildingGhost) {
        this.buildingGhost = buildingGhost;
        getChildren().add(buildingGhost);
    }

    /**
     * Перемещает графическое представление здания на место указанной клетки.
     * Также задает логическому представлению здания координаты указанной клетки
     * @param cellView
     * @param buildingView
     */
    private void moveBuilding(CellView cellView, AbstractBuildingView buildingView) {
        double x = cellView.getLayoutX();
        double y = cellView.getLayoutY();
        buildingView.moveTo(x, y);
        int indX = cellView.getCore().getX();
        int indY = cellView.getCore().getY();
        buildingView.getCore().setCords(indX, indY);
    }

    /**
     * метод обновляющий удаление зданий от камеры таким образом, чтобы передние здания отрисовывались поверх задних
     * @param buildingView
     */
    private void updateBuildingsDisposal(AbstractBuildingView buildingView) {
        buildingsList.sort(Comparator.comparingInt(this::getVerticalShift));
        int k = buildingsList.indexOf(buildingView);
        for (int i = k; i < buildingsList.size(); i++) {
            buildingsList.get(i).toFront();
        }
    }
    /** Вспомогательный метод для определения расположения здания (на переднем или заднем плане находится)
     * @param building
     * @return
     */
    private int getVerticalShift(AbstractBuildingView building) {
        int x = building.getCore().getX();
        int y = building.getCore().getY();
        return y - x;
    }

    /**
     * Метод, удаляющий графическое представления здания с поля, из списка зданий и само здание
     * из логического представления поля
     * @param buildingView
     */
    public void removeBuilding(AbstractBuildingView buildingView) {
        fieldCore.removeBuilding(buildingView.getCore());
        getChildren().remove(buildingView);
        controller.updateResources();
        buildingsList.remove(buildingView);
    }


    /**
     * Метод включающий/выключающий подсветку для всех клеток в радиусе ауры здания
     * @param building
     * @param bool
     */
    private void highlightAura(AbstractBuildingView building, boolean bool) {
        Set<CellView> cells = fieldCore.getCellsInAura(building.getCore()).stream().map(
                e -> cellsArray[e.getX()][e.getY()]).collect(Collectors.toSet());
        for(CellView cellView: cells) {
            if (bool) cellView.setAuraColor(building.getCore().getOwnAura());
            else  cellView.clearAuraColor();
        }
    }

    public double getX() {
        return fieldX;
    }

    public double getY() {
        return fieldY;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public double getCellHeight() {
        return cellHeight;
    }

    public FieldCore getCore() {
        return fieldCore;
    }

    public AbstractBuildingView getChosenBuilding(){
        return chosenBuilding;
    }
}
