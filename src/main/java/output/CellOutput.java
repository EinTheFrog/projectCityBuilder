package output;

import controller.Controller;
import core.CellCore;
import core.FieldCore;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class CellOutput extends Polygon {
    private double pi = Math.PI;
    private FieldOutput parentField;
    CellCore core;

    //конструктор
    public CellOutput (double x, double y, double side, Color color, FieldOutput field, CellCore core) {
        //задаем параметры клетки
        this.core = core;
        this.relocate(x, y);
        parentField = field;
        //отрисовывем клетку
        this.getPoints().addAll(
                side * Math.cos(pi/ 6) , side * Math.sin(pi / 6),
                0.0, 0.0,
                side, 0.0,
                side * (1 + Math.cos(pi/ 6)), side * Math.sin(pi / 6)
        );
        this.setStroke(color);
        this.setFill(Color.rgb(0,0,0, 0));

        //доабвляем оработчик события для щелчка
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Controller.buildBuilding(core);
            event.consume();
        });
    }

    //getters
    public FieldOutput getParentField () {return parentField;}


}
