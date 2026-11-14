package uniquindio.edu.co.proyectofinal.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import uniquindio.edu.co.proyectofinal.controller.VentaController;
import uniquindio.edu.co.proyectofinal.model.*;

public class VentaViewController {

    @FXML private ComboBox<Persona> cmbVendedor;
    @FXML private TextField txtDocumentoCliente;
    @FXML private TextField txtNombreCliente;
    @FXML private ComboBox<Funcion> cmbFuncion;
    @FXML private ComboBox<Asiento> cmbAsiento;
    @FXML private ComboBox<TipoEntrada> cmbTipoEntrada;
    @FXML private Label lblTotalPagar;
    @FXML private Label lblMensaje;

    private VentaController ventaController;

    public void setCine(Cine cine) {
        this.ventaController = new VentaController(cine);
        cmbVendedor.getItems().setAll(ventaController.obtenerVendedoresDisponibles());
        cmbFuncion.getItems().setAll(ventaController.obtenerFuncionesProgramadas());
    }

    @FXML
    public void initialize() {
        cmbTipoEntrada.getItems().setAll(TipoEntrada.values());
        cmbTipoEntrada.setValue(TipoEntrada.GENERAL);

        cmbVendedor.setConverter(new StringConverter<Persona>() {
            @Override
            public String toString(Persona p) {
                if (p == null) return "";
                String tipo = (p instanceof ClienteVendedor) ? "Cliente-Vendedor" : "Vendedor";
                return p.getNombre() + " (" + tipo + " - Doc: " + p.getDocumento() + ")";
            }
            @Override
            public Persona fromString(String s) { return null; }
        });

        Callback<ListView<Asiento>, ListCell<Asiento>> asientoFactory = param -> new ListCell<Asiento>() {
            @Override
            protected void updateItem(Asiento asiento, boolean empty) {
                super.updateItem(asiento, empty);
                setText((empty || asiento == null) ? null : "Puesto " + asiento.obtenerPuesto());
            }
        };

        cmbAsiento.setCellFactory(asientoFactory);
        cmbAsiento.setButtonCell(asientoFactory.call(null));
        cmbAsiento.setConverter(new StringConverter<Asiento>() {
            @Override
            public String toString(Asiento a) { return (a == null) ? "" : "Puesto " + a.obtenerPuesto(); }
            @Override
            public Asiento fromString(String s) { return null; }
        });

        cmbFuncion.setConverter(new StringConverter<Funcion>() {
            @Override
            public String toString(Funcion f) {
                if (f == null) return "";
                return f.getPelicula().getNombre() + " | Sala " + f.getSala().getNumero()
                        + " (" + f.getSala().getColeccionAsientos() + ") - " + f.getHora();
            }
            @Override
            public Funcion fromString(String s) { return null; }
        });
    }

    @FXML
    void alSeleccionarFuncion(ActionEvent event) {
        Funcion funcion = cmbFuncion.getValue();
        cmbAsiento.getItems().setAll(ventaController.obtenerAsientosDisponibles(funcion));
        actualizarPrecioEstimado(null);
    }

    @FXML
    void actualizarPrecioEstimado(ActionEvent event) {
        double total = ventaController.calcularPrecioEstimado(cmbFuncion.getValue(), cmbTipoEntrada.getValue());
        lblTotalPagar.setText(total > 0 ? "$ " + String.format("%,.0f", total) : "$ 0");
    }

    @FXML
    void registrarVenta(ActionEvent event) {
        try {
            Persona vendedor = cmbVendedor.getValue();
            String docTexto = txtDocumentoCliente.getText().trim();
            String nombreCliente = txtNombreCliente.getText().trim();
            Funcion funcion = cmbFuncion.getValue();
            Asiento asiento = cmbAsiento.getValue();
            TipoEntrada tipoEntrada = cmbTipoEntrada.getValue();

            if (vendedor == null || docTexto.isEmpty() || nombreCliente.isEmpty() || funcion == null || asiento == null || tipoEntrada == null) {
                lblMensaje.setStyle("-fx-text-fill: #C8102E;");
                lblMensaje.setText("Por favor complete el vendedor, los datos del cliente, función, asiento y tipo de entrada.");
                return;
            }

            int documento = Integer.parseInt(docTexto);
            Venta venta = ventaController.registrarVenta(vendedor, documento, nombreCliente, funcion, asiento, tipoEntrada);

            lblMensaje.setStyle("-fx-text-fill: #1E7E34;");
            lblMensaje.setText("¡Venta #" + venta.getCodigo() + " exitosa! Puesto " + asiento.obtenerPuesto()
                    + " asignado a " + venta.getCliente().getNombre() + " por $" + String.format("%,.0f", venta.getValorPagado()));

            alSeleccionarFuncion(null);
            txtDocumentoCliente.clear();
            txtNombreCliente.clear();

        } catch (NumberFormatException e) {
            lblMensaje.setStyle("-fx-text-fill: #C8102E;");
            lblMensaje.setText("El documento del cliente debe ser numérico.");
        } catch (Exception e) {
            lblMensaje.setStyle("-fx-text-fill: #C8102E;");
            lblMensaje.setText(e.getMessage());
        }
    }
}
