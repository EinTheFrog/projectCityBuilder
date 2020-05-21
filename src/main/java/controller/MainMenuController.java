package controller;

import render.GameApp;
import render.MainMenu;

import java.io.IOException;

/**
 * класс-контроллер для главного меню
 * определяет методы, срабатывающие при нажатии на кнопки
 */
public class MainMenuController {

    public void btnStartAction() throws IOException {
        GameApp.open();
        MainMenu.close();
    }

    public void btnQuitAction() {
        MainMenu.close();
    }
}
