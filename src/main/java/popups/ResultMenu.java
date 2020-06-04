package popups;

import controller.ResultMenuController;
import javafx.fxml.FXMLLoader;
import stages.GameApp;

public class ResultMenu extends MyAbstractPopup {
    private static ResultMenu instance;
    private ResultMenuController resultMenuController;
    public ResultMenu(String resPath) {
        super(resPath);
    }

    @Override
    void retainController(FXMLLoader loader) {
        resultMenuController = loader.getController();
    }

    private ResultMenuController getInstanceController() {
        return resultMenuController;
    }

    public static void open (Boolean userWon) {
        if (instance == null) instance = new ResultMenu("/design/ResultMenu.fxml");
        instance.isOpen = true;
        instance.showPopup(GameApp.getStage());
        instance.getInstanceController().setText(userWon);
        ResultMenuController.move(GameApp.getXCenter(), GameApp.getYCenter());
    }

    public static void close () {
        if (instance != null) {
            instance.isOpen = false;
            GameApp.getController().setChoosingMod();
            GameApp.getController().resume();
            instance.hidePopup();
        }
    }

    public static void setCoords(double x, double y) {
        instance.setInstanceCoords(x, y);
    }

    public static boolean isOpen() {
        if (instance != null) return instance.isOpen;
        else return false;
    }
}
