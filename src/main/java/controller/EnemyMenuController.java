package controller;

import popups.DefeatMenu;
import popups.EnemyMenu;
import popups.ResultMenu;
import stages.GameApp;

public class EnemyMenuController {
    public void pressOnBtnPay() {
        GameApp.getInstance().getController().gameResources.payEnemies();
        close();
    }

    public void pressOnBtnFight() {
        boolean userWon = GameApp.getInstance().getController().gameResources.beatEnemies();
        if (!userWon) {
            GameApp.getInstance().getController().chosenField.removeRandomBuilding();
        }
        ResultMenu.getInstance().open(userWon);
        EnemyMenu.getInstance().close();
    }

    public void close() {
        EnemyMenu.getInstance().close();
        GameApp.getInstance().getController().setChoosingMod();
        GameApp.getInstance().getController().resume();
        if (GameApp.getInstance().getController().gameResources.userLost()) DefeatMenu.getInstance().open();
    }

}
