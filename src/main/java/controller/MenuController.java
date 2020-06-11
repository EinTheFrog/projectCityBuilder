package controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import popups.Menu;
import stages.GameApp;
import stages.MainMenu;

import java.io.IOException;

public class MenuController {
    public void pressOnBtnResume() {
        Menu.getInstance().close();
    }

    public void pressOnBtnMenu() {
        MainMenu.open();
        GameApp.close();
    }

    public void pressESC(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            Menu.getInstance().close();
        }
    }

   /* public static void move(double x, double y) {
        Menu.setCoords(x , y);
    }*/
}
