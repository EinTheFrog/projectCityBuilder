module projectCityBuilder {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;


    opens render to javafx.base, javafx.fxml, javafx.controls, javafx.graphics;
    opens stages to javafx.base, javafx.fxml, javafx.controls, javafx.graphics;
    opens textures to javafx.base, javafx.fxml, javafx.controls, javafx.graphics;
    exports render;
    exports core;
}