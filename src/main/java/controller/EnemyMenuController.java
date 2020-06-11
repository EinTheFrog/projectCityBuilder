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
        ResultMenu.getInstance().open(userWon);
        EnemyMenu.getInstance().close();
    }

    public void close() {
        EnemyMenu.getInstance().close();
        GameApp.getInstance().getController().setChoosingMod();
        GameApp.getInstance().getController().resume();
        if (GameAppController.gameResources.userLost()) DefeatMenu.getInstance().open();
    }

}
