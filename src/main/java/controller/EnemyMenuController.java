package controller;

import render.StagesManager;

public class EnemyMenuController {
    public void pressOnBtnPay() {
        GameAppController.gameResources.payEnemies();
        close();
    }

    public void pressOnBtnFight() {
        if (!GameAppController.gameResources.beatEnemies()) {
            GameAppController.chosenField.removeRandomBuilding();
        }
        close();
    }

    public void close() {
        StagesManager.EnemyMenu.close();
        if (GameAppController.gameResources.userLost()) StagesManager.DefeatMenu.open();
    }

    public static void move (double x, double y) {
        StagesManager.EnemyMenu.setX(x - StagesManager.EnemyMenu.getWidth() / 2);
        StagesManager.EnemyMenu.setY(y - StagesManager.EnemyMenu.getHeight() / 2);
    }

}
