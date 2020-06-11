package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import popups.DefeatMenu;
import popups.ResultMenu;


public class ResultMenuController {
    @FXML
    private Label txtResult;

    public void pressOnBtnResume() {
        close();
    }

    public void setText(Boolean userWon) {
        if (userWon) txtResult.setText("You won!");
        else txtResult.setText("You lose");
    }

    public void pressESC(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            ResultMenu.getInstance().close();
        }
    }

    public void close() {
        ResultMenu.getInstance().close();
        if (GameAppController.gameResources.userLost()) DefeatMenu.getInstance().open();
    }
}
