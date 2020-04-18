package render;

import controller.Controller;
import core.FieldCore;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import logic.Mod;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class GameApplication {
    //задаем параметры создания игрвого поля
    static final double indent = 50;
    static final int fieldSize = 20;
    static final double paneWidth = 1000 ;
    static final double paneSide = paneWidth / (2 * Math.cos(Math.PI / 6));
    static double paneHeight = 2 * paneSide * Math.sin(Math.PI / 6);
    static final double cellSide = paneSide / fieldSize;
    static final Color cellColor = Color.rgb(178, 178, 177 );
    //создаем объекты для игрвого окна и корневой панели
    static Stage gameWindow;
    public static BorderPane mainPane;

    public static void run () throws FileNotFoundException {

        //инициализируем stage, scene и корневой layout
        gameWindow = new Stage();
        Scene gameScene;
        mainPane = new BorderPane();

        mainPane.setPrefSize(paneWidth + 2 * indent, paneHeight + 2 * indent);
        gameScene = new Scene(mainPane);
        gameScene.getStylesheets().add("RedLord.css");

        //создаем панели, на которых будут располагаться все элементы
        Pane fieldPane = new Pane(); //панель для игрвого поля
        ToolBar toolsPane = new ToolBar(); //панель для интерфейса построек
        //fieldOutput добавиться в fieldPane в своем конструкторе, поэтому просто инициализируем игровое поле
        FieldCore fieldCore = new FieldCore(fieldSize, cellSide, paneSide, cellColor, fieldPane, indent);
        //устанавливаем фокус на этом игровом поле
        fieldCore.getOutput().requestFocus();
        //задаем параметры для кнопки в меню построек
        ImageView imgHouseBtn = new ImageView(new Image(new FileInputStream("src/main/resources/buttons0014.png")));
        imgHouseBtn.setFitWidth(paneHeight / 10 );
        imgHouseBtn.setFitHeight(paneHeight / 10 );
        Button btnHouse = new Button("", imgHouseBtn);
        btnHouse.setId("control_button");

        //создаем событие для нажатия на кнопку
        btnHouse.setOnAction(event -> {
            Controller.pressOnBuildingButton(fieldCore);
        });
        toolsPane.getItems().add(btnHouse);
        //создаем обработку щелчка мыши при открытом окне меню для закрытия этог самого меню
        mainPane.addEventFilter(MouseEvent.ANY, event -> {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                Controller.closeMenu(event);
            }
        });

        //добавляем объекты
        fieldPane.toBack();
        mainPane.setCenter(fieldPane);
        toolsPane.toFront();
        mainPane.setBottom(toolsPane);

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
