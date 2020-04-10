package render;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import logic.Cell;
import logic.Field;


public class GameApplication {

    public static void run () {
        double intend = 50;
        int fieldSize = 20;
        double paneWidth = 1000 ;
        double paneSide = paneWidth / (1 + Math.cos(Math.PI / 6));
        double paneHeight = paneSide * Math.sin(Math.PI / 6);
        double cellSide = paneSide / fieldSize;
        paneWidth += 2 * intend;
        paneHeight += 2 * intend;

        //задаем начальные элементы и параметры для них
        Stage gameWindow = new Stage();
        Scene gameScene;
        StackPane mainPane = new StackPane();
        Pane fieldPane = new Pane();

        mainPane.setPrefSize(paneWidth, paneHeight);
        gameScene = new Scene(mainPane);
        gameScene.getStylesheets().add("RedLord.css");

        //создаем объекты сцены
        Field field = new Field(fieldSize, cellSide, fieldPane, intend);
        field.setFocusTraversable(true);

        //добавляем объекты
        fieldPane.getChildren().add(field);
        mainPane.getChildren().addAll(fieldPane);

        //рендерим окно
        gameWindow.setScene(gameScene);
        gameWindow.setTitle("Game");
        gameWindow.show();
    }

}
