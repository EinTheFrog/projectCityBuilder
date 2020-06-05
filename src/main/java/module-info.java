module render {
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;


    opens controller to javafx.base, javafx.fxml, javafx.controls, javafx.graphics;
    opens core to javafx.base, javafx.fxml, javafx.controls, javafx.graphics;
    opens popups to javafx.base, javafx.fxml, javafx.controls, javafx.graphics;
    opens render to javafx.base, javafx.fxml, javafx.controls, javafx.graphics;
    opens stages to javafx.base, javafx.fxml, javafx.controls, javafx.graphics;
    opens view to javafx.base, javafx.fxml, javafx.controls, javafx.graphics;

    exports controller;
    exports core;
    exports popups;
    exports render;
    exports stages;
    exports view;
    exports core.buildings;
}