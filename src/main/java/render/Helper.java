package render;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Popup;

import java.io.IOException;
import java.net.URL;

/**
 * Вспомогательный класс для создания Popup-ов
 */
public abstract class Helper {
    public static Popup createPopup (String resPath) {
        Popup popup = new Popup();
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = StagesManager.class.getResource(resPath);
        loader.setLocation(xmlUrl);
        try {
            Parent root = loader.load();
            popup.getContent().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return popup;
    }
}
