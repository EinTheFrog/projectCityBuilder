package controller;
import stages.GameApp;
import stages.MainMenu;

public class DefeatMenuController {

    public void pressOnBtnMenu() {
        MainMenu.open();
        GameApp.close();
    }

}