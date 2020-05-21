package render;

import controller.MenuController;
import javafx.stage.Popup;
import javafx.stage.Stage;


public class Menu {
    private static Popup menuPopup;
    public static boolean isOpen = false;

    public static void open () {
        isOpen = true;
        GameApp.getController().setMenuMod();
        Stage owner = GameApp.getStage();
        if (menuPopup != null) {
            menuPopup.show(owner);
            return;
        }
        menuPopup = Helper.createPopup("/Menu.fxml");
        menuPopup.show(owner);
        MenuController.move(GameApp.getX() + GameApp.getWidth() / 2, GameApp.getY() + GameApp.getHeight() / 2);
    }

    public static void close () {
        if (menuPopup != null) {
            isOpen = false;
            GameApp.getController().setChoosingMod();
            menuPopup.hide();
        }
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
