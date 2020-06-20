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
        MainMenu.getInstance().open();
        GameApp.getInstance().close();
        GameApp.deleteInstance();
    }

    public void pressESC(KeyEvent e) {
        if (e.getCode() == KeyCode.ESCAPE) {
            Menu.getInstance().close();
        }
    }
}
