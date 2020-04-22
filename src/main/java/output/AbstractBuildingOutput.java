package output;

import controller.Controller;
import core.AbstractBuilding;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;


public abstract class AbstractBuildingOutput extends Polygon {
    //конструктор
    public AbstractBuildingOutput (AbstractBuilding core) {
        //добавляем обработчик щелчка для передачи события клетке
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Controller.clickOnBuilding(core, event);
        });

        //обработчик движения мыши, чтобы показывать призрак здания на клетке за этим зданием
        this.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
                Controller.cursorOnBuildingInBuildingMod(core, event);
            event.consume();
        });
    }
}
