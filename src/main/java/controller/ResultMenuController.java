package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import render.StagesManager;


public class ResultMenuController {
    @FXML
    Label txtResult;

    public void pressOnBtnResume() {
        close();
    }

    public void setText(Boolean userWon) {
        if (userWon) txtResult.setText("You have won!");
        else txtResult.setText("You have lost");
    }
    public static void move(double x, double y) {
        StagesManager.ResultMenu.setCoords(x , y);
    }

    public void pressESC(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            StagesManager.ResultMenu.close();
        }
    }

    public void close() {
        StagesManager.ResultMenu.close();
        if (GameAppController.gameResources.userLost()) StagesManager.DefeatMenu.open();
    }
}
