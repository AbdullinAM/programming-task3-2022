module programming {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens programming.task3.Controller;
    opens programming.task3;
    opens programming.task3.Core;
}