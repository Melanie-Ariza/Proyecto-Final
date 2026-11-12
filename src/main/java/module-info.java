module uniquindio.edu.co.proyectofinal {
    requires javafx.controls;
    requires javafx.fxml;

    opens uniquindio.edu.co.proyectofinal.controller to javafx.fxml;
    opens uniquindio.edu.co.proyectofinal.viewController to javafx.fxml;

    exports uniquindio.edu.co.proyectofinal.controller;
    exports uniquindio.edu.co.proyectofinal.model;
    exports uniquindio.edu.co.proyectofinal.viewController;
}