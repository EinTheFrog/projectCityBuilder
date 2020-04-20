package output;

import controller.Controller;
import core.AbstractBuilding;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;

import java.io.FileNotFoundException;

public abstract class AbstractBuildingOutput extends Polygon {
    //конструктор
    public AbstractBuildingOutput (AbstractBuilding core) {
        //задаем расположение
        this.relocate(core.getX() - core.getPicWidth() / 2, core.getY() - core.getPicHeight());

        //добавляем обработчик щелчка для передачи события клетке
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            //Controller.clickOnBuilding(core, event);
        });

        //обработчик движения мыши, чтобы показывать призрак здания на клетке за этим зданием
        this.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            /*try {
                //Controller.clickOnBuildingInBuildingMod(core, event);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
            event.consume();
        });
    }
}
