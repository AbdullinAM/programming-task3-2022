module com.example.checkers {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.checkers to javafx.fxml;
    exports com.example.checkers;
}