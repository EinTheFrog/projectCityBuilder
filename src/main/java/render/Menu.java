package render;

import controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Mod;


public class Menu {
    private static Stage owner = GameApplication.gameWindow;
    private static Popup menuWindow = new Popup();
    public static void open () {
        GameApplication.pause();
        //задаем начальные элементы и параметры для них
        VBox vbox = new VBox();
        Button btnMenu = new Button("menu");
        Button btnResume = new Button("resume");
        vbox.getChildren().addAll(btnResume, btnMenu);
        vbox.setPrefSize(200, 300);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        Controller.mod = Mod.MENU_MOD;
        //рендерим окно
        menuWindow.getContent().add(vbox);
        menuWindow.show(owner);

        //создаем событие для открытия окна игры
        btnMenu.setOnAction(e -> {
            MainMenu.open();
            GameApplication.stop();
        });

        //создаем события для закрытия окна
        btnResume.setOnAction(e -> {
            GameApplication.resume();
            close();
        });

        menuWindow.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                GameApplication.resume();
                close();
            }
        });

        vbox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) menuWindow.hide();
        });

    }
    public static void close () {
        if (menuWindow != null) {
            Controller.mod = Mod.CHOOSING_MOD;
            menuWindow.hide();
        }
    }

    public static void move (double x, double y) {
        menuWindow.setX(x + (GameApplication.mainWindowWidth  - menuWindow.getWidth()) / 2);
        menuWindow.setY(y + (GameApplication.mainWindowHeight  - menuWindow.getHeight()) / 2);
    }
}
