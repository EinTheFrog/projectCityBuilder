package view;

import controller.GameAppController;
import controller.Mod;
import core.Aura;
import core.FieldCore;
import core.buildings.AbstractBuilding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import view.buildings.AbstractBuildingView;

import java.util.*;
import java.util.stream.Collectors;

public class FieldView extends Pane {
    private static final double MOVE_SPEED_DENOM = 8; //
    private static final double BASE_SCROLL = 200;

    private FieldCore fieldCore;

    private double fieldX;
    private double fieldY;
    //переменные, отвечающие за то, насколько сдвинута наша панель, не зависят от scale
    private double fieldMoveX;
    private double fieldMoveY;

    private double width;
    private double height;
    private double side;
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
    private double cellWidth;
    private double cellHeight;

    private AbstractBuildingView buildingGhost;
    private  AbstractBuildingView chosenBuilding;
    private List<AbstractBuildingView> buildingsList;

    private GameAppController controller;

    //для каждого поля у нас своё положение камеры, а значит у каждого поля должны быть свои параметры scale
    // и скорости перемщения камеры
    private double moveRange;
    private final double START_MOVE_RANGE;

    /**
     *
     * @param size
     * @param indent
     * @param side
     * @param paneWidth - параметр, необходимый, для работы приближения камеры (вычисления нового положения
     *                 поля расчитываются относительно центра панели, на которй он находится)
     * @param paneHeight
     * @param moveRange
     */
    public FieldView (FieldCore fieldCore, GameAppController controller, int size, double indent, double side,
                      ReadOnlyDoubleProperty paneWidth, ReadOnlyDoubleProperty paneHeight, double moveRange) {
        //позволяем фокусироваться на игровом поле
        setFocusTraversable(true);
        requestFocus();

        this.fieldCore = fieldCore;
        SIZE = size;
        this.side = side;
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
    }

    //метод для приближения камеры
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

    //метод для симуляции приближения камеры к игрвому полю
    private void setScale (double scaleValue) {
        scale.setX(scaleValue);
        scale.setY(scaleValue);
    }

    //метод для перемщения камеры (самого поля относительно камеры)
    public void move(double dx, double dy) {
        fieldMoveX += dx * moveRange;
        fieldMoveY += dy * moveRange;
        //вычитываем координаты поля для его отрисовки в зависимости от scale
        fieldX = (fieldMoveX - paneWidth.getValue() / 2) * scaleValue + paneWidth.getValue() / 2;
        fieldY = (fieldMoveY - paneHeight.getValue() / 2) * scaleValue + paneHeight.getValue() / 2;
        this.relocate(fieldX, fieldY);
    }

    public void addCell(CellView cellView) {
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
        this.getChildren().add(cellView);

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
                if (controller.getMod() == Mod.BUILDING_MOD) {
                    buyBuilding();
                    controller.setChoosingMod();
                }
                if (controller.getMod() == Mod.CHOOSING_MOD) setChosenBuilding(null);
                cellView.isClicked.setValue(false);
            }
        });
    }

    public void setChosenBuilding (AbstractBuildingView building) {
        if (chosenBuilding != null) {
            controller.hideInfo();
            chosenBuilding.highlight(false);
            highlightAura(chosenBuilding, false);
        }
        if (building != null) {
            fieldCore.setChosenBuilding(building.getCore());
            controller.setInfo(building);
            controller.showInfo();
            chosenBuilding = building;
            chosenBuilding.highlight(true);
            highlightAura(chosenBuilding, true);
        } else {
            fieldCore.setChosenBuilding(null);
        }
        chosenBuilding = building;
    }

    public void makeBuildingsClickable (boolean bool) {
        for (AbstractBuildingView building: buildingsList) {
            if (bool) {
                building.setVisibility(Visibility.VISIBLE);
            }
            else building.setVisibility(Visibility.GHOST);
            building.setClickable(bool);
        }
    }

    public void setBuildingMod(boolean bool) {
        if (!bool) {
            if (buildingGhost != null) removeBuildingGhost();
            makeBuildingsClickable(true);
        } else {
            makeBuildingsClickable(false);
        }
    }

    public void removeBuildingGhost() {
        fieldCore.removeBuildingGhost();
        highlightAura(buildingGhost, false);
        getChildren().remove(buildingGhost);
        buildingGhost = null;
    }

    public void removeRandomBuilding() {
        Random rnd = new Random();
        int k = rnd.nextInt(buildingsList.size());
        removeBuilding(buildingsList.get(k));
    }

    public void buyBuilding() {
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
            this.getChildren().add(buildingView);
            buildingsList.sort(Comparator.comparingInt(this::getVerticalShift));
            int k = buildingsList.indexOf(buildingView);
            for (int i = k; i < buildingsList.size(); i++) {
                moveBuildingToFront(buildingsList.get(i));
            }

            controller.updateResources();
            controller.updateIncome();
        }
    }

    //вспомогательный метод для определения расположения здания (на переднем или заднем плане находится)
    private int getVerticalShift(AbstractBuildingView building) {
        int x = building.getCore().getX();
        int y = building.getCore().getY();
        return y - x;
    }

    public void addGhost(AbstractBuildingView buildingGhost) {
        this.buildingGhost = buildingGhost;
        getChildren().add(buildingGhost);
    }

    public void moveBuilding(CellView cellView, AbstractBuildingView buildingView) {
        double x = cellView.getLayoutX();
        double y = cellView.getLayoutY();
        buildingView.moveTo(x, y);
        int indX = cellView.getCore().getX();
        int indY = cellView.getCore().getY();
        buildingView.getCore().setCords(indX, indY);
    }

    /**
     * метод перемещающий здание на передний план
     * @param buildingView
     */
    public void moveBuildingToFront(AbstractBuildingView buildingView) {
        buildingView.toFront();
    }

    public void removeBuilding(AbstractBuildingView buildingView) {
        fieldCore.removeBuilding(buildingView.getCore());
        this.getChildren().remove(buildingView);
    }

    public void highlightAura(AbstractBuildingView building, boolean bool) {
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

    public double getSide() {
        return side;
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
