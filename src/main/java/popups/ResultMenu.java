package popups;

import controller.ResultMenuController;
import javafx.fxml.FXMLLoader;
import stages.GameApp;

public class ResultMenu extends MyAbstractPopup {
    private static ResultMenu instance;
    private ResultMenuController resultMenuController;
    private ResultMenu(String resPath) {
        super(resPath);
    }

    @Override
    void retainController(FXMLLoader loader) {
        resultMenuController = loader.getController();
    }

    public static ResultMenu getInstance() {
        if (instance == null) instance = new ResultMenu("ResultMenu.fxml");
        return instance;
    }

    private ResultMenuController getInstanceController() {
        return resultMenuController;
    }

    public void open (Boolean userWon) {
        isOpen = true;
        showPopup(GameApp.getInstance().getStage());
        getInstanceController().setText(userWon);
    }

    public void close () {
        isOpen = false;
        GameApp.getInstance().getController().setChoosingMod();
        GameApp.getInstance().getController().resume();
        hidePopup();
    }

    public  boolean isOpen() {
        return isOpen;
    }
}
