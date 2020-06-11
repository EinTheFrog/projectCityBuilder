package popups;

import javafx.fxml.FXMLLoader;
import stages.GameApp;

public class DefeatMenu extends MyAbstractPopup {
    private static DefeatMenu instance;
    private DefeatMenu(String resPath) {
        super(resPath);
    }

    @Override
    void retainController(FXMLLoader loader) { }

    public static DefeatMenu getInstance() {
        if (instance == null) instance = new DefeatMenu("DefeatMenu.fxml");
        return instance;
    }

    public void open () {
        GameApp.getInstance().getController().setBlockedMod();
        isOpen = true;
        showPopup(GameApp.getInstance().getStage());
    }

    public boolean isOpen() {
        return isOpen;
    }
}
