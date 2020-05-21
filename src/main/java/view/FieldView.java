package view;

import core.Aura;
import core.CellCore;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import render.GameApp;
import view.buildings.AbstractBuildingView;

import java.util.List;

public class FieldView extends Pane {
    private static final double MOVE_SPEED_DENOM = 8; //
    private static final double BASE_SCROLL = 200;

    private double fieldX;
    private double fieldY;
    //переменные, отвечающие за то, насколько сдвинута наша панель, не зависят от scale
    private double fieldMoveX;
    private double fieldMoveY;

    public DoubleProperty width;
    public DoubleProperty height;

    private double scaleValue = 1.0;
    private Scale scale = new Scale();
    //для каждого поля у нас своё положение камеры, а значит у каждого поля должны быть свои параметры scale
    // и скорости перемщения камеры
    private double moveRange = GameApp.CELL_SIDE / MOVE_SPEED_DENOM;

    public FieldView () {
        //позволяем фокусироваться на игровом поле
        setFocusTraversable(true);
        requestFocus();
        width = new SimpleDoubleProperty(2 * GameApp.FIELD_SIDE * Math.cos(Math.PI / 6));
        height = new SimpleDoubleProperty( 2 * GameApp.FIELD_SIDE * Math.sin(Math.PI / 6));
        this.setPrefSize(width.getValue(), height.getValue());

        //задаем параметры для перемещения поля
        fieldMoveX = 0;
        fieldMoveY = 0;
        //вычитываем координаты поля для его отрисовки
        fieldX = (fieldMoveX + GameApp.INDENT - GameApp.CENTRAL_PANE_WIDTH / 2) * scaleValue + GameApp.CENTRAL_PANE_WIDTH / 2;
        fieldY = (fieldMoveY + GameApp.INDENT - GameApp.CENTRAL_PANE_HEIGHT / 2) * scaleValue + GameApp.CENTRAL_PANE_HEIGHT / 2;
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
        width.setValue(GameApp.FIELD_WIDTH * scaleValue);
        height.setValue(GameApp.FIELD_HEIGHT * scaleValue);
        //перемещаем поле таким образом, чтобы поле не перемещалось относительно камеры при масштабировании
        move(0, 0);
        //изменяем скорость перемщения камеры, чтобы при сильном приближении камера не двигалась слишком быстро
        moveRange = GameApp.CELL_SIDE / scaleValue / MOVE_SPEED_DENOM;
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
        fieldX = (fieldMoveX + GameApp.INDENT - GameApp.CENTRAL_PANE_WIDTH / 2) * scaleValue + GameApp.CENTRAL_PANE_WIDTH / 2;
        fieldY = (fieldMoveY + GameApp.INDENT - GameApp.CENTRAL_PANE_HEIGHT / 2) * scaleValue + GameApp.CENTRAL_PANE_HEIGHT / 2;
        this.relocate(fieldX, fieldY);
    }


    public void addCell(int indX, int indY, CellView cellView) {
        //координаты cellView (по какой-то причине) считаются так, будто бы это прямоуголник,
        // после создания все становится нормально
        double cellWidth = cellView.widthProperty.getValue();
        double cellHeight = cellView.heightProperty.getValue();
        final double FIRST_CELL_X = cellWidth / 2 * indY;
        final double FIRST_CELL_Y = height.getValue() / 2 + cellHeight / 2 * indY - cellHeight / 2;
        double x = indX * cellWidth / 2 + FIRST_CELL_X;
        double y = FIRST_CELL_Y - indX * cellHeight / 2;
        cellView.relocate(x, y);
        this.getChildren().add(cellView);
    }

    public void addBuilding(AbstractBuildingView buildingView) {
        this.getChildren().add(buildingView);
    }

    public void moveBuilding(CellCore cellCore, AbstractBuildingView buildingView) {
        double x = cellCore.getView().getLayoutX();
        double y = cellCore.getView().getLayoutY();
        buildingView.moveTo(x, y);
    }

    /**
     * метод перемещающий здание на передний план
     * @param buildingView
     */
    public void moveBuildingToFront(AbstractBuildingView buildingView) {
        buildingView.toFront();
    }

    public void removeBuilding(AbstractBuildingView buildingView) {
        this.getChildren().remove(buildingView);
    }

    public void highlightAura(List<CellView> cellViewList, boolean bool, Aura aura) {
        for(CellView cellView: cellViewList) {
            if (bool) cellView.setAuraColor(aura);
            else  cellView.clearAuraColor();
        }
    }

    public double getX() {
        return fieldX;
    }

    public double getY() {
        return fieldY;
    }
}
