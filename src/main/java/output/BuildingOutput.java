package output;

import controller.Controller;
import core.BuildingCore;
import core.FieldCore;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class BuildingOutput extends Polygon {
    private double x;
    private double y;
    private double height;
    private double side;
    private double pi = Math.PI;
    private FieldOutput parentField;
    private BuildingCore core;

    //конструктор
    public BuildingOutput (double x, double y, double side, double height, FieldOutput field, BuildingCore core) {
        //задаем параметры
        this.x = x;
        this. y = y;
        this.relocate(x, y - height);
        parentField = field;
        this.side = side;
        this.height = height;
        this. core = core;
        //отрисовываем
        this.getPoints().addAll(
                side * Math.cos(pi/ 6) , height + side * Math.sin(pi / 6),
                0.0, height,
                0.0 , 0.0,
                side, 0.0,
                side * (1 + Math.cos(pi/ 6)), side * Math.sin(pi / 6),
                side * (1 + Math.cos(pi/ 6)), height+ side * Math.sin(pi / 6)
        );
        this.setFill(Color.rgb(0,0,0));

        //добавляем обработчик щелчка
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Controller.clickOnBuilding(core, event);
        });
    }


}
