package render;

import controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;


public class Menu {
    private static Stage owner = GameApplication.gameWindow;
    private static Popup menuPopup = new Popup();
    public static void open () {
        GameApplication.pause();
        //задаем начальные элементы и параметры для них
        VBox vBox = new VBox();
        Button btnMenu = new Button("menu");
        Button btnResume = new Button("resume");
        vBox.getStylesheets().add("Redlord.css");
        vBox.getChildren().addAll(btnResume, btnMenu);
        vBox.setStyle(" -fx-font-family: 'Times New Roman', Serif;\n" +
                "    -fx-font-size: 30;\n" +
                "    -fx-alignment: CENTER;\n" +
                "    -fx-base: #922B21;\n" +
                "    -fx-border-color: #F5B041 ;\n" +
                "    -fx-background-color: #C0392B ;\n" +
                "    -fx-spacing: 20;");
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        vBox.setPrefSize(200, 300);

        Controller.setMenuMod();
        //рендерим окно
        menuPopup.getContent().add(vBox);
        menuPopup.show(owner);

        //создаем событие для открытия окна игры
        btnMenu.setOnAction(e -> {
            MainMenu.open();
            GameApplication.stop();
        });

        //создаем события для закрытия окна
        btnResume.setOnAction(e -> {
            GameApplication.resume();
            close();
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
