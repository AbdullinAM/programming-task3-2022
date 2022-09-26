module programming {

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter.api;


    opens programming.task3.Controller;
    opens programming.task3;
    opens programming.task3.Core;
}