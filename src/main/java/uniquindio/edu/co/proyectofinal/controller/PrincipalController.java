package uniquindio.edu.co.proyectofinal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import uniquindio.edu.co.proyectofinal.model.Cine;

import java.io.IOException;

public class PrincipalController {

    @FXML private Label lblTotalEntradas;
    @FXML private Label lblTotalIngresos;
    @FXML private BorderPane borderPanePrincipal;
    @FXML private Button btnVentas;
    @FXML private Button btnReportes;

    // Nuevos botones del menú superior
    @FXML private Button btnCartelera;
    @FXML private Button btnSalas;
    @FXML private Button btnSalir;

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
        activarBotonMenuPersonalizado(btnVentas);
    }

    @FXML
    void mostrarReportes(ActionEvent event) {
        borderPanePrincipal.setCenter(vistaReportesInicial);
        actualizarDatosEnPantalla();
        activarBotonMenuPersonalizado(btnReportes);
    }

    @FXML
    void mostrarCartelera(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/uniquindio/edu/co/proyectofinal/cartelera-view.fxml")
        );
        Parent vista = loader.load();
        CarteleraController controller = loader.getController();
        controller.setCine(this.cine);

        borderPanePrincipal.setCenter(vista);
        activarBotonMenuPersonalizado(btnCartelera);
    }

    @FXML
    void mostrarSalas(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/uniquindio/edu/co/proyectofinal/salas-view.fxml")
        );
        Parent vista = loader.load();
        SalasController controller = loader.getController();
        controller.setCine(this.cine);

        borderPanePrincipal.setCenter(vista);
        activarBotonMenuPersonalizado(btnSalas);
    }

    @FXML
    void salirAplicacion(ActionEvent event) {
        Stage stage = (Stage) borderPanePrincipal.getScene().getWindow();
        stage.close();
    }

    private void activarBotonMenuPersonalizado(Button botonActivo) {
        Button[] botones = {btnCartelera, btnVentas, btnSalas, btnReportes};
        for (Button b : botones) {
            if (b != null) {
                if (b == botonActivo) {
                    b.setStyle("-fx-background-color: #C8102E; -fx-background-radius: 20; -fx-font-weight: bold; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 6 16 6 16;");
                } else {
                    b.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-text-fill: #333333; -fx-cursor: hand;");
                }
            }
        }
    }
}