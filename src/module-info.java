module main {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.datatransfer;
    requires java.desktop;

    exports com.swellshinider.main;
    exports com.swellshinider.util;

    opens com.swellshinider.controllers to javafx.fxml;
}