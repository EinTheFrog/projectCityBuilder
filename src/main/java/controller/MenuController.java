package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import render.GameApp;
import render.MainMenu;
import render.Menu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    VBox menuRoot;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuRoot.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) Menu.close();
        });
    }

    public void pressOnBtnResume() {
        GameApp.getController().resume();
        Menu.close();
    }

    public void pressOnBtnMenu() {
        try {
            MainMenu.open();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        GameApp.close();
    }

    public void pressESC(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            GameApp.getController().resume();
            Menu.close();
        }
    }

    public static void move(double x, double y) {
        Menu.setX(x - Menu.getWidth() / 2);
        Menu.setY(y - Menu.getHeight() / 2);
    }
}
