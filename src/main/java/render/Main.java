package render;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * класс, запускающий приложение
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainMenu.open();
    }
    public static void main (String[] args) {launch(args);}
}
