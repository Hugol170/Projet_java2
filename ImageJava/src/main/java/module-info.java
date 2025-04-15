module com.example.imagejava {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.example.imagejava to javafx.fxml;
    opens com.example.imagejava.metadata to com.google.gson;

    exports com.example.imagejava;
    exports com.example.imagejava.metadata;
}
