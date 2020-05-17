package render;

import controller.Controller;
import controller.EnemyMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.net.URL;

public class EnemyMenu {
    private static Stage owner = GameApp.gameStage;
    private static Popup menuPopup = new Popup();
    private static EnemyMenuController myController;
    public static boolean  isOpen = false;

    public static void open () {
        isOpen = true;
        //Controller.setEnemyMod();

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = GameApp.class.getResource("/EnemyMenu.fxml");
        loader.setLocation(xmlUrl);
        try {
            Parent root = loader.load();
            menuPopup.getContent().add(root);
            menuPopup.hideOnEscapeProperty().setValue(false);
            menuPopup.show(owner);
            myController = loader.getController();
            myController.move(GameApp.getX() + GameApp.getWidth() / 2, GameApp.getY() + GameApp.getHeight() / 2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public static void close () {
        if (menuPopup != null) {
            isOpen = false;
            //Controller.setChoosingMod();
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

    public static EnemyMenuController getController() {
        return myController;
    }

}
