package controller;

import render.GameApp;
import render.MainMenu;

/**
 * класс-контроллер для главного меню
 * определяет методы, срабатывающие при нажатии на кнопки
 */
public class MainMenuController {

    public void btnStartAction() {
        GameApp.open();
        MainMenu.close();
    }

    public void btnQuitAction() {
        MainMenu.close();
    }
}
