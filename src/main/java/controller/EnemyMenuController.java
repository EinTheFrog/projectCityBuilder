package controller;

import render.StagesManager;

public class EnemyMenuController {
    public void pressOnBtnPay() {
        GameAppController.gameResources.payEnemies();
        close();
    }

    public void pressOnBtnFight() {
        boolean userWon = GameAppController.gameResources.beatEnemies();
        if (!userWon) {
            GameAppController.chosenField.removeRandomBuilding();
        }
        StagesManager.ResultMenu.open(userWon);
        StagesManager.EnemyMenu.close();
    }

    public void close() {
        StagesManager.EnemyMenu.close();
        StagesManager.GameApp.getController().setChoosingMod();
        StagesManager.GameApp.getController().resume();
        if (GameAppController.gameResources.userLost()) StagesManager.DefeatMenu.open();
    }

    public static void move (double x, double y) {
        StagesManager.EnemyMenu.setCoords(x, y);
    }

}
