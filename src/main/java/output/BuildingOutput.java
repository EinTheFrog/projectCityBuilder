package output;

import controller.Controller;
import core.BuildingCore;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BuildingOutput extends Pane {
    private double x;
    private double y;
    private double height;
    private double side;
    private double pi = Math.PI;
    private FieldOutput parentField;
    private BuildingCore core;

    //конструктор
    public BuildingOutput (double x, double y, double side, double height, FieldOutput field, BuildingCore core) throws FileNotFoundException {
        //задаем параметры
        this.x = x;
        this. y = y;
        this.relocate(x, y - height);
        parentField = field;
        this.side = side;
        this.height = height;
        this. core = core;
        //отрисовываем
        /*this.getPoints().addAll(
                side * Math.cos(pi/ 6) , height + side * Math.sin(pi / 6),
                0.0, height,
                0.0 , 0.0,
                side, 0.0,
                side * (1 + Math.cos(pi/ 6)), side * Math.sin(pi / 6),
                side * (1 + Math.cos(pi/ 6)), height+ side * Math.sin(pi / 6)
        );*/
        try {
            ImageView imgView = new ImageView(new Image(new FileInputStream("src/main/resources/thatched.png")));
            this.getChildren().add(imgView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
