package popups;

import controller.EnemyMenuController;
import javafx.fxml.FXMLLoader;
import stages.GameApp;

public class EnemyMenu extends MyAbstractPopup {
    private static EnemyMenu instance;

    private EnemyMenu(String resPath) {
        super(resPath);
    }

    public static EnemyMenu getInstance() {
        if (instance == null) instance = new EnemyMenu("EnemyMenu.fxml");
        return instance;
    }

    @Override
    void retainController(FXMLLoader loader) { }

    public void open () {
        GameApp.getInstance().getController().setBlockedMod();
        isOpen = true;
        showPopup(GameApp.getInstance().getStage());
    }

    public void close () {
        isOpen = false;
        hidePopup();
    }

    public boolean isOpen() {
       return isOpen;
    }
}
