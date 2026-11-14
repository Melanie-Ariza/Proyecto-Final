package uniquindio.edu.co.proyectofinal.viewController;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import uniquindio.edu.co.proyectofinal.controller.GestionPersonasController;
import uniquindio.edu.co.proyectofinal.model.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestionPersonasViewController {

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, Integer> colIdCliente;
    @FXML private TableColumn<Cliente, String> colNombreCliente;
    @FXML private TextField txtIdCliente, txtNombreCliente;

    @FXML private TableView<Vendedor> tablaVendedores;
    @FXML private TableColumn<Vendedor, Integer> colIdVendedor;
    @FXML private TableColumn<Vendedor, String> colNombreVendedor;
    @FXML private TableColumn<Vendedor, Cargo> colCargoVendedor;
    @FXML private TableColumn<Vendedor, Double> colSueldoVendedor;
    @FXML private TextField txtIdVendedor, txtNombreVendedor, txtSalarioVendedor;
    @FXML private ComboBox<Cargo> comboCargoVendedor;

    @FXML private TableView<ClienteVendedor> tablaClienteVendedor;
    @FXML private TableColumn<ClienteVendedor, Integer> colIdCV;
    @FXML private TableColumn<ClienteVendedor, String> colNombreCV;
    @FXML private TableColumn<ClienteVendedor, Cargo> colCargoCV;
    @FXML private TableColumn<ClienteVendedor, Double> colSueldoCV;
    @FXML private TextField txtIdCV, txtNombreCV, txtSalarioCV;
    @FXML private ComboBox<Cargo> comboCargoCV;

    @FXML private ComboBox<Persona> comboPersonas;
    @FXML private TableView<Venta> tablaVentasPersona;
    @FXML private TableColumn<Venta, String> colCodigoVenta, colFechaVenta;
    @FXML private TableColumn<Venta, Double> colValorVenta;

    private GestionPersonasController gestionPersonasController;

    public void setCine(Cine cine) {
        this.gestionPersonasController = new GestionPersonasController(cine);
        actualizarTablas();
    }

    @FXML
    public void initialize() {
        colIdCliente.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colNombreCliente.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        colIdVendedor.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colNombreVendedor.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCargoVendedor.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colSueldoVendedor.setCellValueFactory(cellData -> {
            Vendedor v = cellData.getValue();
            return new SimpleDoubleProperty((v != null && v.getCargo() != null) ? v.getCargo().getSalario() : 0.0).asObject();
        });

        colIdCV.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colNombreCV.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCargoCV.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colSueldoCV.setCellValueFactory(cellData -> {
            ClienteVendedor cv = cellData.getValue();
            return new SimpleDoubleProperty((cv != null && cv.getCargo() != null) ? cv.getCargo().getSalario() : 0.0).asObject();
        });

        colCodigoVenta.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colFechaVenta.setCellValueFactory(cellData -> {
            LocalDateTime fecha = cellData.getValue().getFechaHoraVenta();
            if (fecha != null) {
                return new SimpleStringProperty(fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            return new SimpleStringProperty("");
        });
        colValorVenta.setCellValueFactory(new PropertyValueFactory<>("valorPagado"));

        if (comboCargoVendedor != null) {
            comboCargoVendedor.setItems(FXCollections.observableArrayList(Cargo.values()));
            comboCargoVendedor.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
                    txtSalarioVendedor.setText(newVal != null ? String.format("%,.0f", newVal.getSalario()) : ""));
        }

        if (comboCargoCV != null) {
            comboCargoCV.setItems(FXCollections.observableArrayList(Cargo.values()));
            comboCargoCV.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
                    txtSalarioCV.setText(newVal != null ? String.format("%,.0f", newVal.getSalario()) : ""));
        }

        configurarListenersTablas();
    }

    private void configurarListenersTablas() {
        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtIdCliente.setText(String.valueOf(newVal.getDocumento()));
                txtIdCliente.setDisable(true);
                txtNombreCliente.setText(newVal.getNombre());
            }
        });

        tablaVendedores.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtIdVendedor.setText(String.valueOf(newVal.getDocumento()));
                txtIdVendedor.setDisable(true);
                txtNombreVendedor.setText(newVal.getNombre());
                if (newVal.getCargo() != null) {
                    comboCargoVendedor.setValue(newVal.getCargo());
                    txtSalarioVendedor.setText(String.format("%,.0f", newVal.getCargo().getSalario()));
                }
            }
        });

        tablaClienteVendedor.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtIdCV.setText(String.valueOf(newVal.getDocumento()));
                txtIdCV.setDisable(true);
                txtNombreCV.setText(newVal.getNombre());
                if (newVal.getCargo() != null) {
                    comboCargoCV.setValue(newVal.getCargo());
                    txtSalarioCV.setText(String.format("%,.0f", newVal.getCargo().getSalario()));
                }
            }
        });
    }

    public void actualizarTablas() {
        if (gestionPersonasController != null) {
            tablaClientes.setItems(FXCollections.observableArrayList(gestionPersonasController.obtenerClientes()));
            tablaVendedores.setItems(FXCollections.observableArrayList(gestionPersonasController.obtenerVendedores()));
            tablaClienteVendedor.setItems(FXCollections.observableArrayList(gestionPersonasController.obtenerClienteVendedores()));
            comboPersonas.setItems(FXCollections.observableArrayList(gestionPersonasController.obtenerTodasLasPersonas()));
        }
    }

    @FXML
    void guardarCliente(ActionEvent event) {
        try {
            if (txtIdCliente.getText().isEmpty() || txtNombreCliente.getText().isEmpty()) {
                mostrarAlerta("Error", "Complete todos los campos del cliente.", Alert.AlertType.ERROR);
                return;
            }
            boolean esNuevo = tablaClientes.getSelectionModel().getSelectedItem() == null;
            boolean ok = gestionPersonasController.guardarCliente(Integer.parseInt(txtIdCliente.getText()), txtNombreCliente.getText(), esNuevo);
            if (!ok) {
                mostrarAlerta("Error", "Ya existe una persona registrada con ese documento.", Alert.AlertType.ERROR);
                return;
            }
            limpiarCamposCliente(null);
            actualizarTablas();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El documento debe ser un número entero válido.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void eliminarCliente(ActionEvent event) {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null && gestionPersonasController != null) {
            gestionPersonasController.eliminarPersona(seleccionado.getDocumento());
            actualizarTablas();
            limpiarCamposCliente(null);
        } else {
            mostrarAlerta("Advertencia", "Seleccione un cliente para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void limpiarCamposCliente(ActionEvent event) {
        txtIdCliente.clear();
        txtIdCliente.setDisable(false);
        txtNombreCliente.clear();
        tablaClientes.getSelectionModel().clearSelection();
    }

    @FXML
    void guardarVendedor(ActionEvent event) {
        try {
            if (txtIdVendedor.getText().isEmpty() || txtNombreVendedor.getText().isEmpty() || comboCargoVendedor.getValue() == null) {
                mostrarAlerta("Error", "Complete todos los campos y seleccione un cargo.", Alert.AlertType.ERROR);
                return;
            }
            boolean esNuevo = tablaVendedores.getSelectionModel().getSelectedItem() == null;
            boolean ok = gestionPersonasController.guardarVendedor(Integer.parseInt(txtIdVendedor.getText()), txtNombreVendedor.getText(), comboCargoVendedor.getValue(), esNuevo);
            if (!ok) {
                mostrarAlerta("Error", "Ya existe una persona registrada con ese documento.", Alert.AlertType.ERROR);
                return;
            }
            limpiarCamposVendedor(null);
            actualizarTablas();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Documento inválido.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void eliminarVendedor(ActionEvent event) {
        Vendedor seleccionado = tablaVendedores.getSelectionModel().getSelectedItem();
        if (seleccionado != null && gestionPersonasController != null) {
            gestionPersonasController.eliminarPersona(seleccionado.getDocumento());
            actualizarTablas();
            limpiarCamposVendedor(null);
        } else {
            mostrarAlerta("Advertencia", "Seleccione un vendedor para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void limpiarCamposVendedor(ActionEvent event) {
        txtIdVendedor.clear();
        txtIdVendedor.setDisable(false);
        txtNombreVendedor.clear();
        txtSalarioVendedor.clear();
        comboCargoVendedor.setValue(null);
        tablaVendedores.getSelectionModel().clearSelection();
    }

    @FXML
    void guardarClienteVendedor(ActionEvent event) {
        try {
            if (txtIdCV.getText().isEmpty() || txtNombreCV.getText().isEmpty() || comboCargoCV.getValue() == null) {
                mostrarAlerta("Error", "Complete todos los campos y seleccione un cargo.", Alert.AlertType.ERROR);
                return;
            }
            boolean esNuevo = tablaClienteVendedor.getSelectionModel().getSelectedItem() == null;
            boolean ok = gestionPersonasController.guardarClienteVendedor(Integer.parseInt(txtIdCV.getText()), txtNombreCV.getText(), comboCargoCV.getValue(), esNuevo);
            if (!ok) {
                mostrarAlerta("Error", "Ya existe una persona registrada con ese documento.", Alert.AlertType.ERROR);
                return;
            }
            limpiarCamposCV(null);
            actualizarTablas();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El documento debe ser un número entero válido.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void eliminarClienteVendedor(ActionEvent event) {
        ClienteVendedor seleccionado = tablaClienteVendedor.getSelectionModel().getSelectedItem();
        if (seleccionado != null && gestionPersonasController != null) {
            gestionPersonasController.eliminarPersona(seleccionado.getDocumento());
            actualizarTablas();
            limpiarCamposCV(null);
        } else {
            mostrarAlerta("Advertencia", "Seleccione un cliente-vendedor para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void limpiarCamposCV(ActionEvent event) {
        txtIdCV.clear();
        txtIdCV.setDisable(false);
        txtNombreCV.clear();
        txtSalarioCV.clear();
        comboCargoCV.setValue(null);
        tablaClienteVendedor.getSelectionModel().clearSelection();
    }

    @FXML
    void cargarVentasPersona(ActionEvent event) {
        Persona personaSeleccionada = comboPersonas.getSelectionModel().getSelectedItem();
        if (personaSeleccionada != null && gestionPersonasController != null) {
            tablaVentasPersona.setItems(FXCollections.observableArrayList(gestionPersonasController.obtenerVentasDePersona(personaSeleccionada)));
        }
    }

    @FXML
    void eliminarVentaSeleccionada(ActionEvent event) {
        Venta ventaSeleccionada = tablaVentasPersona.getSelectionModel().getSelectedItem();
        Persona personaSeleccionada = comboPersonas.getSelectionModel().getSelectedItem();
        if (ventaSeleccionada != null && personaSeleccionada != null) {
            gestionPersonasController.eliminarVentaDePersona(personaSeleccionada, ventaSeleccionada);
            cargarVentasPersona(null);
            mostrarAlerta("Éxito", "Venta eliminada de la persona correctamente.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Advertencia", "Seleccione una persona y una venta para eliminar.", Alert.AlertType.WARNING);
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
