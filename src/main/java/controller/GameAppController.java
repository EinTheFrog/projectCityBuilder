package controller;

import core.GameResources;
import core.buildings.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import popups.EnemyMenu;
import popups.Menu;
import view.FieldView;
import view.buildings.AbstractBuildingView;

import java.io.InputStream;
import java.net.URL;
import java.util.*;


public class GameAppController implements Initializable {
    @FXML
    private Label lblGold, lblGoldIncome, lblForce, lblForceIncome, lblPeople, lblTime, lblInfo, lblGoldCost, lblPeopleCost, lblForceCost;
    @FXML
    public Pane fieldPane, p1, p2;
    @FXML
    private ImageView imgInfo;
    @FXML
    private VBox vBoxInfo;
    @FXML
    private Parent root;
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
    static GameResources gameResources;

    private Timeline gameLoop;

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

        buttonSet = new HashSet<>() {
            {
                add(btnHouse);
                add(btnCasern);
                add(btnTavern);
                add(btnCastle);
            }
        };

        //создание игрвого поля
        //вручную высчитвываем размер панели, на которой находится поле, потому что панель еще не отрисована
        // и мы не можем использовать getWidth()
        final double paneWidth = screen.getVisualBounds().getWidth() * 5/9;
        final double fieldSide = ( paneWidth  / 2 - INDENT) * Math.cos(Math.PI / 6);
        gameResources = new GameResources();
        chosenField = Creator.createField(FIELD_SIZE, INDENT, fieldSide,
                fieldPane.widthProperty(), fieldPane.heightProperty(), this, gameResources);
        gameResources.chooseField(chosenField.getCore());
        updateResources();

        fieldPane.getChildren().add(chosenField);
        fieldPane.addEventHandler(ScrollEvent.SCROLL, event -> chosenField.zoom(event.getDeltaY()));

        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(0.017), // 60 FPS
                e -> {
                    moveTask();
                    resourceTask();
                });
        gameLoop = new Timeline(keyFrame);
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    //методы для кнопок
    public void pressOnHouseButton() {
        setBuildingMod();
        HouseCore houseCore = new HouseCore(0,0, 2, 2);
        createGhost(houseCore);
    }
    public void pressOnCasernButton() {
        setBuildingMod();
        CasernCore casernCore = new CasernCore(0,0, 2, 2);
        createGhost(casernCore);
    }
    public void pressOnTavernButton() {
        setBuildingMod();
        TavernCore tavernCore = new TavernCore(0,0, 2, 2);
        createGhost(tavernCore);
    }
    public void pressOnCastleButton() {
        setBuildingMod();
        CastleCore castleCore = new CastleCore(0,0, 6, 6);
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

    /**
     * Метод, обновляющий текст в метках ресурсов
     */
    private void updateResources() {
        lblGold.setText(String.valueOf(gameResources.getGold()));
        lblForce.setText(String.valueOf(gameResources.getForce()));
        lblPeople.setText(String.valueOf(gameResources.getPeople()));
        lblTime.setText(String.valueOf(gameResources.getTime()));

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

    /**
     * Метод для показа панели информации о выбранном здании
     * @param buildingView
     */
    public void setInfo(AbstractBuildingView buildingView) {
        AbstractBuilding building = buildingView.getCore();
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

    private void moveTask() {
        double dy = 0;
        double dx = 0;
        for (KeyboardButtons key: curBtnPressed) {
            dx += key.dx;
            dy += key.dy;
        }
        chosenField.move(dx, dy);
    }

    private void resourceTask() {
        boolean enemiesCame = gameResources.changeTime(17);
        updateResources();
        if (enemiesCame) showEnemy();
    }


    /**
     * Метод, отменяющий процессы для таймера
     */
    private void stopTimer() {
        gameLoop.pause();
    }

    /**
     * Метод для запуска процессов для таймера, если они не запущены
     */
    public void resume() {
        gameLoop.play();
    }

    /**
     * Метод для запуска процессов для таймера, если они запущены
     */
    public void pause() {
        stopTimer();
    }

    /**
     * Метод, обрабатывающий нажатые клавиши и вызывающий соответствующие методы
     * @param code
     */
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
                            Menu.open();
                        }
                        else chosenField.setChosenBuilding(null); break;
                    case BUILDING_MOD: setChoosingMod(); break;
                    case MENU_MOD: Menu.close(); break;
                }
                break;
        }
        //если игрок двигает камеру, то вызываем метод для перемещения камеры
        if (playerMovesCam) updatePressedButtons();
    }

    /**
     * Метод, обрабатывающий нажатые клавиши и вызывающий соответствующие методы
     * @param code
     */
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
        updatePressedButtons();
    }

    /**
     * Метод для обновления нажатых клавиш
     */
    private void updatePressedButtons() {
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
                Menu.close();
            }
            event.consume();
        }
        if (mod == Mod.BLOCKED_MOD) event.consume();
    }

    /**
     * Метод, выключающий buildingMod для fieldView и  включащий  возможность наживать на кнопки
     */
    public void setChoosingMod() {
        for (Button btn: buttonSet) btn.setDisable(false);
        hideInfo();
        mod = Mod.CHOOSING_MOD;
        chosenField.setBuildingMod(false);
        chosenField.setChosenBuilding(null);
    }

    /**
     * Метод, прячущий панель информации и включающий buildingMod для fieldView
     */
    public void setBuildingMod() {
        hideInfo();
        mod = Mod.BUILDING_MOD;
        chosenField.setBuildingMod(true);
    }

    /**
     * Метод, останавливающий процесса таймера, отключащий возможность наживать на кнопки
     */
    public void setMenuMod() {
        for (Button btn: buttonSet) btn.setDisable(true);
        pause();
        mod = Mod.MENU_MOD;
    }

    /**
     * Метод, снимающий выделение со выбранного здания, отключащий возможность наживать на кнопки
     * и останавливающий процесса таймера
     */
    public void setBlockedMod() {
        pause();
        chosenField.setBuildingMod(false);
        chosenField.setChosenBuilding(null);
        for (Button btn: buttonSet) btn.setDisable(true);
        mod = Mod.BLOCKED_MOD;
    }

    /**
     * метод для открытия окно врагов
     */
    public void showEnemy() {
        setBlockedMod();
        EnemyMenu.open();
    }

    public Mod getMod() {
        return mod;
    }
}
