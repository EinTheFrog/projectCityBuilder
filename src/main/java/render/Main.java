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
        try {
            MainMenu.open();
        } catch (IllegalStateException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR: Incorrect path to fxml file");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main (String[] args) {
        launch(args);
    }
}
