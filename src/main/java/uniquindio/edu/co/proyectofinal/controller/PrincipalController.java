package uniquindio.edu.co.proyectofinal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import uniquindio.edu.co.proyectofinal.model.Cine;

public class PrincipalController {

    @FXML
    private Label lblTotalEntradas;

    @FXML
    private Label lblTotalIngresos;

    private Cine cine;

    @FXML
    public void initialize() {
        // Instancia inicial del cine (luego pueden compartir una única instancia entre vistas)
        this.cine = new Cine(67, "CINEUQ");
        actualizarDatosEnPantalla();
    }

    @FXML
    void actualizarReportes(ActionEvent event) {
        actualizarDatosEnPantalla();
    }

    private void actualizarDatosEnPantalla() {
        if (cine != null) {
            int entradas = cine.consultarTotalEntradasVendidas();
            double ingresos = cine.consultarTotalIngresosGenerados();

            lblTotalEntradas.setText(String.valueOf(entradas));
            lblTotalIngresos.setText("$ " + String.format("%,.0f", ingresos));
        }
    }
}