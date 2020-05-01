package output;

import controller.Controller;
import core.AbstractBuilding;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;



public abstract class AbstractBuildingOutput extends Polygon {
    //конструктор
    public AbstractBuildingOutput (AbstractBuilding core) {
        this.setMouseTransparent(true);
        //добавляем обработчик щелчка для передачи события клетке
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Controller.clickOnBuilding(core);
        });
    }

    protected abstract Image getImg();
}
