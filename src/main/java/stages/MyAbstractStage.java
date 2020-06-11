package stages;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

abstract class MyAbstractStage {
    protected Stage stage;

    protected MyAbstractStage(String resPath) {
        //создаем окно и корневой узел с помощью fxml
        stage = new Stage();
        try {
            URL url = getClass().getResource(resPath);
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            retainController(loader);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IllegalStateException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR: Incorrect path to fxml file");
            alert.setContentText(e.getMessage());
            System.err.println(e.getMessage());
            e.printStackTrace();
            alert.showAndWait();
        }
    }

    protected abstract void retainController(FXMLLoader loader);

    protected void showStage() {
        stage.show();
    }

    protected Stage getInstanceStage() {
        return stage;
    }

    public void closeStage () {
        if (stage != null) stage.close();
    }

}
