package output;

import controller.Controller;
import core.buildings.AbstractBuilding;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import render.GameApp;

import java.io.InputStream;


public abstract class AbstractBuildingOutput extends ImageView {
    //конструктор
    public AbstractBuildingOutput (AbstractBuilding core) {
        this.setMouseTransparent(true);
        this.setPickOnBounds(false);
        this.setFitWidth(core.getPicWidth());
        this.setFitHeight(core.getPicHeight());

        String respath = getImgPath();
        InputStream in = GameApp.class.getResourceAsStream(respath);
        Image img = new Image(in);
        this.setImage(img);
        //добавляем обработчик щелчка для передачи события клетке
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Controller.setChosenBuilding(core);
        });
    }

    protected abstract String getImgPath();
}
