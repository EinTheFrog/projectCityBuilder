package popups;


import javafx.fxml.FXMLLoader;
import stages.GameApp;

public class Menu extends MyAbstractPopup {
    private static Menu instance;

    private Menu(String resPath) {
        super(resPath);
    }

    @Override
    void retainController(FXMLLoader loader) { }

    public static Menu getInstance() {
        if (instance == null) instance = new Menu("Menu.fxml");
        return instance;
    }

    public void open () {
        instance.isOpen = true;
        instance.popup.hideOnEscapeProperty().set(true);
        instance.showPopup(GameApp.getInstance().getStage());
    }

    public void close () {
        isOpen = false;
        GameApp.getInstance().getController().setChoosingMod();
        GameApp.getInstance().getController().resume();
        hidePopup();
    }

    public static boolean isOpen() {
        if (instance != null) return instance.isOpen;
        else return false;
    }
}
