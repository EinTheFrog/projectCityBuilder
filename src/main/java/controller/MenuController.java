package controller;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import render.StagesManager;

import java.io.IOException;

public class MenuController {
    @FXML
    VBox menuRoot;

    /*@Override
    public void initialize(URL location, ResourceBundle resources) {
        menuRoot.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) StagesManager.Menu.close();
        });
    }*/

    public void pressOnBtnResume() {
        StagesManager.Menu.close();
    }

    public void pressOnBtnMenu() {
        try {
            StagesManager.MainMenu.open();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        StagesManager.GameApp.close();
    }

    public void pressESC(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            StagesManager.GameApp.getController().resume();
            StagesManager.Menu.close();
        }
    }

    public static void move(double x, double y) {
        StagesManager.Menu.setX(x - StagesManager.Menu.getWidth() / 2);
        StagesManager.Menu.setY(y - StagesManager.Menu.getHeight() / 2);
    }
}
