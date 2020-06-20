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

    public static GameApp getInstance() {
        if (instance == null) instance = new GameApp(RESPATH);
        return instance;
    }

    public static void deleteInstance() {
        instance = null;
    }

    public void open() {
        showStage();
        getStage().xProperty().addListener(((observable, oldValue, newValue) -> moveMenus()));
        getStage().yProperty().addListener(((observable, oldValue, newValue) -> moveMenus()));
    }

    private void moveMenus() {
        Menu.getInstance().setCords(getXCenter(), getYCenter());
        EnemyMenu.getInstance().setCords(getXCenter(), getYCenter());
        DefeatMenu.getInstance().setCords(getXCenter(), getYCenter());
        ResultMenu.getInstance().setCords(getXCenter(), getYCenter());
    }

    public void close() {
        closeStage();
    }

    public GameAppController getController() {
        return myController;
    }

    public double getXCenter() {
        return  getStage().getX() + getStage().getWidth() / 2;
    }

    public double getYCenter() {
        return  getStage().getY() + getStage().getHeight() / 2;
    }
}
