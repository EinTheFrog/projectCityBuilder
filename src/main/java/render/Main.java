package render;


import javafx.application.Application;
import javafx.stage.Stage;
import stages.MainMenu;

/**
 * класс, запускающий приложение
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainMenu.getInstance().open();
    }

    public static void main (String[] args) {
        launch(args);
    }
}
