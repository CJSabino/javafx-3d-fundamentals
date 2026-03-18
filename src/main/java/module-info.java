module com.example.teste3d {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.teste3d to javafx.fxml;
    exports com.example.teste3d;
}