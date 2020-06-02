package popups;

import controller.MenuController;
import javafx.fxml.FXMLLoader;
import stages.GameApp;

public class DefeatMenu extends MyAbstractPopup {
    private static DefeatMenu instance;
    public DefeatMenu(String resPath) {
        super(resPath);
    }

    @Override
    void retainController(FXMLLoader loader) {

    }

    public static void open () {
        if (instance == null) instance = new DefeatMenu("/DefeatMenu.fxml");
        instance.isOpen = true;
        instance.showPopup(GameApp.getStage());
        MenuController.move(GameApp.getXCenter(), GameApp.getYCenter());
    }

    public static void setCoords(double x, double y) {
        instance.setInstanceCoords(x, y);
    }

    public static boolean isOpen() {
        if (instance != null) return instance.isOpen;
        else return false;
    }
}
