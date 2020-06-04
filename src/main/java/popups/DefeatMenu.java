package popups;

import controller.DefeatMenuController;
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
        GameApp.getController().setBlockedMod();
        if (instance == null) instance = new DefeatMenu("/design/DefeatMenu.fxml");
        instance.isOpen = true;
        instance.showPopup(GameApp.getStage());
        DefeatMenuController.move(GameApp.getXCenter(), GameApp.getYCenter());
    }

    public static void setCoords(double x, double y) {
        instance.setInstanceCoords(x, y);
    }

    public static boolean isOpen() {
        if (instance != null) return instance.isOpen;
        else return false;
    }
}
