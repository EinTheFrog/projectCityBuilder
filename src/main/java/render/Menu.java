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
    static Stage menuWindow;
    public static void open () {
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

        //рендерим окно
        menuWindow.setScene(menuScene);
        menuWindow.setTitle("Menu");
        menuWindow.initStyle(StageStyle.UNDECORATED);
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

        vbox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) menuWindow.close();
        });

    }
    public static void close () {
        if (menuWindow != null) {
            menuWindow.close();
            Controller.mod = Mod.BUILDING_MOD;
        }
    }
}
