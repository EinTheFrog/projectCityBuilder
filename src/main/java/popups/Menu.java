package popups;


import controller.MenuController;
import javafx.fxml.FXMLLoader;
import stages.GameApp;

public class Menu extends MyAbstractPopup {
    private static Menu instance;

    public Menu(String resPath) {
        super(resPath);
    }

    @Override
    void retainController(FXMLLoader loader) {

    }

    public static void open () {
        if (instance == null) instance = new Menu("/Menu.fxml");
        instance.isOpen = true;
        instance.showPopup(GameApp.getStage());
        MenuController.move(GameApp.getXCenter(), GameApp.getYCenter());
    }

    public static void close () {
        if (instance != null) {
            instance.isOpen = false;
            GameApp.getController().updateResources();
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
