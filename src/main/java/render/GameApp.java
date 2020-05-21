package render;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;

/**
 * класс для игрвого окна приложения, в котором пользователь и играет
 */
public class GameApp {
    //константы
    public static final Color SPACE_COLOR = Color.rgb(0, 0, 90, 1);
    public static final Rectangle2D ScreenSize = Screen.getPrimary().getBounds();
    public static final double CENTRAL_PANE_WIDTH = ScreenSize.getWidth() / 2;
    public static final double CENTRAL_PANE_HEIGHT = ScreenSize.getHeight() / 2;
    public static final int FIELD_SIZE = 20;
    public static final double INDENT = 50;
    public static final double FIELD_WIDTH = CENTRAL_PANE_WIDTH - 2 * INDENT;
    public static final double FIELD_SIDE = FIELD_WIDTH / (2 * Math.cos(Math.PI / 6));
    public static final double FIELD_HEIGHT = FIELD_SIDE * Math.sin(Math.PI / 6) * 2;
    public static final double CELL_SIDE = FIELD_SIDE / FIELD_SIZE;

    //
    private final static Timer timer = Creator.createTimer();
    private final static Stage gameStage = new Stage();
    private static GameAppController myController;

    /**
     * метод для создания и открытия игровго окна
     * @throws IOException
     */
    public static void open () {
        //создаем окно и корневой узел с помощью fxml
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = GameApp.class.getResource("/GameApp.fxml");
        loader.setLocation(xmlUrl);
        try {
            Parent root = loader.load();
            gameStage.setScene(new Scene(root));
            myController = loader.getController();
            gameStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //привязываем координаты всех PopUp окон к координатам этого окна
        gameStage.xProperty().addListener(((observable, oldValue, newValue) -> moveMenus()));

        gameStage.yProperty().addListener(((observable, oldValue, newValue) -> moveMenus()));
    }

    private static void moveMenus() {
        if (Menu.isOpen) MenuController.move(getX() + getWidth() / 2, getY() + getHeight() / 2);
        if (EnemyMenu.isOpen)EnemyMenuController.move(getX() + getWidth() / 2, getY() + getHeight() / 2);
        if (DefeatMenu.isOpen) DefeatMenuController.move(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    /**
     * метод для закрытия окна
     */
    public static void close () {
        gameStage.close();
    }

    /**
     * метод для получения доступа к контроллеру игрового окна
     */
    public static GameAppController getController() {
        return myController;
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

    public static Timer getTimer() {
        return timer;
    }

    public static Stage getStage() {
        return gameStage;
    }
}
