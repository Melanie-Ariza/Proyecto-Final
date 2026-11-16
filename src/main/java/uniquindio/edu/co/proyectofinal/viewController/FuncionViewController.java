package uniquindio.edu.co.proyectofinal.viewController;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import uniquindio.edu.co.proyectofinal.controller.FuncionController;
import uniquindio.edu.co.proyectofinal.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class FuncionViewController {

    @FXML private TextField txtCodigo, txtHora, txtValor;
    @FXML private ComboBox<Pelicula> cmbPelicula;
    @FXML private ComboBox<Sala> cmbSala;
    @FXML private ComboBox<EstadoFuncion> cmbEstado;
    @FXML private DatePicker datePickerFecha, datePickerConsulta;

    @FXML private TableView<Funcion> tablaFunciones;
    @FXML private TableColumn<Funcion, Integer> colCodigo;
    @FXML private TableColumn<Funcion, String> colPelicula, colSala, colFecha, colHora, colValor, colEstado;
    @FXML private TableColumn<Funcion, Integer> colAsientosDisponibles;

    private FuncionController funcionController;

    public void setCine(Cine cine) {
        this.funcionController = new FuncionController(cine);
        cargarDatosCombo();
        cargarDatos(null);
    }

    @FXML
    public void initialize() {
        txtCodigo.setEditable(false); // El código no se debe dejar modificar

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colPelicula.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPelicula() != null ? cellData.getValue().getPelicula().getNombre() : ""));
        colSala.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSala() != null ? "Sala #" + cellData.getValue().getSala().getNumero() : ""));
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFecha() != null ? cellData.getValue().getFecha().toString() : ""));
        colHora.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHora() != null ? cellData.getValue().getHora().toString() : ""));
        colValor.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("$ %,.0f", cellData.getValue().getValorBase())));
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstadoFuncion() != null ? cellData.getValue().getEstadoFuncion().name() : ""));

        colAsientosDisponibles.setCellValueFactory(cellData -> {
            Funcion f = cellData.getValue();
            int libres = 0;
            if (f != null && f.getSala() != null && f.getSala().getListaAsientos() != null) {
                for (Asiento a : f.getSala().getListaAsientos()) {
                    if (a.getEstado() == EstadoAsiento.DISPONIBLE) libres++;
                }
            }
            return new SimpleObjectProperty<>(libres);
        });

        // Conversores para ComboBox
        cmbPelicula.setConverter(new StringConverter<>() {
            @Override public String toString(Pelicula p) { return p == null ? "" : p.getNombre() + " (" + p.getGenero() + ")"; }
            @Override public Pelicula fromString(String string) { return null; }
        });

        // En initialize(), actualiza el converter de cmbSala para mostrar su estado:
        cmbSala.setConverter(new StringConverter<>() {
            @Override
            public String toString(Sala s) {
                if (s == null) return "";
                String estadoTxt = (s.getEstado() == EstadoSala.FUERA_DE_SERVICIO) ? " [FUERA DE SERVICIO]" : " [Disponible]";
                return "Sala #" + s.getNumero() + " - Cap: " + s.getCapacidad() + estadoTxt;
            }
            @Override
            public Sala fromString(String string) { return null; }
        });

        cmbEstado.getItems().setAll(EstadoFuncion.values());
        cmbEstado.setValue(EstadoFuncion.PROGRAMADA);

        datePickerFecha.setValue(LocalDate.now());
        datePickerConsulta.setValue(LocalDate.now());

        // Listener para cargar datos al seleccionar de la tabla
        tablaFunciones.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, f) -> {
            if (f != null) {
                txtCodigo.setText(String.valueOf(f.getCodigo()));
                cmbPelicula.setValue(f.getPelicula());
                cmbSala.setValue(f.getSala());
                datePickerFecha.setValue(f.getFecha());
                txtHora.setText(f.getHora() != null ? f.getHora().toString() : "");
                txtValor.setText(String.valueOf(f.getValorBase()));
                cmbEstado.setValue(f.getEstadoFuncion());
            }
        });
    }

    public void cargarDatosCombo() {
        if (funcionController != null) {
            // Solo con las películas disponibles
            cmbPelicula.setItems(FXCollections.observableArrayList(funcionController.obtenerPeliculasDisponibles()));
            cmbSala.setItems(FXCollections.observableArrayList(funcionController.obtenerSalasDisponibles()));
        }
    }

    @FXML
    void cargarDatos(ActionEvent event) {
        if (funcionController != null) {
            tablaFunciones.setItems(FXCollections.observableArrayList(funcionController.obtenerTodasLasFunciones()));
            cargarDatosCombo();
        }
    }

    @FXML
    void guardarFuncion(ActionEvent event) {
        try {
            Pelicula peli = cmbPelicula.getValue();
            Sala sala = cmbSala.getValue();
            LocalDate fecha = datePickerFecha.getValue();

            if (peli == null || sala == null || fecha == null || txtHora.getText().isEmpty() || txtValor.getText().isEmpty()) {
                mostrarAlerta("Campos incompletos", "Complete todos los campos para programar la función.", Alert.AlertType.WARNING);
                return;
            }

            LocalTime hora = LocalTime.parse(txtHora.getText().trim());
            double valor = Double.parseDouble(txtValor.getText().trim());

            String error = funcionController.programarFuncion(peli, sala, fecha, hora, valor);
            if (error == null) {
                mostrarAlerta("Éxito", "Función programada correctamente.", Alert.AlertType.INFORMATION);
                limpiarCampos(null);
                cargarDatos(null);
            } else {
                mostrarAlerta("No se puede programar", error, Alert.AlertType.ERROR);
            }
        } catch (DateTimeParseException e) {
            mostrarAlerta("Formato inválido", "Ingrese la hora en formato HH:mm (ej: 18:30).", Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            mostrarAlerta("Valor inválido", "El valor base debe ser un número válido.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void modificarFuncion(ActionEvent event) {
        try {
            if (txtCodigo.getText().isEmpty()) {
                mostrarAlerta("Advertencia", "Seleccione una función de la tabla para modificar.", Alert.AlertType.WARNING);
                return;
            }
            int codigo = Integer.parseInt(txtCodigo.getText());
            Pelicula peli = cmbPelicula.getValue();
            Sala sala = cmbSala.getValue();
            LocalDate fecha = datePickerFecha.getValue();
            LocalTime hora = LocalTime.parse(txtHora.getText().trim());
            double valor = Double.parseDouble(txtValor.getText().trim());
            EstadoFuncion estado = cmbEstado.getValue();

            String error = funcionController.modificarFuncion(codigo, peli, sala, fecha, hora, valor, estado);
            if (error == null) {
                mostrarAlerta("Éxito", "Función modificada correctamente.", Alert.AlertType.INFORMATION);

                actualizarDatos(null);
                limpiarCampos(null);
            } else {
                mostrarAlerta("No se puede modificar", error, Alert.AlertType.ERROR);
            }
        } catch (DateTimeParseException e) {
            mostrarAlerta("Formato inválido", "Ingrese la hora en formato HH:mm (ej: 18:30).", Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            mostrarAlerta("Valor inválido", "El valor base debe ser un número válido.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void consultarPorFecha(ActionEvent event) {
        LocalDate fecha = datePickerConsulta.getValue();
        if (fecha != null && funcionController != null) {
            tablaFunciones.setItems(FXCollections.observableArrayList(funcionController.consultarFuncionesPorFecha(fecha)));
        }
    }

    @FXML
    void limpiarCampos(ActionEvent event) {
        txtCodigo.clear();
        txtHora.clear();
        txtValor.clear();
        cmbPelicula.setValue(null);
        cmbSala.setValue(null);
        cmbEstado.setValue(EstadoFuncion.PROGRAMADA);
        datePickerFecha.setValue(LocalDate.now());
        tablaFunciones.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    void actualizarDatos(ActionEvent event) {
        if (funcionController != null) {

            // Actualizar las películas y salas disponibles
            cargarDatosCombo();

            // Actualizar todas las funciones de la tabla
            tablaFunciones.setItems(
                    FXCollections.observableArrayList(
                            funcionController.obtenerTodasLasFunciones()
                    )
            );

            // Forzar la actualización visual de las columnas
            tablaFunciones.refresh();

            mostrarAlerta(
                    "Datos actualizados",
                    "La información de las funciones se ha actualizado correctamente.",
                    Alert.AlertType.INFORMATION
            );
        }
    }
}