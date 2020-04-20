package output;

import controller.Controller;
import core.BuildingCore;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import logic.BuildingTypes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BuildingOutput extends Polygon {

    //конструктор
    public BuildingOutput (double x, double y, double width, double height, double areaWidth, double areaHeight,
                           BuildingTypes type, BuildingCore core) {
        //отрисовываем базувую форму (в зависимости от типа здания)
        if (type == BuildingTypes.SQUARE) {
            this.getPoints().addAll(
                    0.0 , 0.0,
                    - areaWidth / 2, - areaHeight / 2,
                    - areaWidth / 2, - height,
                    areaWidth / 2, - height,
                    areaWidth / 2, - areaHeight / 2
            );
        }

        // загружаем текстуру для здания
        try {
            Image img = new Image(new FileInputStream("src/main/resources/thatched.png"));
            this.setFill(new ImagePattern(img, areaWidth / 2, 0, width, height, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //задаем расположение
        this.relocate(x - areaWidth / 2, y - height);

        //добавляем обработчик щелчка для передачи события клетке
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Controller.clickOnBuilding(core, event);
        });

        this.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            try {
                Controller.clickOnBuildingInBuildingMod(core, event);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            event.consume();
        });
    }


}
