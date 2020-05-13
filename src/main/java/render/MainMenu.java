package render;

import controller.MainMenuController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class MainMenu {
    private static Stage menuStage;

    /**
     * класс для главного меню игры
     */

    public static void open () throws IOException {
        //создаем окно, сцену, панель и узлы
        menuStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = MainMenu.class.getResource("/MainMenu.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        menuStage.setScene(new Scene(root));
        menuStage.show();
    }

    public static void close () {
        menuStage.close();
    }

}
