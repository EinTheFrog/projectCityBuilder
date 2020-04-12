package render;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import output.FieldOutput;


public class GameApplication {
    static double intend = 50;
    static int fieldSize = 20;
    static double paneWidth = 1000 ;
    static double paneSide = paneWidth / (1 + Math.cos(Math.PI / 6));
    static double paneHeight = paneSide * Math.sin(Math.PI / 6);
    static final double cellSide = paneSide / fieldSize;

    public static void run () {
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
        mainPane.setFocusTraversable(false);
        FieldOutput fieldOutput = new FieldOutput(fieldSize, cellSide, fieldPane, intend);

        //добавляем объекты
        mainPane.getChildren().addAll(fieldPane);

        //рендерим окно
        gameWindow.setScene(gameScene);
        gameWindow.setTitle("Game");
        gameWindow.show();
    }

}
