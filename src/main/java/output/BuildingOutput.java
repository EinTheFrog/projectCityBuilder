package output;

import controller.Controller;
import core.BuildingCore;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BuildingOutput extends Polygon {

    //конструктор
    public BuildingOutput (double x, double y, double width, double height,double cellWidth,double cellHeight,
                           FieldOutput field, BuildingCore core) throws FileNotFoundException {
        //отрисовываем
        this.getPoints().addAll(
                0.0 , 0.0,
                - cellWidth / 2, - cellHeight / 2,
                - cellWidth / 2, - height,
                cellWidth / 2, - height,
                cellWidth / 2, - cellHeight / 2
        );
        try {
            Image img = new Image(new FileInputStream("src/main/resources/thatched.png"));
            this.setFill(new ImagePattern(img, cellWidth / 2, 0, width, height, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //this.setFill(Color.rgb(100, 200, 100));
        this.relocate(x - cellWidth / 2, y - height);

        //добавляем обработчик щелчка
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                Controller.clickOnBuilding(core, event);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }


}
