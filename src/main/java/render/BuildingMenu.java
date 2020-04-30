package render;

import controller.Controller;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class BuildingMenu {
    private static Stage owner = GameApplication.gameWindow;
    private static Popup stage;
    public static void show (String name, int cost) {
        Scene scene;
        VBox vBox;
        Label lblName = new Label(name);
        Label lblCost = new Label(String.valueOf(cost));
        vBox = new VBox(lblName, lblCost);
        GameApplication.focusOnGameWindow();
        scene = new Scene(vBox);
        scene.getStylesheets().add("RedLord.css");
        stage = new Popup();
        stage.getContent().add(vBox);
        stage.show(owner);
    }

    public static void hideWindow () {
        if (stage != null) stage.hide();
    }

    public static void move (double x, double y) {
        stage.setX(x + owner.getWidth() - stage.getWidth());
        stage.setY(y + owner.getHeight() - stage.getHeight());
    }
}
