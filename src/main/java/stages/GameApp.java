package stages;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import popups.DefeatMenu;
import popups.EnemyMenu;
import popups.Menu;
import popups.ResultMenu;

public class GameApp extends MyAbstractStage {
    private static GameApp instance;
    private GameAppController myController;
    private static final String RESPATH = "GameApp.fxml";
    private GameApp(String resPath) {
        super(resPath);
    }

    @Override
    protected void retainController(FXMLLoader loader) {
        myController = loader.getController();
    }

    public static void open() {
        if (instance == null) instance = new GameApp(RESPATH);
        instance.showStage();
        instance.getInstanceStage().xProperty().addListener(((observable, oldValue, newValue) -> moveMenus()));
        instance.getInstanceStage().yProperty().addListener(((observable, oldValue, newValue) -> moveMenus()));
    }

    private static void moveMenus() {
        if (Menu.isOpen()) MenuController.move(getXCenter(), getYCenter());
        if (EnemyMenu.isOpen()) EnemyMenuController.move(getXCenter(), getYCenter());
        if (DefeatMenu.isOpen()) DefeatMenuController.move(getXCenter(), getYCenter());
        if (ResultMenu.isOpen()) ResultMenuController.move(getXCenter(), getYCenter());
    }

    public static void close() {
        instance.closeStage();
        instance = null;
    }

    private GameAppController getInstanceController() {
        return myController;
    }

    public static GameAppController getController() {
        return instance.getInstanceController();
    }

    public static Stage getStage() {
        return instance.getInstanceStage();
    }

    public static double getXCenter() {
        return  instance.getInstanceStage().getX() + instance.getInstanceStage().getWidth() / 2;
    }

    public static double getYCenter() {
        return  instance.getInstanceStage().getY() + instance.getInstanceStage().getHeight() / 2;
    }
}
