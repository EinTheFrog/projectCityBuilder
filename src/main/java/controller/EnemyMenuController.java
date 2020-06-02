package controller;

import popups.DefeatMenu;
import popups.EnemyMenu;
import popups.ResultMenu;
import stages.GameApp;

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
        ResultMenu.open(userWon);
        EnemyMenu.close();
    }

    public void close() {
        EnemyMenu.close();
        GameApp.getController().setChoosingMod();
        GameApp.getController().resume();
        if (GameAppController.gameResources.userLost()) DefeatMenu.open();
    }

    public static void move (double x, double y) {
        EnemyMenu.setCoords(x, y);
    }

}
