package view;

import controller.Controller;
import controller.FieldController;
import core.CellCore;
import core.FieldCore;
import core.buildings.AbstractBuilding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import render.GameApp;
import view.buildings.AbstractBuildingView;

public class FieldView extends Pane {
    private double fieldX;
    private double fieldY;
    private double fieldMoveX;
    private double fieldMoveY;
    public DoubleProperty width;
    public DoubleProperty height;
    private double scaleValue = 1.0;
    private Scale scale = new Scale();
    public static final Color COLOR = Color.rgb(10, 106, 84);
    //для каждого поля у нас своё положение камеры, а значит у каждого поля должны быть свои параметры scale
    // и скорости перемщения камеры
    private double moveRange = GameApp.CELL_SIDE / Controller.moveSpeedDenom;

    public FieldView () {
        //позволяем фокусироваться на игровом поле
        this.setFocusTraversable(true);
        width = new SimpleDoubleProperty(2 * GameApp.PANE_SIDE * Math.cos(Math.PI / 6));
        height = new SimpleDoubleProperty( 2 * GameApp.PANE_SIDE * Math.sin(Math.PI / 6));
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
        //окрашиваем панель (для наглядности на время разработки)
        this.setBackground(
                new Background(new BackgroundFill(COLOR, null, null)));

    }

    //метод для приближения камеры
    public void zoom (double scrollValue) {
        //увеличивая значение scale создаем эфект приближения камеры
        if (scaleValue + scrollValue / Controller.BASE_SCROLL > 0 && scaleValue + scrollValue / Controller.BASE_SCROLL < 20)
            scaleValue += scrollValue / Controller.BASE_SCROLL;
        setScale(scaleValue);
        //вычисляем новую ширину и высоту
        width.setValue(GameApp.PANE_WIDTH * scaleValue);
        height.setValue(GameApp.PANE_HEIGHT * scaleValue);
        this.setPrefSize(width.getValue(), height.getValue());
        //перемещаем поле таким образом, чтобы поле не перемещалось относительно камеры при масштабировании
        move(0, 0);
        //изменяем скорость перемщения камеры, чтобы при сильном приближении камера не двигалась слишком быстро
        moveRange = GameApp.CELL_SIDE * scaleValue / Controller.moveSpeedDenom;
    }

    //метод для симуляции приближения камеры к игрвому полю
    private void setScale (double scaleValue) {
        scale.setX(scaleValue);
        scale.setY(scaleValue);
    }

    //метод для перемщения камеры (самого поля относительно камеры)
    public void move(double dx, double dy) {
        fieldMoveX += dx;
        fieldMoveY += dy;
        //вычитываем координаты поля для его отрисовки
        fieldX = (fieldMoveX + GameApp.INDENT - GameApp.CENTRAL_PANE_WIDTH / 2) * scaleValue + GameApp.CENTRAL_PANE_WIDTH / 2;
        fieldY = (fieldMoveY + GameApp.INDENT - GameApp.CENTRAL_PANE_HEIGHT / 2) * scaleValue + GameApp.CENTRAL_PANE_HEIGHT / 2;
        this.relocate(fieldX, fieldY);
    }

    //метод для нахождения клетки по координатам (используется в Controller.moveCursor)
/*    public CellCore findCell(double x, double y) {
        //изменяем координаты курсора так, чтобы его положение правильно вопронималось относительно клеток при масштабировании поля
        double cursorX = (x - GameApp.CENTRAL_PANE_WIDTH / 2) / scaleValue + GameApp.CENTRAL_PANE_WIDTH / 2 ;
        double cursorY = (y - GameApp.CENTRAL_PANE_HEIGHT / 2) / scaleValue + GameApp.CENTRAL_PANE_HEIGHT / 2;
        //ищем клетку
        int j = -1;;
        cursorY -= GameApp.PANE_SIDE * Math.sin(Math.PI / 6);
        while (cursorY <= Math.tan(Math.PI / 6) * cursorX - j * cellHeight) { j++; }
        j--;
        int i = -1;
        while (cursorY >= - Math.tan(Math.PI / 6) * cursorX + i * cellHeight) { i++; }
        if (i >= 0 && j >= 0 && i < core.getSize() && j < core.getSize()) {
            return core.getCellsArray()[j][i];
        }
        else return null;
    }*/

    //метод для графического добавление узла на игровое поле
/*    public void add (Node node) {
        this.getChildren().remove(node);
        this.getChildren().add(node);
    }*/

    public void addCell(int indX, int indY, CellView cellView) {
        //тут какие-то беды с логикой и координаты cellView считаются так, будто бы это прямоуголник
        //почему? кто знает...
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

    public void redrawBuilding(AbstractBuildingView buildingView) {
        //эксперимент
        buildingView.toFront();
    }

    public void removeBuilding(AbstractBuildingView buildingView) {
        this.getChildren().remove(buildingView);
    }


    //getters
    public double getX() {return fieldX;}
    public double getY() {return fieldY;}
    public double getScale() {return scaleValue;}
    public  double getMoveRange() {return moveRange;}
}
