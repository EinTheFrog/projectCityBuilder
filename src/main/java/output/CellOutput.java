package output;

import controller.Controller;
import core.CellCore;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class CellOutput extends Polygon {
    private FieldOutput parentField;
    CellCore core;

    //конструктор
    public CellOutput (CellCore core) {
        //задаем параметры клетки
        this.core = core;
        double x = core.getX();
        double y = core.getY();
        this.relocate(x, y);
        parentField = core.getField().getOutput();
        double width = core.getWidth();
        double height = core.getHeight();
        Color borderColor = core.getBorderColor();
        parentField.add(this);

        //отрисовывем клетку
        this.getPoints().addAll(
                0.0 , 0.0,
                - width / 2, - height / 2,
                0.0 , - height,
                width / 2, - height / 2
        );
        this.setStroke(borderColor);
        this.setFill(core.getFillColor());

        //доабвляем оработчик события для щелчка
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("click");
            Controller.buildBuilding();
            event.consume();
        });
    }


}
