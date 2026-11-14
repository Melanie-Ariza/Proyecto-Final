package uniquindio.edu.co.proyectofinal.viewController;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import uniquindio.edu.co.proyectofinal.Excepciones.PeliculaDuplicadaException.PeliculaDuplicadaException;
import uniquindio.edu.co.proyectofinal.controller.GestionPeliculasController;
import uniquindio.edu.co.proyectofinal.model.Cine;
import uniquindio.edu.co.proyectofinal.model.Clasificacion;
import uniquindio.edu.co.proyectofinal.model.Pelicula;

public class GestionPeliculasViewController {

    @FXML private TableView<Pelicula> tablaPeliculas;
    @FXML private TableColumn<Pelicula, Integer> colCodigo;
    @FXML private TableColumn<Pelicula, String> colNombre;
    @FXML private TableColumn<Pelicula, String> colGenero;
    @FXML private TableColumn<Pelicula, Integer> colDuracion;
    @FXML private TableColumn<Pelicula, Clasificacion> colClasificacion;
    @FXML private TableColumn<Pelicula, String> colEstado;

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtGenero;
    @FXML private TextField txtDuracion;
    @FXML private TextField txtEstado;
    @FXML private ComboBox<Clasificacion> comboClasificacion;

    private GestionPeliculasController gestionPeliculasController;

    public void setCine(Cine cine) {
        this.gestionPeliculasController = new GestionPeliculasController(cine);
        actualizarTabla();
    }

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colClasificacion.setCellValueFactory(new PropertyValueFactory<>("clasificacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        comboClasificacion.setItems(FXCollections.observableArrayList(Clasificacion.values()));

        tablaPeliculas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, peliculaSeleccionada) -> {
            if (peliculaSeleccionada != null) {
                txtCodigo.setText(String.valueOf(peliculaSeleccionada.getCodigo()));
                txtNombre.setText(peliculaSeleccionada.getNombre());
                txtGenero.setText(peliculaSeleccionada.getGenero());
                txtDuracion.setText(String.valueOf(peliculaSeleccionada.getDuracion()));
                comboClasificacion.setValue(peliculaSeleccionada.getClasificacion());
                txtEstado.setText(peliculaSeleccionada.getEstado());
            }
        });
    }

    public void actualizarTabla() {
        if (gestionPeliculasController != null) {
            tablaPeliculas.setItems(FXCollections.observableArrayList(gestionPeliculasController.obtenerPeliculas()));
            tablaPeliculas.refresh();
        }
    }

    @FXML
    void registrarPelicula(ActionEvent event) {
        try {
            if (txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() || txtGenero.getText().isEmpty()
                    || txtDuracion.getText().isEmpty() || comboClasificacion.getValue() == null) {
                mostrarAlerta("Campos Incompletos", "Por favor completa todos los campos para registrar la película.", Alert.AlertType.WARNING);
                return;
            }

            gestionPeliculasController.registrarPelicula(
                    Integer.parseInt(txtCodigo.getText().trim()),
                    txtNombre.getText().trim(),
                    txtGenero.getText().trim(),
                    Integer.parseInt(txtDuracion.getText().trim()),
                    comboClasificacion.getValue(),
                    txtEstado.getText().trim()
            );

            mostrarAlerta("Éxito", "¡Película registrada correctamente en el sistema!", Alert.AlertType.INFORMATION);
            actualizarTabla();
            limpiarCampos(null);

        } catch (PeliculaDuplicadaException e) {
            mostrarAlerta("Película Duplicada", e.getMessage(), Alert.AlertType.WARNING);
        } catch (NumberFormatException e) {
            mostrarAlerta("Formato Inválido", "El código y la duración deben ser números enteros válidos.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void modificarPelicula(ActionEvent event) {
        try {
            Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
            if (seleccionada == null) {
                mostrarAlerta("Advertencia", "Seleccione una película de la tabla para modificar.", Alert.AlertType.WARNING);
                return;
            }

            if (txtNombre.getText().isEmpty() || txtGenero.getText().isEmpty() || txtDuracion.getText().isEmpty() || comboClasificacion.getValue() == null) {
                mostrarAlerta("Campos Incompletos", "Por favor complete todos los campos de la película.", Alert.AlertType.WARNING);
                return;
            }

            boolean actualizada = gestionPeliculasController.modificarPelicula(
                    seleccionada,
                    txtNombre.getText().trim(),
                    txtGenero.getText().trim(),
                    Integer.parseInt(txtDuracion.getText().trim()),
                    comboClasificacion.getValue(),
                    txtEstado.getText().trim()
            );

            if (actualizada) {
                mostrarAlerta("Éxito", "Película modificada correctamente.", Alert.AlertType.INFORMATION);
                actualizarTabla();
                limpiarCampos(null);
            } else {
                mostrarAlerta("Error", "No se pudo modificar la película.", Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Formato Inválido", "La duración debe ser un número entero válido.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void deshabilitarPelicula(ActionEvent event) {
        Pelicula seleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (seleccionada != null && gestionPeliculasController != null) {
            if (gestionPeliculasController.deshabilitarPelicula(seleccionada.getCodigo())) {
                mostrarAlerta("Éxito", "Película deshabilitada correctamente.", Alert.AlertType.INFORMATION);
                actualizarTabla();
                limpiarCampos(null);
            } else {
                mostrarAlerta("Error", "No se pudo deshabilitar la película.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione una película de la tabla para deshabilitar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    void limpiarCampos(ActionEvent event) {
        txtCodigo.clear();
        txtNombre.clear();
        txtGenero.clear();
        txtDuracion.clear();
        txtEstado.setText("ACTIVO");
        comboClasificacion.setValue(null);
        tablaPeliculas.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
