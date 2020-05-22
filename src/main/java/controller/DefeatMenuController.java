package controller;
import render.StagesManager;

import java.io.IOException;

public class DefeatMenuController {

    public void pressOnBtnMenu() throws IOException {
        StagesManager.MainMenu.open();
        StagesManager.GameApp.close();
    }

    public static void move(double x, double y) {
        StagesManager.DefeatMenu.setX(x - StagesManager.DefeatMenu.getWidth() / 2);
        StagesManager.DefeatMenu.setY(y - StagesManager.DefeatMenu.getHeight() / 2);
    }
}