package render;

import controller.Controller;
import controller.MenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class Menu {
    //private static Stage owner = GameApplication.gameWindow;
    private static Popup menuPopup;
    private static Stage owner = GameApp.gameStage;
    private static MenuController controller;
    public static boolean isOpen = false;

    public static void open () {
        isOpen = true;
        if (menuPopup != null) {
            menuPopup.show(owner);
            return;
        }
        menuPopup = new Popup();
        //задаем начальные элементы и параметры для них
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = GameApp.class.getResource("/menu.fxml");
        loader.setLocation(xmlUrl);
        try {
            Parent vBox = loader.load();
            menuPopup.getContent().add(vBox);
            controller = loader.getController();
            menuPopup.show(owner);
            controller.move(GameApp.getX() + GameApp.getWidth() / 2, GameApp.getY() + GameApp.getHeight() / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close () {
        if (menuPopup != null) {
            isOpen = false;
            Controller.setChoosingMod();
            menuPopup.hide();
        }
    }

    public static MenuController getController() {
        return controller;
    }

    public static void setX(double x) {
        menuPopup.setX(x);
    }
    public static void setY(double y) {
        menuPopup.setY(y);
    }

    public static double getWidth() {
        return menuPopup.getWidth();
    }
    public static double getHeight() {
        return menuPopup.getHeight();
    }
}
