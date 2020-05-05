package render;

import controller.Controller;
import core.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.function.Supplier;


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
    public static final Color cellBorderColor = Color.rgb(178, 178, 177 );
    public static final Color cellFillColor = Color.rgb(10, 106, 84);

    //создаем игровые параметры
    private static SimpleIntegerProperty gold = new SimpleIntegerProperty();
    private static SimpleIntegerProperty force = new SimpleIntegerProperty();
    private static SimpleIntegerProperty people = new SimpleIntegerProperty();

    //создаем объекты для игрвого окна и корневой панели
    static Stage gameWindow = new Stage();
    public static BorderPane mainPane;
    private static Pane fieldPane;
    private static VBox buildingPane;
    private static GridPane gridPane;
    private static StackPane stackPane;
    private static ToolBar resourcesPane;
    private static ToolBar toolsPane;

    private static SimpleIntegerProperty goldCost = new SimpleIntegerProperty();
    private static SimpleIntegerProperty peopleCost = new SimpleIntegerProperty();
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
        FieldCore fieldCore = new FieldCore(fieldSize, cellSide, paneSide, cellBorderColor, cellFillColor, fieldPane, indent);
        fieldCore.draw();
        fieldCore.createCells();
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
        //создаем кнопки для меню построек
        Button btnHouse = createBuildingButton("/textures/btnHouse.png", () ->
                new HouseCore(0,0, 1, 1, 2, Controller.getChosenField(), 0));
        Button btnCasern = createBuildingButton("/textures/btnCasern.png", () ->
                new CasernCore(0,0, 1, 1, 2, Controller.getChosenField(), 0));
        Button btnTavern = createBuildingButton("/textures/btnTavern.png", () ->
                new TavernCore(0,0, 1, 1, 2, Controller.getChosenField(), 0));
        Button btnCastle = createBuildingButton("/textures/btnCastle.png", () ->
                new CastleCore(0,0, 1, 1, 8, Controller.getChosenField(), 0));

        //добавляем кнопки на панель
        toolsPane.getItems().addAll(btnHouse, btnCasern, btnTavern, btnCastle);

        //задаем параметры элементов панели ресурсов
        gold.set(fieldCore.getGold());
        HBox resourceGold = createResource(new Label("Gold"),"/textures/gold.png", gold);
        force.set(fieldCore.getForce());
        HBox resourceForce = createResource(new Label("Force"),"/textures/force.png", force);
        force.set(fieldCore.getPeople());
        HBox resourcePeople = createResource(new Label("People"),"/textures/people.png", people);

        resourcesPane.getItems().addAll(resourceGold, resourceForce, resourcePeople);

        //задаем параметры для панели здания
        lblBuildingName = new Label("Building");
        HBox hBoxCost = createResource(new Label("Gold"), "/textures/gold.png", goldCost);
        HBox hBoxPeople = createResource(new Label("People"), "/textures/people.png", peopleCost);
        Button btnDestroy = new Button("destroy");
        btnDestroy.setMinWidth(btnDestroy.getPrefWidth());
        btnDestroy.setOnAction(event -> {
            Controller.destroyBuilding();
            Controller.focusOnField();
        });
        buildingPane.getChildren().addAll(lblBuildingName, hBoxCost, hBoxPeople, btnDestroy);
        buildingPane.setFocusTraversable(false);
        buildingPane.setVisible(false);

        //добавляем объекты
        //p1 нужен для сдвига панели здания
        Pane p1 = new Pane();
        p1.prefWidthProperty().bind(fieldPane.widthProperty().subtract(buildingPane.widthProperty()));
        p1.setVisible(false);
        gridPane.setPickOnBounds(false);
        gridPane.add(p1, 0,0);
        gridPane.add(buildingPane, 1,0);
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

    public static void updateResources(int newGold, int newForce, int newPeople) {
        gold.set(newGold);
        force.set(newForce);
        people.set(newPeople);
    }
    public static double getX() { return gameWindow.getX(); }
    public static double getY() { return gameWindow.getY(); }

    public static void showBuildingInfo (String name, int price, int peopleChange) {
        lblBuildingName.setText(name);
        goldCost.set(price);
        peopleCost.set(peopleChange);
        buildingPane.setVisible(true);
    }
    public static void hideBuildingInfo () {
        buildingPane.setVisible(false);
    }

    //вспомогательные методы
    private static Button createBuildingButton (String respath, Supplier sup) {
        if (!(sup.get() instanceof AbstractBuilding)){
            System.err.println("wrong Supplier.get() implementation!" + System.lineSeparator() +
                    " sup should return instance of AbstractBuilding implementation");
            return null;
        }
        InputStream in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgBuildingBtn = new ImageView(new Image(in));
        imgBuildingBtn.setFitWidth(mainWindowHeight / 10 );
        imgBuildingBtn.setFitHeight(mainWindowHeight / 10 );
        Button btnBuilding = new Button("", imgBuildingBtn);
        btnBuilding.setId("control_button");
        btnBuilding.setFocusTraversable(false);
        btnBuilding.setOnAction(event -> {
            Controller.pressOnBuildingButton(Controller.getChosenField(),(AbstractBuilding) sup.get());
        });
        return btnBuilding;
    }

    private static HBox createResource(Label lblName, String respath, IntegerProperty resource) {
        HBox resourceGold = new HBox();
        resourceGold.setSpacing(10);
        InputStream in = GameApplication.class.getResourceAsStream(respath);
        ImageView imgGoldPic = new ImageView(new Image(in));
        imgGoldPic.setFitWidth(mainWindowHeight / 20 );
        imgGoldPic.setFitHeight(mainWindowHeight / 20 );
        Label lblCount = new Label();
        lblCount.textProperty().bind(resource.asString());
        resourceGold.getChildren().addAll(lblName, lblCount, imgGoldPic);
        return resourceGold;
    }
}
