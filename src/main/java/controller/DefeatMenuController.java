package controller;
import stages.GameApp;
import stages.MainMenu;

public class DefeatMenuController {

    public void pressOnBtnMenu() {
        MainMenu.getInstance().open();
        GameApp.getInstance().close();
        GameApp.deleteInstance();
    }

}