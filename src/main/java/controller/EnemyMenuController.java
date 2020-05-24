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
        StagesManager.EnemyMenu.setCoords(x, y);
    }

}
