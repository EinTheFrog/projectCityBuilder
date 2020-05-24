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
            StagesManager.Menu.close();
        }
    }

    public static void move(double x, double y) {
        StagesManager.Menu.setCoords(x , y);
    }
}
