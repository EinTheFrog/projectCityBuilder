package controller;

import core.Economy;
import core.FieldCore;
import core.buildings.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import render.GameApp;
import render.Menu;
import view.CellView;
import view.FieldView;

import java.io.InputStream;
import java.net.URL;
import java.util.*;


public class GameApplicationController implements Initializable {
    @FXML
    Label lblGold, lblGoldIncome, lblForce, lblForceIncome, lblPeople, lblTime, lblInfo, lblGoldCost, lblPeopleCost, lblForceCost;
    @FXML
    public Pane fieldPane, p1, p2;
    @FXML
    ImageView imgInfo;
    @FXML
    VBox vBoxInfo;


    private static EnumSet <KeyboardButtons> curBtnPressed = EnumSet.noneOf(KeyboardButtons.class);
    private static EnumSet <KeyboardButtons> newBtnPressed = EnumSet.noneOf(KeyboardButtons.class);
    public static Mod mod = Mod.CHOOSING_MOD;
    public static final Color SPACE_COLOR = Color.rgb(0, 0, 90, 1);
    private FieldCore chosenFieldCore;

    public void pressOnHouseButton() {
        hideInfo();
        HouseCore houseCore = new HouseCore(0,0, 1, 1, 2);
        //Controller.pressOnBuildingButton(Controller.getChosenField(), house);
        lblInfo.setText(houseCore.getName());

        String respath = "/textures/house.png";
        InputStream in = GameApp.class.getResourceAsStream(respath);
        Image img = new Image(in);
        imgInfo.setImage(img);
        setInfo(houseCore, respath);
        showInfo();
        chosenFieldCore.addGhost(houseCore);
    }
    public void pressOnCasernButton() {
        hideInfo();
        CasernCore casernCore = new CasernCore(0,0, 1, 1, 2);
        //Controller.pressOnBuildingButton(Controller.getChosenField(), house);
        lblInfo.setText(casernCore.getName());

        String respath = "/textures/casern.png";
        InputStream in = GameApp.class.getResourceAsStream(respath);
        Image img = new Image(in);
        imgInfo.setImage(img);
        setInfo(casernCore, respath);
        showInfo();
        chosenFieldCore.addGhost(casernCore);
    }
    public void pressOnTavernButton() {
        hideInfo();
        TavernCore tavernCore = new TavernCore(0,0, 1, 1, 2);
        //Controller.pressOnBuildingButton(Controller.getChosenField(), house);
        lblInfo.setText(tavernCore.getName());

        String respath = "/textures/tavern.png";
        InputStream in = GameApp.class.getResourceAsStream(respath);
        Image img = new Image(in);
        imgInfo.setImage(img);
        setInfo(tavernCore, respath);
        showInfo();
        chosenFieldCore.addGhost(tavernCore);
    }
    public void pressOnCastleButton() {
        hideInfo();
        CastleCore castleCore = new CastleCore(0,0, 1, 1, 6);
        //Controller.pressOnBuildingButton(Controller.getChosenField(), house);
        lblInfo.setText(castleCore.getName());

        String respath = "/textures/castle.png";
        InputStream in = GameApp.class.getResourceAsStream(respath);
        Image img = new Image(in);
        imgInfo.setImage(img);
        setInfo(castleCore, respath);
        showInfo();
        chosenFieldCore.addGhost(castleCore);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HBox.setHgrow(p1, Priority.ALWAYS);
        VBox.setVgrow(p2,Priority.ALWAYS);
        updateResources(Economy.getGold(), Economy.getForce(), Economy.getPeople());
        updateIncome(0,0);

        FieldCore fieldCore = Creator.createField();
        CellView.widthProperty.bind(fieldCore.getView().width.divide(GameApp.FIELD_SIZE));
        CellView.heightProperty.bind(fieldCore.getView().height.divide(GameApp.FIELD_SIZE));
        Creator.createCellsForField(fieldCore);
        chosenFieldCore = fieldCore;
        fieldPane.setBackground( new Background(new BackgroundFill(SPACE_COLOR, null, null)));
        fieldPane.getChildren().add(fieldCore.getView());

        Timer timer = Creator.createTimer();
        Creator.startTimer(fieldCore, timer, curBtnPressed, newBtnPressed);

        fieldPane.addEventHandler(ScrollEvent.SCROLL, event -> {
            fieldCore.getView().zoom(event.getDeltaY());
        });
    }

    public static void keyPressed(KeyCode code) {
        boolean playerMovesCam = false;
        switch (code) {
            case W:
                //добавляем в мапу значение true для W, теперь мы знаем, что клавиша W уже нажата (1 будет споильзована
                // в дальнейшем для вычисления изменения положения камеры)
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
                    case CHOOSING_MOD: ; break;
                    case BUILDING_MOD: ; break;
                    case MENU_MOD: Menu.close(); break;
                }
                break;
        }
        //если игрок двигает камеру, то вызываем метод для перемещения камеры
        if (playerMovesCam) startCameraMovement();
    }

    public static void keyReleased(KeyCode code, FieldCore fieldCore) {
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

    private static void startCameraMovement() {
        curBtnPressed.addAll(newBtnPressed);
    }

    private static void stopCameraMovement() {
        curBtnPressed.clear();
        curBtnPressed.addAll(newBtnPressed);
    }

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

    public void setInfo(AbstractBuilding building, String respath) {
        hideInfo();
        InputStream in = GameApp.class.getResourceAsStream(respath);
        Image img = new Image(in);
        imgInfo.setImage(img);
        String goldProfitTxt = building.getGoldProfit() > 0? "+" + building.getGoldProfit(): String.valueOf(building.getGoldProfit());
        lblGoldCost.setText(building.getGoldCost() + "(" + goldProfitTxt + ")");
        lblForceCost.setText(String.valueOf(building.getForceProfit()));
        lblPeopleCost.setText(String.valueOf(building.getPeopleChange()));
        showInfo();
    }

    public void pressOnBtnDestroy() {
        //Controller.destroyBuilding(Controller.getChosenBuilding());
    }
}
