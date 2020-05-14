package controller;

import core.FieldCore;
import core.buildings.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import render.EnemyMenu;
import render.GameApp;
import render.Menu;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;


public class GameApplicationController implements Initializable {
    @FXML
    Label lblGold, lblGoldIncome, lblForce, lblForceIncome, lblPeople, lblTime, lblInfo, lblGoldCost, lblPeopleCost, lblForceCost;
    @FXML
    public Pane fieldPane, p1, p2;
    @FXML
    ImageView imgInfo;
    @FXML
    VBox vBoxInfo;

    public void pressOnHouseButton() {
        hideInfo();
        HouseCore house = new HouseCore(0,0, 1, 1, 2, Controller.getChosenField(), 0);
        Controller.pressOnBuildingButton(Controller.getChosenField(), house);
        lblInfo.setText(house.getName());

        String respath = "/textures/house.png";
        InputStream in = GameApp.class.getResourceAsStream(respath);
        Image img = new Image(in);
        imgInfo.setImage(img);
        setInfo(house.getGoldCost(), house.getGoldProfit(), house.getForceProfit(), house.getPeopleChange(), "/textures/house.png");
        showInfo();
    }
    public void pressOnCasernButton() {
        hideInfo();
        CasernCore casern = new CasernCore(0,0, 1, 1, 2, Controller.getChosenField(), 0);
        Controller.pressOnBuildingButton(Controller.getChosenField(),casern);
        lblInfo.setText(casern.getName());
        setInfo(casern.getGoldCost(), casern.getGoldProfit(), casern.getForceProfit(), casern.getPeopleChange(), "/textures/casern.png");
        showInfo();
    }
    public void pressOnTavernButton() {
        hideInfo();
        TavernCore tavern = new TavernCore(0,0, 1, 1, 2, Controller.getChosenField(), 0);
        Controller.pressOnBuildingButton(Controller.getChosenField(),tavern);
        lblInfo.setText(tavern.getName());
        setInfo(tavern.getGoldCost(), tavern.getGoldProfit(), tavern.getForceProfit(), tavern.getPeopleChange(), "/textures/tavern.png");
        showInfo();
    }
    public void pressOnCastleButton() {
        hideInfo();
        CastleCore castle = new CastleCore(0,0, 1, 1, 6, Controller.getChosenField(), 0);
        Controller.pressOnBuildingButton(Controller.getChosenField(),castle);
        lblInfo.setText(castle.getName());
        setInfo(castle.getGoldCost(), castle.getGoldProfit(), castle.getForceProfit(), castle.getPeopleChange(), "/textures/castle.png");
        showInfo();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FieldCore fieldCore = new FieldCore(GameApp.FIELD_SIZE, GameApp.CELL_SIDE, GameApp.PANE_SIDE, GameApp.INDENT);
        fieldCore.getOutput().draw(fieldPane);
        fieldCore.createCells();
        Controller.chooseField(fieldCore);
        Controller.focusOnField();
        Controller.startTimer();

        fieldPane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            Controller.closeMenuOnClick(event);
        });

        fieldPane.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            double cursorOnFieldX = event.getX() - fieldCore.getX() * fieldCore.getScale();
            double cursorOnFieldY = event.getY() - fieldCore.getY() * fieldCore.getScale();
            Controller.moveCursor(cursorOnFieldX, cursorOnFieldY);
            event.consume();
        });

        fieldPane.addEventHandler(ScrollEvent.SCROLL, event -> {
            Controller.zoom(event.getDeltaY(), fieldCore);
        });
        HBox.setHgrow(p1, Priority.ALWAYS);
        VBox.setVgrow(p2,Priority.ALWAYS);
        updateResources(fieldCore.getGold(), fieldCore.getForce(), fieldCore.getPeople());
        updateIncome(0,0);
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

    public void setInfo(int goldCost, int goldProfit, int forceCost, int peopleCost, String respath) {
        hideInfo();
        InputStream in = GameApp.class.getResourceAsStream(respath);
        Image img = new Image(in);
        imgInfo.setImage(img);
        String goldProfitTxt = goldProfit > 0? "+" + goldProfit: String.valueOf(goldProfit);
        lblGoldCost.setText(goldCost + "(" + goldProfitTxt + ")");
        lblForceCost.setText(String.valueOf(forceCost));
        lblPeopleCost.setText(String.valueOf(peopleCost));
        showInfo();
    }

    public void pressOnBtnDestroy() {
        Controller.destroyBuilding(Controller.getChosenBuilding());
    }
}
