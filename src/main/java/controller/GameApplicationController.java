package controller;

import core.FieldCore;
import core.buildings.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import render.EnemyMenu;
import render.GameApp;
import render.Menu;

import java.net.URL;
import java.util.ResourceBundle;


public class GameApplicationController implements Initializable {
    @FXML
    Label lblGold, lblGoldIncome, lblForce, lblForceIncome, lblPeople, lblTime;
    @FXML
    Pane fieldPane;

    public void pressOnHouseButton() {
        Controller.pressOnBuildingButton(
                Controller.getChosenField(),new HouseCore(0,0, 1, 1, 2, Controller.getChosenField(), 0));
    }
    public void pressOnCasernButton() {
        Controller.pressOnBuildingButton(
                Controller.getChosenField(),new CasernCore(0,0, 1, 1, 2, Controller.getChosenField(), 0));
    }
    public void pressOnTavernButton() {
        Controller.pressOnBuildingButton(
                Controller.getChosenField(),new TavernCore(0,0, 1, 1, 2, Controller.getChosenField(), 0));
    }
    public void pressOnCastleButton() {
        Controller.pressOnBuildingButton(
                Controller.getChosenField(),new CastleCore(0,0, 1, 1, 6, Controller.getChosenField(), 0));
    }

/*    public void move() {
        Menu.getController().move(GameApp.getX(), GameApp.getY());
        Menu.getController().move(GameApp.getX(), GameApp.getY());
        EnemyMenu.move(GameApp.getX(), GameApp.getY());
    }*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        final Color cellBorderColor = Color.rgb(178, 178, 177 );
        final Color cellFillColor = Color.rgb(10, 106, 84);
        FieldCore fieldCore = new FieldCore(GameApp.FIELD_SIZE, GameApp.CELL_SIDE, GameApp.PANE_SIDE, cellBorderColor, cellFillColor, fieldPane, GameApp.INDENT);
        fieldCore.draw();
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
}
