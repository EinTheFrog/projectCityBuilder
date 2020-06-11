package controller;

import stages.GameApp;
import stages.MainMenu;

/**
 * класс-контроллер для главного меню
 * определяет методы, срабатывающие при нажатии на кнопки
 */
public class MainMenuController {

    public void btnStartAction() {
        GameApp.getInstance().open();
        MainMenu.close();
    }

    public void btnQuitAction() {
        MainMenu.close();
    }
}
