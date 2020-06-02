package controller;
import popups.DefeatMenu;
import stages.GameApp;
import stages.MainMenu;

import java.io.IOException;

public class DefeatMenuController {

    public void pressOnBtnMenu() throws IOException {
        MainMenu.open();
        GameApp.close();
    }

    public static void move(double x, double y) {
        DefeatMenu.setCoords(x , y);
    }
}