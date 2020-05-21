package render;

import controller.EnemyMenuController;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class EnemyMenu {
    private static Popup enemyPopup;
    public static boolean  isOpen = false;

    public static void open () {
        isOpen = true;
        GameApp.getController().setBlockedMod();
        Stage owner = GameApp.getStage();
        if (enemyPopup != null) {
            enemyPopup.show(owner);
            return;
        }
        enemyPopup = Helper.createPopup("/EnemyMenu.fxml");
        enemyPopup.hideOnEscapeProperty().setValue(false);
        enemyPopup.show(owner);
        EnemyMenuController.move(GameApp.getX() + GameApp.getWidth() / 2, GameApp.getY() + GameApp.getHeight() / 2);
    }

    public static void close () {
        if (enemyPopup != null) {
            isOpen = false;
            GameApp.getController().setChoosingMod();
            enemyPopup.hide();
        }
    }

    public static void setX(double x) {
        enemyPopup.setX(x);
    }

    public static void setY(double y) {
        enemyPopup.setY(y);
    }

    public static double getWidth() {
        return enemyPopup.getWidth();
    }

    public static double getHeight() {
        return enemyPopup.getHeight();
    }

}
