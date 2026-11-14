package uniquindio.edu.co.proyectofinal.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import uniquindio.edu.co.proyectofinal.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PrincipalController {

    @FXML private Label lblTotalEntradas;
    @FXML private Label lblTotalIngresos;
    @FXML private Label lblPeliculaMasVendida;
    @FXML private Label lblDetallePeliculaTop;

    // Controles para Programar Función
    @FXML private ComboBox<Pelicula> cmbPeliculaProg;
    @FXML private ComboBox<Sala> cmbSalaProg;
    @FXML private DatePicker datePickerProg;
    @FXML private TextField txtHoraProg;
    @FXML private TextField txtValorProg;

    // Controles para consulta de funciones por fecha
    @FXML private DatePicker datePickerFunciones;
    @FXML private TableView<Funcion> tablaFuncionesFecha;
    @FXML private TableColumn<Funcion, Integer> colCodigoFuncion;
    @FXML private TableColumn<Funcion, String> colPeliculaFuncion;
    @FXML private TableColumn<Funcion, String> colSalaFuncion;
    @FXML private TableColumn<Funcion, String> colHoraFuncion;
    @FXML private TableColumn<Funcion, String> colEstadoFuncion;

    @FXML private BorderPane borderPanePrincipal;
    @FXML private Button btnVentas;
    @FXML private Button btnReportes;
    @FXML private Button btnCartelera;
    @FXML private Button btnSalas;
    @FXML private Button btnPersonas;
    @FXML private Button btnSalir;
    @FXML private Button btnGestionPeliculas;

    private Node vistaReportesInicial;
    private Cine cine;

    private Parent nodoCartelera;
    private CarteleraController carteleraController;

    private Parent nodoVentas;
    private VentaController ventaController;

    private Parent nodoSalas;
    private SalasController salasController;

    private Parent nodoPersonas;
    private GestionPersonasController gestionPersonasController;

    private Parent nodoPeliculas;
    private GestionPeliculasController gestionPeliculasController;

    @FXML
    public void initialize() {
        this.cine = new Cine(1, "CINEMAUQ");
        this.vistaReportesInicial = borderPanePrincipal.getCenter();

        configurarTablaFunciones();
        datePickerFunciones.setValue(LocalDate.now());
        datePickerProg.setValue(LocalDate.now());

        // ---------------------------------------------------------
        // CONVERTIDOR PARA QUE EL COMBOBOX DE PELÍCULAS MUESTRE TEXTO CLARO
        // ---------------------------------------------------------
        cmbPeliculaProg.setConverter(new StringConverter<Pelicula>() {
            @Override
            public String toString(Pelicula p) {
                return (p == null) ? "" : p.getNombre() + " (" + p.getGenero() + ")";
            }
            @Override
            public Pelicula fromString(String string) {
                return null;
            }
        });

        // ---------------------------------------------------------
        // CONVERTIDOR PARA QUE EL COMBOBOX DE SALAS MUESTRE TEXTO CLARO
        // ---------------------------------------------------------
        cmbSalaProg.setConverter(new StringConverter<Sala>() {
            @Override
            public String toString(Sala s) {
                return (s == null) ? "" : "Sala #" + s.getNumero() + " - " + s.getColeccionAsientos() + " (Cap: " + s.getCapacidad() + ")";
            }
            @Override
            public Sala fromString(String string) {
                return null;
            }
        });

        cargarCombosProgramacion();
        actualizarDatosEnPantalla();
    }

    private void configurarTablaFunciones() {
        colCodigoFuncion.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colPeliculaFuncion.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPelicula().getNombre()));
        colSalaFuncion.setCellValueFactory(cellData ->
                new SimpleStringProperty("Sala #" + cellData.getValue().getSala().getNumero()));
        colHoraFuncion.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getHora().toString()));
        colEstadoFuncion.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstadoFuncion().toString()));
    }

    public void cargarCombosProgramacion() {
        if (cine != null) {
            if (cmbPeliculaProg != null) {
                cmbPeliculaProg.setItems(FXCollections.observableArrayList(cine.consultarPeliculasDisponibles()));
            }
            if (cmbSalaProg != null) {
                cmbSalaProg.setItems(FXCollections.observableArrayList(cine.getListaSalas()));
            }
        }
    }

    @FXML
    void actualizarReportes(ActionEvent event) {
        cargarCombosProgramacion();
        actualizarDatosEnPantalla();
        consultarFuncionesPorFecha(null);
    }

    private void actualizarDatosEnPantalla() {
        if (cine != null) {
            int entradas = cine.consultarTotalEntradasVendidas();
            double ingresos = cine.consultarTotalIngresosGenerados();

            lblTotalEntradas.setText(String.valueOf(entradas));
            lblTotalIngresos.setText("$ " + String.format("%,.0f", ingresos));

            // Consultar Película Más Vendida
            Pelicula topPelicula = cine.identificarPeliculaMasVendida();
            if (topPelicula != null) {
                lblPeliculaMasVendida.setText(topPelicula.getNombre());
                lblDetallePeliculaTop.setText("Género: " + topPelicula.getGenero() + " • " + topPelicula.getDuracion() + " min");
            } else {
                lblPeliculaMasVendida.setText("Sin ventas aún");
                lblDetallePeliculaTop.setText("Mayor demanda en cartelera");
            }
        }
    }

    @FXML
    void guardarNuevaFuncion(ActionEvent event) {
        try {
            Pelicula peliculaSeleccionada = cmbPeliculaProg.getValue();
            Sala salaSeleccionada = cmbSalaProg.getValue();
            LocalDate fecha = datePickerProg.getValue();
            String horaTexto = txtHoraProg.getText().trim();
            String valorTexto = txtValorProg.getText().trim();

            if (peliculaSeleccionada == null || salaSeleccionada == null || fecha == null || horaTexto.isEmpty() || valorTexto.isEmpty()) {
                mostrarAlerta("Campos Incompletos", "Por favor completa todos los campos para programar la función.", Alert.AlertType.WARNING);
                return;
            }

            LocalTime hora = LocalTime.parse(horaTexto); // Formato HH:mm
            double valorBase = Double.parseDouble(valorTexto);

            int codigoFuncion = cine.getListaFunciones().size() + 1;

            Funcion nuevaFuncion = new Funcion(
                    codigoFuncion,
                    peliculaSeleccionada,
                    salaSeleccionada,
                    hora,
                    fecha,
                    valorBase,
                    EstadoFuncion.PROGRAMADA
            );

            boolean programada = cine.programarFuncion(peliculaSeleccionada, nuevaFuncion);

            if (programada) {
                mostrarAlerta("Éxito", "¡Función #" + codigoFuncion + " programada con éxito!", Alert.AlertType.INFORMATION);

                txtHoraProg.clear();
                txtValorProg.clear();
                cmbPeliculaProg.setValue(null);
                cmbSalaProg.setValue(null);

                datePickerFunciones.setValue(fecha);
                consultarFuncionesPorFecha(null);
                actualizarDatosEnPantalla();
            } else {
                mostrarAlerta("Error", "No se pudo programar. Verifique que la sala esté disponible y la película activa.", Alert.AlertType.ERROR);
            }

        } catch (java.time.format.DateTimeParseException e) {
            mostrarAlerta("Formato de Hora Inválido", "Ingrese la hora en formato correcto, por ejemplo: 18:30", Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            mostrarAlerta("Valor Inválido", "El valor base debe ser un número válido.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void consultarFuncionesPorFecha(ActionEvent event) {
        if (cine != null && datePickerFunciones.getValue() != null) {
            LocalDate fechaSeleccionada = datePickerFunciones.getValue();
            List<Funcion> funcionesEncontradas = cine.consultarFuncionesPorFecha(fechaSeleccionada);
            tablaFuncionesFecha.setItems(FXCollections.observableArrayList(funcionesEncontradas));
        } else {
            mostrarAlerta("Fecha requerida", "Por favor selecciona una fecha para consultar las funciones.", Alert.AlertType.WARNING);
        }
    }

    // --- NAVEGACIÓN ENTRE VISTAS ---
    @FXML
    void mostrarVentas(ActionEvent event) {
        try {
            if (nodoVentas == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/proyectofinal/ventas-view.fxml"));
                nodoVentas = loader.load();
                ventaController = loader.getController();
            }
            ventaController.setCine(this.cine);

            borderPanePrincipal.setCenter(nodoVentas);
            activarBotonMenuPersonalizado(btnVentas);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana de ventas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void mostrarReportes(ActionEvent event) {
        borderPanePrincipal.setCenter(vistaReportesInicial);
        actualizarDatosEnPantalla();
        cargarCombosProgramacion();
        consultarFuncionesPorFecha(null);
        activarBotonMenuPersonalizado(btnReportes);
    }

    @FXML
    void mostrarCartelera(ActionEvent event) {
        try {
            if (nodoCartelera == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/proyectofinal/cartelera-view.fxml"));
                nodoCartelera = loader.load();
                carteleraController = loader.getController();
            }
            carteleraController.setCine(this.cine); // Refresca datos si hubo cambios
            borderPanePrincipal.setCenter(nodoCartelera);
            activarBotonMenuPersonalizado(btnCartelera);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la cartelera: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void mostrarSalas(ActionEvent event) {
        try {
            if (nodoSalas == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/proyectofinal/salas-view.fxml"));
                nodoSalas = loader.load();
                salasController = loader.getController();
            }
            salasController.setCine(this.cine);
            borderPanePrincipal.setCenter(nodoSalas);
            activarBotonMenuPersonalizado(btnSalas);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar las salas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void mostrarGestionPersonas(ActionEvent event) {
        try {
            if (nodoPersonas == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/proyectofinal/gestion-personas-view.fxml"));
                nodoPersonas = loader.load();
                gestionPersonasController = loader.getController();
            }
            gestionPersonasController.setCine(this.cine);
            gestionPersonasController.actualizarTablas(); // Refresca tablas al entrar
            borderPanePrincipal.setCenter(nodoPersonas);
            activarBotonMenuPersonalizado(btnPersonas);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la gestión de personas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void salirAplicacion(ActionEvent event) {
        Stage stage = (Stage) borderPanePrincipal.getScene().getWindow();
        stage.close();
    }

    private void activarBotonMenuPersonalizado(Button botonActivo) {
        Button[] botones = {btnCartelera, btnVentas, btnSalas, btnPersonas, btnReportes};
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

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    void mostrarGestionPeliculas(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/proyectofinal/gestion-peliculas-view.fxml"));
        Parent vista = loader.load();
        GestionPeliculasController controller = loader.getController();
        controller.setCine(this.cine);

        borderPanePrincipal.setCenter(vista);
        activarBotonMenuPersonalizado(btnGestionPeliculas);
    }
}