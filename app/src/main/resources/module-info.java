module FirstGUI {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens FirstGUI.Controller;
    opens FirstGUI.View;
    opens FirstGUI.Model;
}