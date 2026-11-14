module uniquindio.edu.co.proyectofinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.sql;
    requires org.junit.jupiter.api;

    opens uniquindio.edu.co.proyectofinal.controller to javafx.fxml;
    opens uniquindio.edu.co.proyectofinal.viewController to javafx.fxml;

    exports uniquindio.edu.co.proyectofinal.controller;
    exports uniquindio.edu.co.proyectofinal.model;
    exports uniquindio.edu.co.proyectofinal.viewController;
    exports uniquindio.edu.co.proyectofinal;

    opens uniquindio.edu.co.proyectofinal to javafx.fxml;
}