package uniquindio.edu.co.proyectofinal.viewController;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import uniquindio.edu.co.proyectofinal.controller.SalasController;
import uniquindio.edu.co.proyectofinal.model.*;

public class SalasViewController {

    @FXML private ListView<Sala> listSalas;
    @FXML private GridPane gridAsientos;
    @FXML private Label lblTituloSala;

    // Componentes del modo gestión
    @FXML private Button btnModificarSalas;
    @FXML private Button btnCambiarModo;
    @FXML private VBox panelVistaAsientos;
    @FXML private VBox panelGestionSalas;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNumero;
    @FXML private TextField txtCapacidad;
    @FXML private ComboBox<EstadoSala> cmbEstadoSala;
    @FXML private ComboBox<ColeccionAsientos> cmbColeccionAsientos;
    @FXML private Label lblMensajeSala;

    private SalasController salasController;
    private boolean modoGestion = false;

    public void setCine(Cine cine) {
        this.salasController = new SalasController(cine);
        configurarCombos();
        configurarListaSalas();
    }

    private void configurarCombos() {
        if (EstadoSala.values().length > 0) {
            cmbEstadoSala.getItems().setAll(EstadoSala.values());
        }
        if (ColeccionAsientos.values().length > 0) {
            cmbColeccionAsientos.getItems().setAll(ColeccionAsientos.values());
        }
    }

    private void configurarListaSalas() {
        if (salasController != null) {
            actualizarLista();

            listSalas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, salaSeleccionada) -> {
                if (salaSeleccionada != null) {
                    if (!modoGestion) {
                        mostrarMapaAsientos(salaSeleccionada);
                    } else {
                        // Si estamos en modo gestión y el usuario hace clic en una sala, cargamos sus datos para modificar
                        cargarDatosSalaEnFormulario(salaSeleccionada);
                    }
                }
            });

            // Por defecto en modo vista seleccionamos la primera sala si existe
            if (!listSalas.getItems().isEmpty() && !modoGestion) {
                listSalas.getSelectionModel().select(0);
            }
        }
    }

    private void actualizarLista() {
        listSalas.getItems().setAll(salasController.obtenerSalas());
    }

    @FXML
    private void cambiarModo() {
        modoGestion = !modoGestion;
        if (modoGestion) {
            // Cambiar a MODO GESTIÓN
            btnCambiarModo.setText("Modo: Gestión");
            btnCambiarModo.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");
            panelVistaAsientos.setVisible(false);
            panelVistaAsientos.setManaged(false);
            panelGestionSalas.setVisible(true);
            panelGestionSalas.setManaged(true);

            // Limpiamos la selección de la lista para que los campos queden vacíos listos para registrar
            listSalas.getSelectionModel().clearSelection();
            limpiarCamposParaNuevaSala();

        } else {
            // Cambiar a MODO VISTA
            btnCambiarModo.setText("Modo: Vista");
            btnCambiarModo.setStyle("-fx-background-color: #C8102E; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");
            panelGestionSalas.setVisible(false);
            panelGestionSalas.setManaged(false);
            panelVistaAsientos.setVisible(true);
            panelVistaAsientos.setManaged(true);

            // Volver a seleccionar la primera sala en el modo vista
            if (!listSalas.getItems().isEmpty()) {
                listSalas.getSelectionModel().select(0);
            }
        }
    }

    /**
     * Limpia los campos y asigna automáticamente el siguiente código consecutivo, dejándolo bloqueado.
     */
    @FXML
    private void limpiarCamposSala() {
        limpiarCamposParaNuevaSala();
        listSalas.getSelectionModel().clearSelection();
    }

    private void limpiarCamposParaNuevaSala() {
        // Calcular el siguiente código correlativo basado en las salas existentes
        int siguienteCodigo = 1;
        if (salasController != null && salasController.obtenerSalas() != null) {
            for (Sala s : salasController.obtenerSalas()) {
                if (s.getCodigo() >= siguienteCodigo) {
                    siguienteCodigo = s.getCodigo() + 1;
                }
            }
        }

        txtCodigo.setText(String.valueOf(siguienteCodigo));
        txtCodigo.setEditable(false); // El código se autogenera y no se deja modificar
        txtCodigo.setStyle("-fx-background-color: #E9ECEF; -fx-text-fill: #495057;"); // Apariencia de campo bloqueado

        txtNumero.clear();
        txtCapacidad.clear();
        cmbEstadoSala.getSelectionModel().clearSelection();
        cmbColeccionAsientos.getSelectionModel().clearSelection();
        lblMensajeSala.setText("");
    }

    private void cargarDatosSalaEnFormulario(Sala sala) {
        txtCodigo.setText(String.valueOf(sala.getCodigo()));
        txtCodigo.setEditable(false); // Al modificar, el código tampoco debe alterarse
        txtCodigo.setStyle("-fx-background-color: #E9ECEF; -fx-text-fill: #495057;");

        txtNumero.setText(String.valueOf(sala.getNumero()));
        txtCapacidad.setText(String.valueOf(sala.getCapacidad()));
        cmbEstadoSala.setValue(sala.getEstado());
        cmbColeccionAsientos.setValue(sala.getColeccionAsientos());
        lblMensajeSala.setText("");
    }

    private void mostrarMapaAsientos(Sala sala) {
        gridAsientos.getChildren().clear();
        lblTituloSala.setText("Sala #" + sala.getNumero() + " (" + sala.getColeccionAsientos() + ") - Capacidad: " + sala.getCapacidad());

        int columna = 0;
        int fila = 0;
        int maxColumnas = 10;

        for (Asiento asiento : sala.getListaAsientos()) {
            Button btnAsiento = new Button(asiento.obtenerPuesto());
            btnAsiento.setPrefWidth(55);
            btnAsiento.setPrefHeight(40);
            btnAsiento.setAlignment(Pos.CENTER);

            if (asiento.getEstado() == EstadoAsiento.DISPONIBLE) {
                btnAsiento.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
            } else {
                btnAsiento.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
            }

            gridAsientos.add(btnAsiento, columna, fila);
            columna++;
            if (columna >= maxColumnas) {
                columna = 0;
                fila++;
            }
        }
    }

    @FXML
    private void registrarSala() {
        try {
            int codigo = Integer.parseInt(txtCodigo.getText());
            int numero = Integer.parseInt(txtNumero.getText());
            int capacidad = Integer.parseInt(txtCapacidad.getText());
            EstadoSala estado = cmbEstadoSala.getValue();
            ColeccionAsientos coleccion = cmbColeccionAsientos.getValue();

            if (estado == null || coleccion == null || txtNumero.getText().isEmpty() || txtCapacidad.getText().isEmpty()) {
                lblMensajeSala.setStyle("-fx-text-fill: #C8102E;");
                lblMensajeSala.setText("Por favor complete todos los campos obligatorios.");
                return;
            }

            boolean exito = salasController.registrarSala(codigo, numero, capacidad, estado, coleccion);
            if (exito) {
                lblMensajeSala.setStyle("-fx-text-fill: #2ECC71;");
                lblMensajeSala.setText("Sala registrada con éxito.");
                actualizarLista();
                limpiarCamposSala(); // Limpia y autogenera el siguiente código
            } else {
                lblMensajeSala.setStyle("-fx-text-fill: #C8102E;");
                lblMensajeSala.setText("Error: Ya existe una sala registrada con ese código o número.");
            }
        } catch (NumberFormatException e) {
            lblMensajeSala.setStyle("-fx-text-fill: #C8102E;");
            lblMensajeSala.setText("Ingrese valores numéricos válidos en Número y Capacidad.");
        }
    }

    @FXML
    private void modificarSala() {
        Sala seleccionada = listSalas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            lblMensajeSala.setStyle("-fx-text-fill: #C8102E;");
            lblMensajeSala.setText("Seleccione una sala de la lista para modificar.");
            return;
        }

        try {
            int numero = Integer.parseInt(txtNumero.getText());
            int capacidad = Integer.parseInt(txtCapacidad.getText());
            EstadoSala estado = cmbEstadoSala.getValue();
            ColeccionAsientos coleccion = cmbColeccionAsientos.getValue();

            boolean exito = salasController.modificarSala(seleccionada, numero, capacidad, estado, coleccion);
            if (exito) {
                lblMensajeSala.setStyle("-fx-text-fill: #2ECC71;");
                lblMensajeSala.setText("Sala modificada correctamente.");
                actualizarLista();
            } else {
                lblMensajeSala.setStyle("-fx-text-fill: #C8102E;");
                lblMensajeSala.setText("No se pudo modificar: Ya existe otra sala con ese número.");
            }
        } catch (NumberFormatException e) {
            lblMensajeSala.setStyle("-fx-text-fill: #C8102E;");
            lblMensajeSala.setText("Ingrese valores numéricos válidos.");
        }
    }

    @FXML
    private void deshabilitarSala() {
        Sala seleccionada = listSalas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            lblMensajeSala.setStyle("-fx-text-fill: #C8102E;");
            lblMensajeSala.setText("Seleccione una sala para deshabilitar.");
            return;
        }

        boolean exito = salasController.deshabilitarSala(seleccionada);
        if (exito) {
            lblMensajeSala.setStyle("-fx-text-fill: #2ECC71;");
            lblMensajeSala.setText("Sala deshabilitada correctamente.");
            actualizarLista();
            limpiarCamposSala();
        } else {
            lblMensajeSala.setStyle("-fx-text-fill: #C8102E;");
            lblMensajeSala.setText("No se pudo deshabilitar la sala.");
        }
    }

    private boolean esAdmin = true;

    public void setModoAdministrador(boolean esAdmin) {
        this.esAdmin = esAdmin;
        // Si es operador, ocultamos el botón de cambiar a modo gestión y deshabilitamos el panel CRUD
        if (!esAdmin) {
            if (btnCambiarModo != null) btnCambiarModo.setVisible(false);
            // Forzamos que siempre esté en modo vista de asientos
            if (panelGestionSalas != null) {
                panelGestionSalas.setVisible(false);
                panelGestionSalas.setManaged(false);
            }
            if (panelVistaAsientos != null) {
                panelVistaAsientos.setVisible(true);
                panelVistaAsientos.setManaged(true);
            }
        } else {
            if (btnCambiarModo != null) btnCambiarModo.setVisible(true);
        }
    }
}