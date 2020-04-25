package render;

import controller.Controller;
import core.CasernCore;
import core.FieldCore;
import core.HouseCore;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class GameApplication {
    //задаем параметры создания игрвого поля
    public static final double indent = 50;
    public static final int fieldSize = 20;
    public static final double windowWidth = 1200;
    public static final double windowHeight = 2 * windowWidth / (2 * Math.cos(Math.PI / 6)) * Math.sin(Math.PI / 6);
    public static final double paneWidth = windowWidth - 2 * indent;
    public static final double paneSide = paneWidth / (2 * Math.cos(Math.PI / 6));
    public static double paneHeight = 2 * paneSide * Math.sin(Math.PI / 6);
    public static final double cellSide = paneSide / fieldSize;
    public static final Color cellColor = Color.rgb(178, 178, 177 );
    //создаем объекты для игрвого окна и корневой панели
    static Stage gameWindow;
    public static BorderPane mainPane;

    public static void run () throws FileNotFoundException {

        //инициализируем stage, scene и корневой layout
        gameWindow = new Stage();
        Scene gameScene;
        mainPane = new BorderPane();

        mainPane.setPrefSize(windowWidth, windowHeight);
        gameScene = new Scene(mainPane);
        gameScene.getStylesheets().add("RedLord.css");

        //создаем панели, на которых будут располагаться все элементы
        Pane fieldPane = new Pane(); //панель для игрвого поля
        ToolBar toolsPane = new ToolBar(); //панель для интерфейса построек
        //fieldOutput добавиться в fieldPane в своем конструкторе, поэтому просто инициализируем игровое поле
        FieldCore fieldCore = new FieldCore(fieldSize, cellSide, paneSide, cellColor, fieldPane, indent);
        //устанавливаем фокус на этом игровом поле
        fieldCore.getOutput().requestFocus();
        Controller.chooseField(fieldCore);

        fieldPane.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            double cursorOnFieldX = event.getX() - fieldCore.getX();
            double cursorOnFieldY = event.getY() - fieldCore.getY();
            Controller.moveCursor(cursorOnFieldX, cursorOnFieldY);
            event.consume();
        });
        //задаем параметры для кнопки в меню построек
        ImageView imgHouseBtn = new ImageView(new Image(new FileInputStream("src/main/resources/buttons0014.png")));
        imgHouseBtn.setFitWidth(paneHeight / 10 );
        imgHouseBtn.setFitHeight(paneHeight / 10 );
        Button btnHouse = new Button("", imgHouseBtn);
        btnHouse.setId("control_button");

        Button btnNone = new Button();
        btnNone.setPrefWidth(paneHeight / 10 );
        btnNone.setPrefHeight(paneHeight / 10 );
        btnHouse.setId("control_button");

        ImageView imgCasernBtn = new ImageView(new Image(new FileInputStream("src/main/resources/buttons0006.png")));
        imgCasernBtn.setFitWidth(paneHeight / 10 );
        imgCasernBtn.setFitHeight(paneHeight / 10 );
        Button btnCasern = new Button("", imgCasernBtn);
        btnHouse.setId("control_button");

        //создаем события для нажатия на кнопки на панели
        btnHouse.setOnAction(event -> {
            Controller.pressOnBuildingButton(fieldCore, new HouseCore(0,0, 1, 1, 2, fieldCore, 0));
        });

        btnNone.setOnAction(event -> {
            Controller.pressOnChooseButton(fieldCore);
        });

        btnCasern.setOnAction(event -> {
            Controller.pressOnBuildingButton(fieldCore, new CasernCore(0,0, 1, 1, 2, fieldCore, 0));
        });


        toolsPane.getItems().addAll(btnHouse, btnCasern, btnNone);
        //создаем обработку щелчка мыши при открытом окне меню для закрытия этог самого меню
        mainPane.addEventFilter(MouseEvent.ANY, event -> {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                Controller.closeMenu(event);
            }
        });

        //добавляем объекты
        mainPane.setCenter(fieldPane);
        mainPane.setBottom(toolsPane);

        //рендерим окно и запускаем таймер
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
