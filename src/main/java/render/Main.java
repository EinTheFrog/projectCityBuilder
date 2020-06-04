package render;


import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import stages.MainMenu;

import java.io.IOException;

/**
 * класс, запускающий приложение
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainMenu.open();
    }

    public static void main (String[] args) {
        launch(args);
    }
}
