package controller;

import render.GameApp;
import render.MainMenu;

import java.io.IOException;


public class MainMenuController {

    public void btnStartAction() throws IOException {
        GameApp.open();
        MainMenu.close();
    }
}
