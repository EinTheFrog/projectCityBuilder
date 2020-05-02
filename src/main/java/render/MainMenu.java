package render;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class MainMenu {

    /**
     * класс для главного меню игры
     */
    public static void open () {
        //создаем окно, сцену, панель и узлы
        Stage menuWindow = new Stage();
        Scene menuScene;
        VBox vbox = new VBox();
        Button btnStart = new Button("start");
        //добавляем узел на панель
        vbox.getChildren().add(btnStart);
        //задаем параметы панели
        vbox.setPrefSize(300, 400);
        vbox.getStylesheets().add("RedLord.css");
        //инициализируем сцену и задаем ее параметры
        menuScene = new Scene(vbox);
        //menuScene.getStylesheets().add("RedLord.css");

        //рендерим окно
        menuWindow.setScene(menuScene);
        menuWindow.setTitle("Menu");
        menuWindow.show();

        //создаем событие для открытия окна игры
        btnStart.setOnAction(e -> {
            GameApplication.run();
            menuWindow.close();
        });
    }

}
