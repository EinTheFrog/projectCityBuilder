package render;

import controller.GameAppController;
import controller.MenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class Menu {
    private static Popup menuPopup;
    private static MenuController myController;
    public static boolean isOpen = false;

    public static void open () {
        isOpen = true;
        GameAppController.setMenuMod();
        Stage owner = GameApp.gameStage;

        if (menuPopup != null) {
            menuPopup.show(owner);
            return;
        }
        menuPopup = new Popup();
        //задаем начальные элементы и параметры для них
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = Menu.class.getResource("/Menu.fxml");
        loader.setLocation(xmlUrl);
        try {
            Parent root = loader.load();
            menuPopup.getContent().add(root);
            myController = loader.getController();
            menuPopup.show(owner);
            myController.move(GameApp.getX() + GameApp.getWidth() / 2, GameApp.getY() + GameApp.getHeight() / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close () {
        if (menuPopup != null) {
            isOpen = false;
            GameAppController.setChoosingMod();
            menuPopup.hide();
        }
    }

    public static MenuController getController() {
        return myController;
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
