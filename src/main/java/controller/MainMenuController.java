package controller;

import render.StagesManager;

/**
 * класс-контроллер для главного меню
 * определяет методы, срабатывающие при нажатии на кнопки
 */
public class MainMenuController {

    public void btnStartAction() {
        StagesManager.GameApp.open();
        StagesManager.MainMenu.close();
    }

    public void btnQuitAction() {
        StagesManager.MainMenu.close();
    }
}
