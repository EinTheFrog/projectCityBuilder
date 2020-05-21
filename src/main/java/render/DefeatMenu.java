package render;

import controller.DefeatMenuController;
import javafx.stage.Popup;
import javafx.stage.Stage;


public class DefeatMenu {
    private static Popup defeatPopup;
    public static boolean isOpen = false;

    public static void open() {
        isOpen = true;
        GameApp.getController().setBlockedMod();
        Stage owner = GameApp.getStage();
        if (defeatPopup != null) {
            defeatPopup.show(owner);
            return;
        }
        defeatPopup = Helper.createPopup("/DefeatMenu.fxml");
        defeatPopup.hideOnEscapeProperty().setValue(false);
        defeatPopup.show(owner);
        DefeatMenuController.move(GameApp.getX() + GameApp.getWidth() / 2, GameApp.getY() + GameApp.getHeight() / 2);
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