package controller;

import render.StagesManager;

import java.util.Random;

public class EnemyMenuController {
    public void pressOnBtnPay() {
        close();
        GameAppController.gameResources.payEnemies();
    }

    public void pressOnBtnFight() {
        close();
        if (!GameAppController.gameResources.beatEnemies()) {
            GameAppController.chosenField.removeRandomBuilding();
        }
    }

    public void close() {
        StagesManager.EnemyMenu.close();
    }

    public static void move (double x, double y) {
        StagesManager.EnemyMenu.setX(x - StagesManager.EnemyMenu.getWidth() / 2);
        StagesManager.EnemyMenu.setY(y - StagesManager.EnemyMenu.getHeight() / 2);
    }

}
