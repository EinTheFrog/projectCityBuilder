package popups;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public abstract class MyAbstractPopup {
    protected Popup popup;
    protected boolean isOpen = false;

    protected MyAbstractPopup(String resPath) {
        popup = new Popup();
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = MyAbstractPopup.class.getResource(resPath);
        loader.setLocation(xmlUrl);
        try {
            Parent root = loader.load();
            popup.getContent().add(root);
            popup.hideOnEscapeProperty().set(false);
            retainController(loader);
        } catch (IllegalStateException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR: Incorrect path to fxml file");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    protected void showPopup(Stage owner) {
        popup.show(owner);
    }

    public void hidePopup() {
        if (popup != null) popup.hide();
    }

    public void setInstanceCoords(double x, double y) {
        popup.setX(x - popup.getWidth() / 2);
        popup.setY(y - popup.getHeight() / 2);
    }

    abstract void retainController(FXMLLoader loader);

}