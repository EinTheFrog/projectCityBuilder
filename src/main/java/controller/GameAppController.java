package controller;

import core.Economy;
import core.FieldCore;
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
import render.EnemyMenu;
import render.GameApp;
import render.Menu;

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


    private Set<Button> buttonSet;
    //множества для контроля за зажатыми клавишами,
    //curBtnPressed отвечает за клавиши, что уже были зажаты
    //newBtnPressed отвечает за клавиши, которые пользователь нажал
    private static EnumSet <KeyboardButtons> curBtnPressed = EnumSet.noneOf(KeyboardButtons.class);
    private static EnumSet <KeyboardButtons> newBtnPressed = EnumSet.noneOf(KeyboardButtons.class);
    public static Mod mod = Mod.CHOOSING_MOD; //при открытии игрвого окна мы находимся в режиме выбора
    private static FieldCore chosenFieldCore; //поле является абстрактными,
    // т.к у нас может быть только одно игрвое поле, накотором находится игрок
    private static TimerTask timerTask;
    private static TimerTask timerMoveTask;

    /**
     * Метод для отображения призрака здания при строительстве.
     * Добавляет призрак здания на поле, а также выводит информацию о выбранном типе здания в панель информации о здании
     * @param buildingCore тип выбранного здания
     */
    private void createGhost(AbstractBuilding buildingCore) {
        lblInfo.setText(buildingCore.getName());
        chosenFieldCore.addGhost(buildingCore);
        setInfo(buildingCore);
        showInfo();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //коррекция параметров, необходимая для того,
        //чтобы панель информации о здании находилась в левом верхнем углу
        //p1 и p2 являются вспомогательными невидимыми панелями
        HBox.setHgrow(p1, Priority.ALWAYS);
        VBox.setVgrow(p2,Priority.ALWAYS);
        //добавление обработчика события для закрытия меню при щелчке
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            closeMenuOnClick(event);
        });

        buttonSet = new HashSet<>();
        buttonSet.add(btnHouse);
        buttonSet.add(btnCasern);
        buttonSet.add(btnTavern);
        buttonSet.add(btnCastle);

        //создание игрвого поля
        chosenFieldCore = Creator.createField();
        Creator.createCellsForField(chosenFieldCore);
        Economy.setStartParams();
        Economy.chooseField(chosenFieldCore);
        updateResources(Economy.START_GOLD, Economy.START_FORCE, Economy.START_PEOPLE);
        updateIncome(0, 0);
        updateTime(0);
        startTimer();

        fieldPane.setBackground( new Background(new BackgroundFill(GameApp.SPACE_COLOR, null, null)));
        fieldPane.getChildren().add(chosenFieldCore.getView());

        fieldPane.addEventHandler(ScrollEvent.SCROLL, event -> {
            chosenFieldCore.getView().zoom(event.getDeltaY());
        });
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
        chosenFieldCore.removeBuilding(chosenFieldCore.getChosenBuilding());
    }

    //методы для обновления текста в метках
    public void updateTime(int time) {
        lblTime.setText(String.valueOf(time));
    }

    public void updateResources(int gold, int force, int people) {
        lblGold.setText(String.valueOf(gold));
        lblForce.setText(String.valueOf(force));
        lblPeople.setText(String.valueOf(people));
    }

    public void updateIncome(int goldIncome, int forceIncome) {
        final char goldSign = goldIncome >= 0? '+': '-';
        lblGoldIncome.setText("(" + goldSign + goldIncome +")");
        final char forceSign = forceIncome >= 0? '+': '-';
        lblForceIncome.setText("(" + forceSign + forceIncome+")");
    }

    public void showInfo() {
        vBoxInfo.setVisible(true);
    }
    public void hideInfo() {
        vBoxInfo.setVisible(false);
    }

    public void setInfo(AbstractBuilding building) {
        hideInfo();
        hideInfo();
        InputStream in = GameApp.class.getResourceAsStream(building.getView().getImgPath());
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
        timerMoveTask = new TimerTask() {
            @Override
            public void run() {
                double dy = 0;
                double dx = 0;
                for (KeyboardButtons key: curBtnPressed) {
                    dx += key.dx;
                    dy += key.dy;
                }
                chosenFieldCore.getView().move(dx, dy);
            }
        };
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Economy.changeTime(500);
                });
            }
        };
        GameApp.getTimer().schedule(timerMoveTask, 0, 20);
        GameApp.getTimer().schedule(timerTask, 500, 500);
    }

    private void stopTimer() {
        timerMoveTask.cancel();
        timerTask.cancel();
    }

    public void resume() {
        startTimer();
    }

    public void pause() {
        stopTimer();
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
                        if (chosenFieldCore.getChosenBuilding() == null) Menu.open();
                        else chosenFieldCore.setChosenBuilding(null); break;
                    case BUILDING_MOD: setChoosingMod(); break;
                    case MENU_MOD: Menu.close(); break;
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
                Menu.close();
            }
            event.consume();
        }
        if (mod == Mod.BLOCKED_MOD) event.consume();
    }

    //методы для выставления режима взаимедействия с пользователем
    public void setChoosingMod() {
        for (Button btn: buttonSet) btn.setDisable(false);
        GameApp.getController().hideInfo();
        mod = Mod.CHOOSING_MOD;
        chosenFieldCore.setBuildingMod(false);
        chosenFieldCore.setChosenBuilding(null);
    }

    public void setBuildingMod() {
        hideInfo();
        mod = Mod.BUILDING_MOD;
        chosenFieldCore.setBuildingMod(true);
    }

    public void setMenuMod() {
        for (Button btn: buttonSet) btn.setDisable(true);
        pause();
        mod = Mod.MENU_MOD;
    }

    public void setBlockedMod() {
        pause();
        chosenFieldCore.setChosenBuilding(null);
        for (Button btn: buttonSet) btn.setDisable(true);
        mod = Mod.BLOCKED_MOD;
    }

    /**
     * метод для выбора здания для постройки
     * @param buildingCore
     */
    public void chooseBuilding(AbstractBuilding buildingCore) {
        hideInfo();
        chosenFieldCore.setChosenBuilding(buildingCore);
        showInfo();
    }

    /**
     * метод для открытия окна коче
     */
    public void showEnemy() {
        EnemyMenu.open();
    }

    public Mod getMod() {
        return mod;
    }
    public FieldCore getChosenFieldCore() {
        return chosenFieldCore;
    }
}
