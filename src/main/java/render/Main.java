package render;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * класс, запускающий приложение
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            StagesManager.MainMenu.open();
        } catch (IOException e) {
            System.err.println("ERROR: Incorrect path to fxml file");
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {
        launch(args);
    }
}
