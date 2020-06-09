module projectCityBuilder {
    requires javafx.fxml;
    requires javafx.controls;

    opens controller to javafx.fxml;
    exports render;
}