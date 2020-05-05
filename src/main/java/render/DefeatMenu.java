package render;

import controller.Controller;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class DefeatMenu {
    private static Stage owner = GameApplication.gameWindow;
    private static Popup menuPopup = new Popup();
    public static void open () {
        GameApplication.pause();
        //задаем начальные элементы и параметры для них
        Label lbl = new Label("You lose!");
        Button btnMenu = new Button("menu");
        VBox vBox = new VBox(lbl, btnMenu);
        vBox.getStylesheets().add("Redlord.css");
        vBox.setStyle(" -fx-font-family: 'Times New Roman', Serif;\n" +
                "    -fx-font-size: 30;\n" +
                "    -fx-alignment: CENTER;\n" +
                "    -fx-base: #922B21;\n" +
                "    -fx-border-color: #F5B041 ;\n" +
                "    -fx-background-color: #C0392B ;\n" +
                "    -fx-spacing: 20;");


        Controller.setMenuMod();
        //рендерим окно
        menuPopup.getContent().add(vBox);
        menuPopup.show(owner);

        //создаем событие для открытия главного меню
        btnMenu.setOnAction(e -> {
            MainMenu.open();
            GameApplication.stop();
        });

        menuPopup.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                GameApplication.resume();
                close();
            }
        });

        vBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) menuPopup.hide();
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
