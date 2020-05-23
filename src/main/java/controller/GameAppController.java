package controller;

import core.Resources;
import core.buildings.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import render.StagesManager;
import view.FieldView;
import view.buildings.AbstractBuildingView;

import java.io.InputStream;
import java.net.URL;
import java.util.*;


public class GameAppController implements Initializable {
    @FXML
    Label lblGold, lblGoldIncome, lblForce, lblForceIncome, lblPeople, lblTime, lblInfo, lblGoldCost, lblPeopleCost, lblForceCost;
    @FXML
    public Pane fieldPane, p1, p2;
    @FXML
    ImageView imgInfo;
    @FXML
    VBox vBoxInfo;
    @FXML
    Parent root;
    @FXML
    private Button btnHouse, btnCasern, btnTavern, btnCastle;
    @FXML
    private Screen screen;


    private Set<Button> buttonSet;
    //множества для контроля за зажатыми клавишами,
    //curBtnPressed отвечает за клавиши, что уже были зажаты
    //newBtnPressed отвечает за клавиши, которые пользователь нажал
    private static final EnumSet <KeyboardButtons> curBtnPressed = EnumSet.noneOf(KeyboardButtons.class);
    private static final EnumSet <KeyboardButtons> newBtnPressed = EnumSet.noneOf(KeyboardButtons.class);
    public static Mod mod = Mod.CHOOSING_MOD; //при открытии игрвого окна мы находимся в режиме выбора
    //поле является static, т.к у нас может быть только одно игрвое поле, накотором находится игрок
    static FieldView chosenField;
    static Resources gameResources;

    private Timer timer = new Timer(true);
    private TimerTask timerTask;
    private TimerTask timerMoveTask;
    private boolean timerIsRunning;

    private final int FIELD_SIZE = 20;
    private final double INDENT = 50;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //коррекция параметров, необходимая для того,
        //чтобы панель информации о здании находилась в левом верхнем углу
        //p1 и p2 являются вспомогательными невидимыми панелями
        HBox.setHgrow(p1, Priority.ALWAYS);
        VBox.setVgrow(p2,Priority.ALWAYS);
        //добавление обработчика события для закрытия меню при щелчке
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, this::closeMenuOnClick);

        buttonSet = new HashSet<>();
        buttonSet.add(btnHouse);
        buttonSet.add(btnCasern);
        buttonSet.add(btnTavern);
        buttonSet.add(btnCastle);

        //создание игрвого поля
        //вручную высчитвываем размер панели, на которой находится поле, потому что панель еще не отрисована
        // и мы не можем использовать getWidth()
        final double paneWidth = screen.getVisualBounds().getWidth() * 5/9;
        final double fieldSide = ( paneWidth  / 2 - INDENT) * Math.cos(Math.PI / 6);
        gameResources = new Resources();
        chosenField = Creator.createField(FIELD_SIZE, INDENT, fieldSide,
                fieldPane.widthProperty(), fieldPane.heightProperty(), this, gameResources);
        Creator.createCellsForField(chosenField);
        gameResources.chooseField(chosenField.getCore());
        updateResources();
        updateIncome();
        updateTime();

        timerIsRunning = false;
        startTimer();

        fieldPane.getChildren().add(chosenField);
        fieldPane.addEventHandler(ScrollEvent.SCROLL, event -> chosenField.zoom(event.getDeltaY()));
    }

    //методы для кнопок
    public void pressOnHouseButton() {
        setBuildingMod();
        HouseCore houseCore = new HouseCore(0,0, 1, 1, 2);
        createGhost(houseCore);
    }
    public void pressOnCasernButton() {
        setBuildingMod();
        CasernCore casernCore = new CasernCore(0,0, 1, 1, 2);
        createGhost(casernCore);
    }
    public void pressOnTavernButton() {
        setBuildingMod();
        TavernCore tavernCore = new TavernCore(0,0, 1, 1, 2);
        createGhost(tavernCore);
    }
    public void pressOnCastleButton() {
        setBuildingMod();
        CastleCore castleCore = new CastleCore(0,0, 1, 1, 6);
        createGhost(castleCore);
    }
    public void pressOnBtnDestroy() {
        chosenField.removeBuilding(chosenField.getChosenBuilding());
        chosenField.setChosenBuilding(null);
    }

    /**
     * Метод для отображения призрака здания при строительстве.
     * Добавляет призрак здания на поле, а также выводит информацию о выбранном типе здания в панель информации о здании
     * @param buildingCore тип выбранного здания
     */
    private void createGhost(AbstractBuilding buildingCore) {
        AbstractBuildingView buildingView = Creator.createBuildingGhost(chosenField, buildingCore);
        lblInfo.setText(buildingCore.getName());
        setInfo(buildingView);
        showInfo();
    }

    //методы для обновления текста в метках
    public void updateTime() {
        lblTime.setText(String.valueOf(gameResources.getTime()));
    }

    public void updateResources() {
        lblGold.setText(String.valueOf(gameResources.getGold()));
        lblForce.setText(String.valueOf(gameResources.getForce()));
        lblPeople.setText(String.valueOf(gameResources.getPeople()));
    }

    public void updateIncome() {
        final char goldSign = gameResources.getGoldIncome() >= 0? '+': '-';
        lblGoldIncome.setText("(" + goldSign + gameResources.getGoldIncome() + ")");
        final char forceSign = gameResources.getForceIncome() >= 0? '+': '-';
        lblForceIncome.setText("(" + forceSign + gameResources.getForceIncome() + ")");
    }

    public void showInfo() {
        vBoxInfo.setVisible(true);
    }

    public void hideInfo() {
        vBoxInfo.setVisible(false);
    }

    public void setInfo(AbstractBuildingView buildingView) {
        AbstractBuilding building = buildingView.getCore();
        hideInfo();
        hideInfo();
        InputStream in = GameAppController.class.getResourceAsStream(buildingView.getImgPath());
        Image img = new Image(in);
        imgInfo.setImage(img);
        String goldProfitTxt = building.getGoldProfit() > 0? "+" + building.getGoldProfit():
                String.valueOf(building.getGoldProfit());
        lblGoldCost.setText(building.getGoldCost() + "(" + goldProfitTxt + ")");
        lblForceCost.setText(String.valueOf(building.getForceProfit()));
        lblPeopleCost.setText(String.valueOf(building.getPeopleChange()));
        showInfo();
    }

    //методы для работы с таймером
    private void startTimer() {
        timerIsRunning = true;
        timerMoveTask = new TimerTask() {
            @Override
            public void run() {
                double dy = 0;
                double dx = 0;
                for (KeyboardButtons key: curBtnPressed) {
                    dx += key.dx;
                    dy += key.dy;
                }
                chosenField.move(dx, dy);
            }
        };
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    boolean enemiesCame = gameResources.changeTime(500);
                    updateResources();
                    updateTime();
                    if (enemiesCame) showEnemy();
                });
            }
        };
        timer.schedule(timerMoveTask, 0, 20);
        timer.schedule(timerTask, 500, 500);
    }

    private void stopTimer() {
        timerIsRunning = false;
        timerMoveTask.cancel();
        timerTask.cancel();
    }

    public void resume() {
        if (!timerIsRunning) startTimer();
    }

    public void pause() {
        if (timerIsRunning) stopTimer();
    }

    //методы для обработки информации о нажатых клавишах
    public void keyPressed(KeyCode code) {
        boolean playerMovesCam = false;
        switch (code) {
            case W:
                newBtnPressed.add(KeyboardButtons.W);
                //говорим, что игрок двигает камеру
                playerMovesCam = true;
                break;
            case A:
                newBtnPressed.add(KeyboardButtons.A);
                playerMovesCam = true;
                break;
            case D:
                newBtnPressed.add(KeyboardButtons.D);
                playerMovesCam = true;
                break;
            case S:
                newBtnPressed.add(KeyboardButtons.S);
                playerMovesCam = true;
                break;
            case ESCAPE:
                switch (mod) {
                    case CHOOSING_MOD:
                        if (chosenField.getChosenBuilding() == null) {
                            setMenuMod();
                            StagesManager.Menu.open();
                        }
                        else chosenField.setChosenBuilding(null); break;
                    case BUILDING_MOD: setChoosingMod(); break;
                    case MENU_MOD: StagesManager.Menu.close(); break;
                }
                break;
        }
        //если игрок двигает камеру, то вызываем метод для перемещения камеры
        if (playerMovesCam) startCameraMovement();
    }

    public void keyReleased(KeyCode code) {
        switch (code) {
            case W:
                //обнуляем расстояние, нак оторое камера должна пермещаться по оси OY
                //удаляем кнопку из списка зажатых сейчас
                newBtnPressed.remove(KeyboardButtons.W);
                break;
            case A:
                newBtnPressed.remove(KeyboardButtons.A);
                break;
            case D:
                newBtnPressed.remove(KeyboardButtons.D);
                break;
            case S:
                newBtnPressed.remove(KeyboardButtons.S);
                break;
        }
        stopCameraMovement();
    }

    //методы для перемещения камеры
    private void startCameraMovement() {
        curBtnPressed.addAll(newBtnPressed);
    }

    private void stopCameraMovement() {
        curBtnPressed.clear();
        curBtnPressed.addAll(newBtnPressed);
    }

    /**
     * метод для закрытия PopUp меню при щелчке внутри игрвого окна
     * @param event
     */
    public void closeMenuOnClick(MouseEvent event) {
        if (mod == Mod.MENU_MOD) {
            if (event.getEventType() == MouseEvent.MOUSE_CLICKED || event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                resume();
                StagesManager.Menu.close();
            }
            event.consume();
        }
        if (mod == Mod.BLOCKED_MOD) event.consume();
    }

    //методы для выставления режима взаимедействия с пользователем
    public void setChoosingMod() {
        for (Button btn: buttonSet) btn.setDisable(false);
        hideInfo();
        mod = Mod.CHOOSING_MOD;
        chosenField.setBuildingMod(false);
        chosenField.setChosenBuilding(null);
    }

    public void setBuildingMod() {
        hideInfo();
        mod = Mod.BUILDING_MOD;
        chosenField.setBuildingMod(true);
    }

    public void setMenuMod() {
        for (Button btn: buttonSet) btn.setDisable(true);
        pause();
        mod = Mod.MENU_MOD;
    }

    public void setBlockedMod() {
        pause();
        chosenField.setChosenBuilding(null);
        for (Button btn: buttonSet) btn.setDisable(true);
        mod = Mod.BLOCKED_MOD;
    }

    /**
     * метод для открытия окна коче
     */
    public void showEnemy() {
        setBlockedMod();
        StagesManager.EnemyMenu.open();
    }

    public Mod getMod() {
        return mod;
    }
}
