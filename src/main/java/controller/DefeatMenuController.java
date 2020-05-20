package controller;

import render.DefeatMenu;
import render.EnemyMenu;
import render.GameApp;
import render.MainMenu;

import java.io.IOException;

public class DefeatMenuController {

    public void pressOnBtnMenu() {
        try {
            MainMenu.open();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        GameApp.close();
    }

    public static void move(double x, double y) {
        DefeatMenu.setX(x - DefeatMenu.getWidth() / 2);
        DefeatMenu.setY(y - DefeatMenu.getHeight() / 2);
    }
}