package controller;
import popups.DefeatMenu;
import stages.GameApp;
import stages.MainMenu;

import java.io.IOException;

public class DefeatMenuController {

    public void pressOnBtnMenu() {
        MainMenu.open();
        GameApp.close();
    }

    public static void move(double x, double y) {
        DefeatMenu.setCoords(x , y);
    }
}