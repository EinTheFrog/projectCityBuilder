package render;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Menu {
    static Stage menuWindow;
    public static void open () {
        //задаем начальные элементы и параметры для них
        menuWindow = new Stage();
        Scene menuScene;
        VBox vbox = new VBox();
        Button btnMenu = new Button("menu");
        vbox.getChildren().add(btnMenu);
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
            GameApplication.stop();
            MainMenu.open();
            menuWindow.close();
        });
    }

    public static void close () {
        menuWindow.close();
    }
}
