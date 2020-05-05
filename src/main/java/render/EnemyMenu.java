package render;

import controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.InputStream;

public class EnemyMenu {
    private static int force = 20;
    private static int cost = 100;
    private static int forceUp;
    private static int costUp;
    private static Stage owner = GameApplication.gameWindow;
    private static Popup menuPopup = new Popup();
    public static void open () {
        GameApplication.pause();
        //задаем начальные элементы и параметры для них
        VBox vBox = new VBox();
        Label lblText = new Label("Пришли кочевники");
        InputStream in = GameApplication.class.getResourceAsStream("/textures/nomads.jpg");
        ImageView imgNomads = new ImageView(new Image(in));
        Button btnPay = new Button("Pay");
        Button btnFight = new Button("Fight");
        HBox hBox = new HBox(btnPay, btnFight);
        vBox.getStylesheets().add("Redlord.css");
        vBox.getChildren().addAll(lblText, imgNomads, hBox);
        vBox.setStyle(" -fx-font-family: 'Times New Roman', Serif;\n" +
                "    -fx-font-size: 30;\n" +
                "    -fx-alignment: CENTER;\n" +
                "    -fx-base: #922B21;\n" +
                "    -fx-border-color: #F5B041 ;\n" +
                "    -fx-background-color: #C0392B ;\n" +
                "    -fx-spacing: 20;");

        btnPay.setOnAction(event -> {
            GameApplication.resume();
            close();
        });

        btnFight.setOnAction(event -> {
            GameApplication.resume();
            close();
        });

        Controller.setEnemyMod();
        //рендерим окно
        menuPopup.getContent().add(vBox);
        menuPopup.show(owner);



        menuPopup.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                GameApplication.resume();
                close();
            }
        });


    }
    public static void close () {
        if (menuPopup != null) {
            Controller.setChoosingMod();
            menuPopup.hide();
        }
    }

    public static void move (double x, double y) {
        if (menuPopup == null) return;
        menuPopup.setX(x + (GameApplication.mainWindowWidth  - menuPopup.getWidth()) / 2);
        menuPopup.setY(y + (GameApplication.mainWindowHeight  - menuPopup.getHeight()) / 2);
    }
}
