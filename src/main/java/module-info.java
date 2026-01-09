module com.example.myapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.myapp to javafx.fxml;
    exports com.example.myapp;
}