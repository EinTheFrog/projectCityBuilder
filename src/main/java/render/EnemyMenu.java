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
import java.util.Random;

public class EnemyMenu {
    private static int force = 20;
    private static int cost = 100;
/*    private static int FORCE_UP;
    private static int COST_UP;*/
    private static Stage owner = GameApplication.gameWindow;
    private static Popup menuPopup = new Popup();
    public static void open () {
        GameApplication.pause();
        //задаем начальные элементы и параметры для них
        VBox vBox = new VBox();
        Label lblText = new Label("Nomads came");
        InputStream in = GameApplication.class.getResourceAsStream("/textures/nomads.jpg");
        ImageView imgNomads = new ImageView(new Image(in));
        Button btnPay = new Button("Pay " + cost);
        Button btnFight = new Button("Fight " + force);
        HBox hBox = new HBox(btnPay, btnFight);
        hBox.setAlignment(Pos.CENTER);
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
            Controller.getChosenField().pay(cost);
            GameApplication.resume();
            close();
            cost *= 2;
            force -= 10;
        });

        btnFight.setOnAction(event -> {
            Random rnd = new Random();
            int result = rnd.nextInt(100);
            if (result > Controller.getChosenField().getForce() / (Controller.getChosenField().getForce() + force)) {
                Controller.getChosenField().pay(force * 5);
                if (Controller.getChosenField().getBuildingsList().size() > 0) {
                    result = rnd.nextInt(Controller.getChosenField().getBuildingsList().size());
                    Controller.destroyBuilding(Controller.getChosenField().getBuildingsList().get(result));
                }
            }
            GameApplication.resume();
            force *= 2;
            cost -= 10;
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
