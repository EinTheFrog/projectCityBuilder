package render;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {

    public static void open () {
        //задаем начальные элементы и параметры для них
        Stage menuWindow = new Stage();
        Scene menuScene;
        VBox vbox = new VBox();
        Button btnStart = new Button("start");
        vbox.getChildren().add(btnStart);
        vbox.setPrefSize(300, 400);
        menuScene = new Scene(vbox);
        menuScene.getStylesheets().add("RedLord.css");

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
