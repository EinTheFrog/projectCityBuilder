package output;

import controller.Controller;
import core.CellCore;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.io.FileNotFoundException;

public class CellOutput extends Polygon {
    private FieldOutput parentField;
    CellCore core;

    //конструктор
    public CellOutput (double x, double y, double width, double height, Color color, FieldOutput field, CellCore core) {
        //задаем параметры клетки
        this.core = core;
        this.relocate(x, y);
        parentField = field;
        //отрисовывем клетку
        this.getPoints().addAll(
                0.0 , 0.0,
                - width / 2, - height / 2,
                0.0 , - height,
                width / 2, - height / 2
        );
        this.setStroke(color);
        this.setFill(Color.rgb(0,0,0, 0));

        //доабвляем оработчик события для щелчка
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Controller.buildBuilding();
            event.consume();
        });
    }


}
