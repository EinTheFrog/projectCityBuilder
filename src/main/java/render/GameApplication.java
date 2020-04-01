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
        double cellSide = 50;
        int fieldSize = 10;
        double paneWidth = (cellSide + cellSide * Math.cos(Math.PI / 6)) * fieldSize + 100;
        double paneHeight = (cellSide * Math.sin(Math.PI / 6)) * fieldSize + 100;
        //задаем начальные элементы и параметры для них
        Stage gameWindow = new Stage();
        Scene gameScene;
        StackPane mainPane = new StackPane();

        mainPane.setPrefSize(paneWidth, paneHeight);
        gameScene = new Scene(mainPane);
        gameScene.getStylesheets().add("RedLord.css");

        //создаем объекты сцены
        Field field = new Field(fieldSize, new Cell(0, 0, cellSide, Color.BLACK));
        System.out.println(field.getPrefHeight() + " " + field.getPrefWidth());

        //добавляем объекты
        mainPane.getChildren().addAll(field);



        //рендерим окно
        gameWindow.setScene(gameScene);
        gameWindow.setTitle("Game");
        gameWindow.show();
    }

}
