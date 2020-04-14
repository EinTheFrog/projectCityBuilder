package output;

import controller.Controller;
import core.FieldCore;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class FieldOutput extends Pane{
    private Pane parentPane;
    private double cellSide;
    private double cellHeight;
    private double indent;
    private FieldCore core;
    private final Color cellColor = Color.rgb(178, 178, 177);

    public FieldOutput (int size, double cellSide, Pane parentPane, double indent, FieldCore core) {
        //устанавливаем фокус на этом игровом поле
        this.setFocusTraversable(true);
        //записываем значения перемнных
        this.parentPane = parentPane; //панель, на которой находится игровое поле
        this.indent = indent; // отступ
        this.setLayoutX(indent);
        this.setLayoutY(indent);
        this.core = core;
        this.cellSide = cellSide; //длина стороны одной клетки на игровом поле
        cellHeight = cellSide * Math.sin(Math.PI / 6);
        this.setPrefSize(cellSide * size * (1 + Math.cos(Math.PI / 6)), cellHeight * size);
        //задаем цвет для панели, на которой нахидится игрвое поле и добавляем наше поле на панель
        this.parentPane.setBackground(
                new Background(new BackgroundFill(Color.rgb(133, 106, 84), null, null)));
        this.parentPane.getChildren().add(this);


        //добавляем обработчиков событий для взаимодействия с пользователем,
        // сама обработка событий реализуется в Controller
        this.addEventHandler(ScrollEvent.SCROLL, event -> {
            Controller.zoom(event.getDeltaY(), core);
        });
        this.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            Controller.keyPressed(event.getCode(), core);
        });
        this.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            Controller.keyReleased(event.getCode(), core);
        });
    }

    //метод для симуляции приближения камеры к игрвому полю
    public void zoom (double scaleValue) {
        this.setScaleX(scaleValue);
        this.setScaleY(scaleValue);
    }

    //метод для симуляции движения игровго поля
    public void move (double x, double y, double dw, double dh) {
        parentPane.setPrefSize(this.getPrefWidth() + dw, this.getPrefHeight() + dh);
        this.relocate(x, y);
    }



    //метод для графического добавление узла на игровое поле
    public void add (Node node) {this.getChildren().add(node);}

    //getters
    public double getCellSide () {return cellSide;}
    public double getIntend() { return indent; }
    public Color getCellColor() {return cellColor;}
    public FieldCore getCore() {return core;}
}
