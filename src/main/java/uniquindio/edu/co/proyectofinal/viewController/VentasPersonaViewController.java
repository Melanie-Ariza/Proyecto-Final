package uniquindio.edu.co.proyectofinal.viewController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import uniquindio.edu.co.proyectofinal.controller.VentasPersonaController;
import uniquindio.edu.co.proyectofinal.model.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentasPersonaViewController {

    @FXML private ComboBox<Persona> comboPersonas;

    @FXML private TableView<Venta> tablaVentasPersona;
    @FXML private TableColumn<Venta, String> colCodigoVenta, colFechaVenta;
    @FXML private TableColumn<Venta, Double> colValorVenta;

    @FXML private TableView<Venta> tablaVentasCanceladas;
    @FXML private TableColumn<Venta, String> colCodigoVentaCancelada, colFechaVentaCancelada;
    @FXML private TableColumn<Venta, Double> colValorVentaCancelada;

    private VentasPersonaController ventasPersonaController;

    public void setCine(Cine cine) {
        this.ventasPersonaController = new VentasPersonaController(cine);
        actualizarComboPersonas();
    }

    @FXML
    public void initialize() {
        colCodigoVenta.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colFechaVenta.setCellValueFactory(cellData -> {
            LocalDateTime fecha = cellData.getValue().getFechaHoraVenta();
            return new SimpleStringProperty(fecha != null ? fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
        });
        colValorVenta.setCellValueFactory(new PropertyValueFactory<>("valorPagado"));

        if (colCodigoVentaCancelada != null) {
            colCodigoVentaCancelada.setCellValueFactory(new PropertyValueFactory<>("codigo"));
            colFechaVentaCancelada.setCellValueFactory(cellData -> {
                LocalDateTime fecha = cellData.getValue().getFechaHoraVenta();
                return new SimpleStringProperty(fecha != null ? fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
            });
            colValorVentaCancelada.setCellValueFactory(new PropertyValueFactory<>("valorPagado"));
        }
    }

    public void actualizarComboPersonas() {
        if (ventasPersonaController != null) {
            comboPersonas.setItems(FXCollections.observableArrayList(ventasPersonaController.obtenerTodasLasPersonas()));
        }
    }

    @FXML
    void cargarVentasPersona(ActionEvent event) {
        Persona personaSeleccionada = comboPersonas.getSelectionModel().getSelectedItem();
        if (personaSeleccionada != null && ventasPersonaController != null) {
            tablaVentasPersona.setItems(FXCollections.observableArrayList(
                    ventasPersonaController.obtenerVentasDePersona(personaSeleccionada)
            ));
            if (tablaVentasCanceladas != null) {
                tablaVentasCanceladas.setItems(FXCollections.observableArrayList(
                        ventasPersonaController.obtenerVentasCanceladasDePersona(personaSeleccionada)
                ));
            }
        }
    }

    @FXML
    void cancelarVentaSeleccionada(ActionEvent event) {
        Venta ventaSeleccionada = tablaVentasPersona.getSelectionModel().getSelectedItem();
        if (ventaSeleccionada != null) {
            ventasPersonaController.cancelarVentaDePersona(ventaSeleccionada);
            cargarVentasPersona(null); // Refresca ambas tablas (activos y cancelados)
            mostrarAlerta("Éxito", "La venta ha sido cambiada a estado CANCELADA, se liberaron los asientos y se movió al apartado correspondiente.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Seleccione una venta activa para cancelar.", Alert.AlertType.WARNING);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}