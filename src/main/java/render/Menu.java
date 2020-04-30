package render;

import controller.Controller;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Mod;


public class Menu {
    private static Stage owner = GameApplication.gameWindow;
    static Stage menuWindow;
    public static void open () {
        GameApplication.pause();
        //задаем начальные элементы и параметры для них
        menuWindow = new Stage();
        Scene menuScene;
        VBox vbox = new VBox();
        Button btnMenu = new Button("menu");
        Button btnResume = new Button("resume");
        vbox.getChildren().addAll(btnResume, btnMenu);
        vbox.setPrefSize(200, 300);
        menuScene = new Scene(vbox);
        menuScene.getStylesheets().add("RedLord.css");

        Controller.mod = Mod.MENU_MOD;
        //рендерим окно
        menuWindow.setScene(menuScene);
        menuWindow.setTitle("Menu");
        menuWindow.initStyle(StageStyle.UNDECORATED);
        menuWindow.initOwner(owner);
        menuWindow.setAlwaysOnTop(true);
        menuWindow.show();

        //создаем событие для открытия окна игры
        btnMenu.setOnAction(e -> {
            MainMenu.open();
            GameApplication.stop();
        });

        //создаем события для закрытия окна
        btnResume.setOnAction(e -> {
            close();
        });

        menuWindow.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) close();
        });

        vbox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) menuWindow.close();
        });

    }
    public static void close () {
        if (menuWindow != null) {
            Controller.mod = Mod.CHOOSING_MOD;
            GameApplication.resume();
            menuWindow.close();
        }
    }

    public static void move (double x, double y) {
        menuWindow.setX(x + owner.getWidth() / 2 - menuWindow.getWidth() / 2);
        menuWindow.setY(y + owner.getHeight() / 2 - menuWindow.getHeight() / 2);
    }
}
