package render;

import controller.EnemyMenuController;
import controller.GameAppController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.net.URL;

public class EnemyMenu {
    private static Popup enemyPopup;
    private static EnemyMenuController myController;
    public static boolean  isOpen = false;

    public static void open () {
        isOpen = true;
        GameAppController.setChoosingMod();
        Stage owner = GameApp.gameStage;

        if (enemyPopup != null) {
            enemyPopup.show(owner);
            return;
        }
        enemyPopup = new Popup();
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = GameApp.class.getResource("/EnemyMenu.fxml");
        loader.setLocation(xmlUrl);
        try {
            Parent root = loader.load();
            enemyPopup.getContent().add(root);
            enemyPopup.hideOnEscapeProperty().setValue(false);
            enemyPopup.show(owner);
            myController = loader.getController();
            myController.move(GameApp.getX() + enemyPopup.getWidth() / 2, GameApp.getY() + enemyPopup.getHeight() / 2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public static void close () {
        if (enemyPopup != null) {
            isOpen = false;
            GameAppController.setChoosingMod();
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

    public static EnemyMenuController getController() {
        return myController;
    }

}
