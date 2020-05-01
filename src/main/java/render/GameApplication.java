package render;

import controller.Controller;
import core.*;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import output.AbstractBuildingOutput;

import java.io.InputStream;


public class GameApplication {
    //задаем параметры создания игрвого поля
    public static final double indent = 50;
    public static final int fieldSize = 20;
    public static final double mainWindowWidth = 1200;
    public static final double paneWidth = mainWindowWidth - 2 * indent;
    public static final double paneSide = paneWidth / (2 * Math.cos(Math.PI / 6));
    public static final double paneHeight = 2 * paneSide * Math.sin(Math.PI / 6);
    public static final double mainWindowHeight = paneHeight + 2 * indent;
    public static final double cellSide = paneSide / fieldSize;
    public static final Color cellColor = Color.rgb(178, 178, 177 );
    private static Label lblGold;

    //создаем объекты для игрвого окна и корневой панели
    static Stage gameWindow = new Stage();
    public static BorderPane mainPane;
    private static Pane fieldPane;
    private static VBox buildingPane;
    private static GridPane gridPane;
    private static StackPane stackPane;
    private static ToolBar resourcesPane;
    private static ToolBar toolsPane;

    private static Label lblBuildingCost;
    private static Label lblBuildingName;

    public static void run () {
        //инициализируем stage, scene и корневой layout
        Scene gameScene;
        mainPane = new BorderPane();
        gameScene = new Scene(mainPane);
        mainPane.getStylesheets().add("RedLord.css");

        //создаем панели, на которых будут располагаться все элементы
        gridPane = new GridPane();
        gridPane.setFocusTraversable(false);
        stackPane = new StackPane();
        stackPane.setFocusTraversable(false);
        buildingPane = new VBox();
        buildingPane.setStyle("-fx-background-color: #C0392B; -fx-border-color: #F5B041; -fx-alignment: CENTER");

        fieldPane = new Pane(); //панель для игрвого поля
        fieldPane.setPrefSize(mainWindowWidth, mainWindowHeight);
        toolsPane = new ToolBar(); //панель для интерфейса построек
        resourcesPane = new ToolBar(); //панель для информации о ресурсах
        resourcesPane.setFocusTraversable(false);
        //fieldOutput добавиться в fieldPane в своем конструкторе, поэтому просто инициализируем игровое поле
        FieldCore fieldCore = new FieldCore(fieldSize, cellSide, paneSide, cellColor, fieldPane, indent);
        //устанавливаем фокус на этом игровом поле
        //fieldCore.getOutput().requestFocus();
        Controller.chooseField(fieldCore);

        //добавляем обрабочик перемещения курсора внутри игрового окна
        fieldPane.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            double cursorOnFieldX = event.getX() - fieldCore.getX() * fieldCore.getScale();
            double cursorOnFieldY = event.getY() - fieldCore.getY() * fieldCore.getScale();
            Controller.moveCursor(cursorOnFieldX, cursorOnFieldY);
            event.consume();
        });
        //задаем параметры для кнопки в меню построек
        String respath = "/textures/btnHouse.png";
        InputStream in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgHouseBtn = new ImageView(new Image(in));
        imgHouseBtn.setFitWidth(mainWindowHeight / 10 );
        imgHouseBtn.setFitHeight(mainWindowHeight / 10 );
        Button btnHouse = new Button("", imgHouseBtn);
        btnHouse.setId("control_button");
        btnHouse.setFocusTraversable(false);

        ImageView imgNoneBtn = new ImageView();
        imgNoneBtn.setFitWidth(mainWindowHeight / 10 );
        imgNoneBtn.setFitHeight(mainWindowHeight / 10 );
        Button btnNone = new Button("", imgNoneBtn);
        btnNone.setId("control_button");
        btnNone.setFocusTraversable(false);

        respath = "/textures/btnCasern.png";
        in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgCasernBtn = new ImageView(new Image(in));
        imgCasernBtn.setFitWidth(mainWindowHeight / 10 );
        imgCasernBtn.setFitHeight(mainWindowHeight / 10 );
        Button btnCasern = new Button("", imgCasernBtn);
        btnCasern.setId("control_button");
        btnCasern.setFocusTraversable(false);

        respath = "/textures/btnCastle.png";
        in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgCastleBtn = new ImageView(new Image(in));
        imgCastleBtn.setFitWidth(mainWindowHeight / 10 );
        imgCastleBtn.setFitHeight(mainWindowHeight / 10 );
        Button btnCastle = new Button("", imgCastleBtn);
        btnCastle.setId("control_button");
        btnCastle.setFocusTraversable(false);

        respath = "/textures/btnTavern.png";
        in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgTavernBtn = new ImageView(new Image(in));
        imgTavernBtn.setFitWidth(mainWindowHeight / 10 );
        imgTavernBtn.setFitHeight(mainWindowHeight / 10 );
        Button btnTavern = new Button("", imgTavernBtn);
        btnTavern.setId("control_button");
        btnTavern.setFocusTraversable(false);

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
        btnCastle.setOnAction(event -> {
            Controller.pressOnBuildingButton(fieldCore, new CastleCore(0,0, 1, 1, 8, fieldCore, 0));
        });
        btnTavern.setOnAction(event -> {
            Controller.pressOnBuildingButton(fieldCore, new TavernCore(0,0, 1, 1, 2, fieldCore, 0));
        });


        //добавляем кнопки на панель
        toolsPane.getItems().addAll(btnHouse, btnCasern, btnTavern, btnCastle, btnNone);

        //задаем параметры элементов панели ресурсов
        respath = "/textures/gold.png";
        in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgGoldPic = new ImageView(new Image(in));
        imgGoldPic.setFitWidth(mainWindowHeight / 20 );
        imgGoldPic.setFitHeight(mainWindowHeight / 20 );

        lblGold = new Label(String.valueOf(fieldCore.getGold()));

        resourcesPane.getItems().addAll(imgGoldPic, lblGold);

        //задаем параметры для панели здания
        lblBuildingCost = new Label("0");
        lblBuildingName = new Label("Building");
        buildingPane.getChildren().addAll(lblBuildingName, lblBuildingCost);
        buildingPane.setVisible(false);

        //добавляем объекты
        Pane p1 = new Pane();
        DoubleProperty d;
        p1.prefWidthProperty().bind(fieldPane.widthProperty().subtract(buildingPane.widthProperty()));
        p1.setVisible(false);
        gridPane.add(p1, 0,0);
        gridPane.add(buildingPane, 1,0);
        gridPane.setMouseTransparent(true);
        stackPane.getChildren().addAll(fieldPane, gridPane);
        mainPane.setCenter(stackPane);
        mainPane.setBottom(toolsPane);
        mainPane.setTop(resourcesPane);


        //рендерим окно и запускаем таймер
        Controller.startTimer();

       // gameWindow
        gameWindow.addEventFilter(MouseEvent.ANY, event -> {
            //создаем обработку щелчка мыши при открытом окне меню для закрытия этог самого меню
            Controller.closeMenuOnClick(event);
        });
        gameWindow.xProperty().addListener(((observable, oldValue, newValue) -> {
            Menu.move(getX(), getY());
        }));
        gameWindow.yProperty().addListener(((observable, oldValue, newValue) -> {
            Menu.move(getX(), getY());
        }));
        gameWindow.setScene(gameScene);
        gameWindow.setTitle("Game");
        gameWindow.sizeToScene();
        gameWindow.setResizable(false);
        gameWindow.show();

        //закрытие окна осуществляем через собственный метод
        gameWindow.setOnCloseRequest(event -> {
            event.consume();
            Controller.stopTimer();
            stop();
        });
    }

    public static void pause () {
        Controller.stopTimer();
    }
    public static void resume () {
        Controller.startTimer();
    }

    //событие для закрытия игрвого окна
    public static void stop () {
        Controller.stopTimer();
        Menu.close();
        gameWindow.close();
    }

    //public static void focusOnGameWindow() {gameWindow.requestFocus();}
    public static void writeGold(int gold) {
        lblGold.setText(String.valueOf(gold));
    }
    public static double getX() { return gameWindow.getX(); }
    public static double getY() { return gameWindow.getY(); }

    public static void showBuildingInfo (int cost, String name){
        lblBuildingName.setText(name);
        lblBuildingCost.setText(String.valueOf(cost));
        buildingPane.setVisible(true);
    }
    public static void hideBuildingInfo (){
        buildingPane.setVisible(false);
    }

    public static double getResourcesPaneHeight() { return resourcesPane.getHeight(); }
    public static double getToolsPaneHeight() { return resourcesPane.getHeight(); }
}
