package render;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Класс, отвечающий за все окна приложения
 */
public abstract class StagesManager {

    public static abstract class MainMenu {
        private static Stage mainMenuStage;

        public static void open () throws IOException {
            //создаем окно и корневой узел с помощью fxml
            mainMenuStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = MainMenu.class.getResource("/MainMenu.fxml");
            loader.setLocation(xmlUrl);
            Parent root = loader.load();
            //задаем сцену и окрываем окно
            mainMenuStage.setScene(new Scene(root));
            mainMenuStage.show();
        }

        public static void close () {
            if (mainMenuStage != null) mainMenuStage.close();
        }
    }

    public static abstract class GameApp {
        private static Stage gameStage = new Stage();
        private static GameAppController myController;
        public static void open () {
            //создаем окно и корневой узел с помощью fxml
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = GameApp.class.getResource("/GameApp.fxml");
            loader.setLocation(xmlUrl);
            try {
                Parent root = loader.load();
                gameStage.setScene(new Scene(root));
                myController = loader.getController();
                gameStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //привязываем координаты всех PopUp окон к координатам этого окна
            gameStage.xProperty().addListener(((observable, oldValue, newValue) -> moveMenus()));

            gameStage.yProperty().addListener(((observable, oldValue, newValue) -> moveMenus()));
        }

        
        private static void moveMenus() {
            if (Menu.isOpen) MenuController.move(getX() + getWidth() / 2, getY() + getHeight() / 2);
            if (EnemyMenu.isOpen)EnemyMenuController.move(getX() + getWidth() / 2, getY() + getHeight() / 2);
            if (DefeatMenu.isOpen) DefeatMenuController.move(getX() + getWidth() / 2, getY() + getHeight() / 2);
        }


        public static void close () {
            gameStage.close();
        }

        public static GameAppController getController() {
            return myController;
        }

        public static double getX() {
            return gameStage.getX();
        }

        public static double getY() {
            return gameStage.getY();
        }

        public static double getWidth() {
            return gameStage.getWidth();
        }

        public static double getHeight() {
            return gameStage.getHeight();
        }
    }

    public static abstract class EnemyMenu {
        private static Popup enemyPopup;
        public static boolean  isOpen = false;

        public static void open () {
            isOpen = true;
            GameApp.getController().setBlockedMod();
            Stage owner = GameApp.gameStage;
            if (enemyPopup != null) {
                enemyPopup.show(owner);
                return;
            }
            enemyPopup = Helper.createPopup("/EnemyMenu.fxml");
            enemyPopup.hideOnEscapeProperty().setValue(false);
            enemyPopup.show(owner);
            EnemyMenuController.move(GameApp.getX() + GameApp.getWidth() / 2,
                    GameApp.getY() + GameApp.getHeight() / 2);
        }

        public static void close () {
            if (enemyPopup != null) {
                isOpen = false;
                GameApp.myController.updateResources();
                GameApp.myController.setChoosingMod();
                GameApp.myController.resume();
                enemyPopup.hide();
            }
        }

        public static void setCoords(double x, double y) {
            StagesManager.setCoords(x, y, enemyPopup);
        }

    }

    public static abstract class Menu {
        private static Popup menuPopup;
        public static boolean isOpen = false;

        public static void open () {
            isOpen = true;
            Stage owner = GameApp.gameStage;
            if (menuPopup != null) {
                menuPopup.show(owner);
                return;
            }
            menuPopup = Helper.createPopup("/Menu.fxml");
            menuPopup.show(owner);
            MenuController.move(GameApp.getX() + GameApp.getWidth() / 2,
                    GameApp.getY() + GameApp.getHeight() / 2);
        }

        public static void close () {
            if (menuPopup != null) {
                isOpen = false;
                GameApp.myController.setChoosingMod();
                GameApp.myController.resume();
                menuPopup.hide();
            }
        }

        public static void setCoords(double x, double y) {
            StagesManager.setCoords(x, y, menuPopup);
        }
    }

    public static abstract class DefeatMenu {
        private static Popup defeatPopup;
        public static boolean isOpen = false;

        public static void open() {
            isOpen = true;
            GameApp.myController.setBlockedMod();
            Stage owner = GameApp.gameStage;
            if (defeatPopup != null) {
                defeatPopup.show(owner);
                return;
            }
            defeatPopup = Helper.createPopup("/DefeatMenu.fxml");
            defeatPopup.hideOnEscapeProperty().setValue(false);
            defeatPopup.show(owner);
            DefeatMenuController.move(GameApp.getX() + GameApp.getWidth() / 2,
                    GameApp.getY() + GameApp.getHeight() / 2);
        }

        public static void setCoords(double x, double y) {
            StagesManager.setCoords(x, y, defeatPopup);
        }
    }

    private static void setCoords(double x, double y, Popup popup) {
        popup.setX(x - popup.getWidth() / 2);
        popup.setY(y - popup.getHeight() / 2);
    }

    /**
     * Вспомогательный класс для создания Popup-ов
     */
    private static abstract class Helper {
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
}