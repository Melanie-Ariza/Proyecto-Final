package uniquindio.edu.co.proyectofinal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import uniquindio.edu.co.proyectofinal.model.Cine;

import java.io.IOException;

public class PrincipalController {

    @FXML
    private Label lblTotalEntradas;

    @FXML
    private Label lblTotalIngresos;

    @FXML private BorderPane borderPanePrincipal;
    @FXML private Button btnVentas;
    @FXML private Button btnReportes;

    private Node vistaReportesInicial;

    private Cine cine;

    @FXML
    public void initialize() {
        this.cine = new Cine(1, "CINEMAUQ");
        this.vistaReportesInicial = borderPanePrincipal.getCenter();
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
    @FXML
    void mostrarVentas(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/uniquindio/edu/co/proyectofinal/ventas-view.fxml")
        );
        Parent vistaVentas = loader.load();
        VentaController controller = loader.getController();
        controller.setCine(this.cine);

        borderPanePrincipal.setCenter(vistaVentas);
        activarBotonMenu(btnVentas, btnReportes);
    }

    @FXML
    void mostrarReportes(ActionEvent event) {
        borderPanePrincipal.setCenter(vistaReportesInicial);
        actualizarDatosEnPantalla();
        activarBotonMenu(btnReportes, btnVentas);
    }

    private void activarBotonMenu(Button activo, Button inactivo) {
        activo.setStyle("-fx-background-color: #C8102E; -fx-background-radius: 20; -fx-font-weight: bold; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 6 16 6 16;");
        inactivo.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-text-fill: #333333; -fx-cursor: hand;");
    }
}