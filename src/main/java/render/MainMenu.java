package render;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * класс для главного меню игры
 */

public class MainMenu {
    private static Stage menuStage;

    /**
     * метод для создания и открытия главного меню игры
     */
    public static void open () throws IOException {
        //создаем окно и корневой узел с помощью fxml
        menuStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = MainMenu.class.getResource("/MainMenu.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        //задаем сцену и окрываем окно
        menuStage.setScene(new Scene(root));
        menuStage.show();
    }

    /**
     * метод для закрытия главного меню игры
     */
    public static void close () {
        if (menuStage != null) menuStage.close();
    }

}
