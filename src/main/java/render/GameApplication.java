package render;

import controller.Controller;
import core.FieldCore;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;




public class GameApplication {
    //задаем параметры создания игрвого поля
    static final double indent = 50;
    static final int fieldSize = 50;
    static final double paneWidth = 1000 ;
    static final double paneSide = paneWidth / (2 * Math.cos(Math.PI / 6));
    static double paneHeight = 2 * paneSide * Math.sin(Math.PI / 6);
    static final double cellSide = paneSide / fieldSize;
    static final Color cellColor = Color.rgb(178, 178, 177 );
    //создаем объекты для игрвого окна и корневой панели
    static Stage gameWindow;
    public static StackPane mainPane;

    public static void run () {

        //задаем начальные элементы и параметры для них
        gameWindow = new Stage();
        Scene gameScene;
        mainPane = new StackPane();
        Pane fieldPane = new Pane();

        mainPane.setPrefSize(paneWidth + 2 * indent, paneHeight + 2 * indent);
        gameScene = new Scene(mainPane);
        gameScene.getStylesheets().add("RedLord.css");

        //создаем объекты сцены
        mainPane.setFocusTraversable(false); //убираем фокус с mainPane, фокус переключтся на fieldOutput при его создании
        FieldCore fieldCore = new FieldCore(fieldSize, cellSide, paneSide, cellColor, fieldPane, indent); //fieldOutput добавиться в
        // fieldPane в своем конструкторе


        //создаем обработку щелчка мыши при открытом окне меню для закрытия этог самого меню
        mainPane.addEventFilter(MouseEvent.ANY, event -> {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                Controller.closeMenu(event);
            }
        });

        //добавляем объекты
        mainPane.getChildren().addAll(fieldPane);

        //рендерим окно
        gameWindow.setScene(gameScene);
        gameWindow.setTitle("Game");
        gameWindow.show();

        //закрытие окна осуществляем через собственный метод
        gameWindow.setOnCloseRequest(event -> {
            event.consume();
            stop();
        });
    }

    //событие для закрытия игрвого окна
    public static void stop () {
        Menu.close();
        gameWindow.close();
    }

}
