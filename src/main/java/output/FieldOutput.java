package output;

import controller.Controller;
import controller.GameApplicationController;
import core.FieldCore;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import render.GameApp;

public class FieldOutput extends Pane{
    private double indent;
    private FieldCore core;
    private Scale scale = new Scale();

    public FieldOutput (FieldCore core) {
        //позволяем фокусироваться на игровом поле
        this.setFocusTraversable(true);
        //записываем значения перемнных
        this.indent = core.getIndent(); // отступ
        this.setLayoutX(core.getX());
        this.setLayoutY(core.getY());
        this.core = core;
        this.setPrefSize(core.getWidth(), core.getHeight());
        //задаем нужные параметры для преобразования scale и применяем его
        scale.setPivotX(0);
        scale.setPivotY(0);
        scale.setX(1);
        scale.setY(1);
        this.getTransforms().add(scale);
        //окрашиваем панель (для наглядности на время разработки)
        this.setBackground(
                new Background(new BackgroundFill(Color.rgb(10, 106, 84), null, null)));



        //добавляем обработчиков событий для взаимодействия с пользователем,
        // сама обработка событий реализуется в Controller
        this.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            Controller.keyPressed(event.getCode());
            if (event.getCode() == KeyCode.X) Controller.zoom(50, core);
            if (event.getCode() == KeyCode.Z) Controller.zoom(-50, core);
        });
        this.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            Controller.keyReleased(event.getCode(), core);
        });

    }

    //метод для симуляции приближения камеры к игрвому полю
    public void zoom (double scaleValue) {
        scale.setX(scaleValue);
        scale.setY(scaleValue);
    }

    //метод для симуляции движения камеры над полем
    public void move (double x, double y) {
        this.relocate(x, y);
    }

    //метод для графического добавление узла на игровое поле
    public void add (Node node) {
        this.getChildren().remove(node);
        this.getChildren().add(node);
    }

    public void draw(Pane fieldPane) {
        core.move(0,0);
        //задаем цвет для панели, на которой нахидится игрвое поле и добавляем наше поле на панель
        fieldPane.setBackground(
                new Background(new BackgroundFill(Color.rgb(4, 10, 84), null, null)));
        fieldPane.getChildren().add(this);
    }

    //getters
    public FieldCore getCore() {return core;}
}
