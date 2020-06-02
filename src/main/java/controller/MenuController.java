package controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import popups.Menu;
import stages.GameApp;
import stages.MainMenu;

import java.io.IOException;

public class MenuController {
    public void pressOnBtnResume() {
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
            Menu.close();
        }
    }

    public static void move(double x, double y) {
        Menu.setCoords(x , y);
    }
}
