package render;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import logic.Cell;
import logic.Field;


public class GameApplication {

    public static void run () {
        double paneWidth = 800;
        double paneHeight = 600;
        double cellSide = 50;
        //задаем начальные элементы и параметры для них
        Stage gameWindow = new Stage();
        Scene gameScene;
        StackPane mainPane = new StackPane();

        mainPane.setPrefSize(paneWidth, paneHeight);
        gameScene = new Scene(mainPane);
        gameScene.getStylesheets().add("RedLord.css");

        //создаем объекты сцены
        final Canvas canvas = new Canvas(paneWidth, paneHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(20, 114, 77 ));
        gc.fillRect(0,0, paneWidth, paneHeight);

        Field field = new Field(10, new Cell(50, 50, cellSide, Color.YELLOW));
        System.out.println(field.getPrefHeight() + " " + field.getPrefWidth());

        //добавляем объекты
        mainPane.getChildren().addAll(canvas, field);



        //рендерим окно
        gameWindow.setScene(gameScene);
        gameWindow.setTitle("Game");
        gameWindow.show();
    }

}
