package stages;

import javafx.fxml.FXMLLoader;

public class MainMenu extends MyAbstractStage {
    private static MainMenu instance;
    private static final String RESPATH = "MainMenu.fxml";
    private MainMenu(String resPath) {
        super(resPath);
    }

    @Override
    protected void retainController(FXMLLoader loader) {}

    public static void open() {
        if (instance == null) instance = new MainMenu(RESPATH);
        instance.showStage();
    }

    public static void close() {
        instance.closeStage();
    }
}
