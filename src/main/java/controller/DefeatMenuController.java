package controller;
import render.StagesManager;

import java.io.IOException;

public class DefeatMenuController {

    public void pressOnBtnMenu() throws IOException {
        StagesManager.MainMenu.open();
        StagesManager.GameApp.close();
    }

    public static void move(double x, double y) {
        StagesManager.DefeatMenu.setCoords(x , y);
    }
}