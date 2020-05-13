package render;

import controller.Controller;
import controller.GameApplicationController;
import controller.MainMenuController;
import core.FieldCore;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class GameApp {
    public static final Rectangle2D ScreenSize = Screen.getPrimary().getBounds();
    public static final double CENTRAL_PANE_WIDTH = ScreenSize.getWidth() / 2;
    public static final double CENTRAL_PANE_HEIGHT = ScreenSize.getHeight() / 2;
    public static final int FIELD_SIZE = 20;
    public static final double INDENT = 50;
    public static final double PANE_WIDTH = CENTRAL_PANE_WIDTH - 2 * INDENT;
    public static final double PANE_SIDE = PANE_WIDTH / (2 * Math.cos(Math.PI / 6));
    public static final double PANE_HEIGHT = PANE_SIDE * Math.sin(Math.PI / 6) * 2;
    public static final double CELL_SIDE = PANE_SIDE / FIELD_SIZE;

    public static Stage gameStage;
    private static GameApplicationController controller;

    public static void open () throws IOException {
        //создаем окно, сцену, панель и узлы
        gameStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = GameApp.class.getResource("/gameApplication.fxml");
        loader.setLocation(xmlUrl);
        try {
            Parent root = loader.load();
            gameStage.setScene(new Scene(root));
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
        gameStage.show();

        gameStage.xProperty().addListener(((observable, oldValue, newValue) -> {
            if (Menu.isOpen) Menu.getController().move(getX() + getWidth() / 2, getY() + getHeight() / 2);
            if (EnemyMenu.isOpen) EnemyMenu.getController().move(getX() + getWidth() / 2, getY() + getHeight() / 2);
        }));

        gameStage.yProperty().addListener(((observable, oldValue, newValue) -> {
            if (Menu.isOpen) Menu.getController().move(getX() + getWidth() / 2, getY() + getHeight() / 2);
            if (EnemyMenu.isOpen)EnemyMenu.getController().move(getX() + getWidth() / 2, getY() + getHeight() / 2);
        }));

        controller = loader.getController();
    }

    public static void close () {
        gameStage.close();
    }

    public static GameApplicationController getController() {
        return controller;
    }

    public static double getX() {
        return gameStage.getX();
    }
    public static double getY() {
        return gameStage.getY();
    }
    public static double getWidth() {
        return gameStage.getWidth();
    }
    public static double getHeight() {
        return gameStage.getHeight();
    }
}
