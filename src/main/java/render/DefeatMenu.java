package render;

import controller.DefeatMenuController;
import controller.GameAppController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class DefeatMenu {
    private static Popup defeatPopup;
    private static DefeatMenuController myController;
    public static boolean isOpen = false;

    public static void open() {
        isOpen = true;
        GameAppController.setMenuMod();
        Stage owner = GameApp.gameStage;

        if (defeatPopup != null) {
            defeatPopup.show(owner);
            return;
        }
        defeatPopup = new Popup();
        //задаем начальные элементы и параметры для них
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = Menu.class.getResource("/DefeatMenu.fxml");
        loader.setLocation(xmlUrl);
        try {
            Parent root = loader.load();
            defeatPopup.getContent().add(root);
            myController = loader.getController();
            defeatPopup.show(owner);
            myController.move(GameApp.getX() + GameApp.getWidth() / 2, GameApp.getY() + GameApp.getHeight() / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DefeatMenuController getController() {
        return myController;
    }

    public static void setX(double x) {
        defeatPopup.setX(x);
    }

    public static void setY(double y) {
        defeatPopup.setY(y);
    }

    public static double getWidth() {
        return defeatPopup.getWidth();
    }

    public static double getHeight() {
        return defeatPopup.getHeight();
    }
}