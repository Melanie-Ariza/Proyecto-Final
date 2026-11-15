package uniquindio.edu.co.proyectofinal.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import uniquindio.edu.co.proyectofinal.model.*;
import java.io.IOException;

public class PrincipalViewController {

    @FXML private Label lblTotalEntradas;
    @FXML private Label lblTotalIngresos;
    @FXML private Label lblPeliculaMasVendida;
    @FXML private Label lblDetallePeliculaTop;

    @FXML private BorderPane borderPanePrincipal;
    @FXML private Button btnCartelera;
    @FXML private Button btnFunciones;
    @FXML private Button btnVentas;
    @FXML private Button btnSalas;
    @FXML private Button btnGestionVentas;
    @FXML private Button btnPersonas;
    @FXML private Button btnGestionPeliculas;
    @FXML private Button btnReportes;
    @FXML private Button btnCambiarRol;
    @FXML private Button btnSalir;

    private Node vistaReportesInicial;
    private Cine cine;
    private boolean esAdministrador = true; // true = ADMIN, false = OPERADOR

    // Caché de vistas para optimizar navegación
    private Parent nodoCartelera;
    private CarteleraViewController carteleraViewController;

    private Parent nodoFunciones;
    private FuncionViewController funcionViewController;

    private Parent nodoVentas;
    private VentaViewController ventaViewController;

    private Parent nodoSalas;
    private SalasViewController salasViewController;

    private Parent nodoPersonas;
    private GestionPersonasViewController gestionPersonasViewController;

    @FXML
    public void initialize() {
        this.cine = new Cine(1, "CINEMAUQ");
        this.vistaReportesInicial = borderPanePrincipal.getCenter();
        actualizarDatosEnPantalla();
        actualizarPermisosInterfaz();
    }

    @FXML
    void actualizarReportes(ActionEvent event) {
        if (esAdministrador) {
            actualizarDatosEnPantalla();
        }
    }

    private void actualizarDatosEnPantalla() {
        if (cine != null && lblTotalEntradas != null) {
            lblTotalEntradas.setText(String.valueOf(cine.consultarTotalEntradasVendidas()));
            lblTotalIngresos.setText("$ " + String.format("%,.0f", cine.consultarTotalIngresosGenerados()));

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
    void mostrarCartelera(ActionEvent event) {
        try {
            if (nodoCartelera == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/proyectofinal/cartelera-view.fxml"));
                nodoCartelera = loader.load();
                carteleraViewController = loader.getController();
            }
            carteleraViewController.setCine(this.cine);
            borderPanePrincipal.setCenter(nodoCartelera);
            activarBotonMenuPersonalizado(btnCartelera);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la cartelera: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void mostrarFunciones(ActionEvent event) {
        try {
            if (nodoFunciones == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/proyectofinal/funcion-view.fxml"));
                nodoFunciones = loader.load();
                funcionViewController = loader.getController();
            }
            funcionViewController.setCine(cine);
            borderPanePrincipal.setCenter(nodoFunciones);
            activarBotonMenuPersonalizado(btnFunciones);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la vista de funciones: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void mostrarVentas(ActionEvent event) {
        try {
            if (nodoVentas == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/proyectofinal/ventas-view.fxml"));
                nodoVentas = loader.load();
                ventaViewController = loader.getController();
            }
            ventaViewController.setCine(cine);
            borderPanePrincipal.setCenter(nodoVentas);
            activarBotonMenuPersonalizado(btnVentas);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la ventana de ventas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void mostrarSalas(ActionEvent event) {
        try {
            if (nodoSalas == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/proyectofinal/salas-view.fxml"));
                nodoSalas = loader.load();
                salasViewController = loader.getController();
            }
            salasViewController.setCine(cine);
            salasViewController.setModoAdministrador(esAdministrador); // Restringe o habilita el CRUD de salas según el rol
            borderPanePrincipal.setCenter(nodoSalas);
            activarBotonMenuPersonalizado(btnSalas);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar las salas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    @FXML
    void mostrarVentasPersona(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/proyectofinal/ventas-persona-view.fxml"));
            Parent root = loader.load();
            VentasPersonaViewController controller = loader.getController();
            controller.setCine(cine);

            borderPanePrincipal.setCenter(root);

            // Activar el estilo rojo en el botón de Ventas y quitarlo de los demás
            activarBotonMenuPersonalizado(btnGestionVentas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void mostrarGestionPersonas(ActionEvent event) {
        if (!esAdministrador) {
            mostrarAlerta("Acceso Denegado", "Los operadores no tienen permiso para gestionar personas.", Alert.AlertType.WARNING);
            return;
        }
        try {
            if (nodoPersonas == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/proyectofinal/gestion-personas-view.fxml"));
                nodoPersonas = loader.load();
                gestionPersonasViewController = loader.getController();
            }
            gestionPersonasViewController.setCine(cine);
            gestionPersonasViewController.actualizarTablas();
            borderPanePrincipal.setCenter(nodoPersonas);
            activarBotonMenuPersonalizado(btnPersonas);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la gestión de personas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void mostrarGestionPeliculas(ActionEvent event) {
        if (!esAdministrador) {
            mostrarAlerta("Acceso Denegado", "Los operadores no tienen permiso para gestionar películas.", Alert.AlertType.WARNING);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/proyectofinal/gestion-peliculas-view.fxml"));
            Parent vista = loader.load();
            GestionPeliculasViewController controller = loader.getController();
            controller.setCine(cine);
            borderPanePrincipal.setCenter(vista);
            activarBotonMenuPersonalizado(btnGestionPeliculas);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la gestión de películas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void mostrarReportes(ActionEvent event) {
        if (!esAdministrador) {
            mostrarAlerta("Acceso Denegado", "Los operadores no tienen acceso a los reportes administrativos.", Alert.AlertType.WARNING);
            return;
        }
        borderPanePrincipal.setCenter(vistaReportesInicial);
        actualizarDatosEnPantalla();
        activarBotonMenuPersonalizado(btnReportes);
    }

    @FXML
    void cambiarRol(ActionEvent event) {
        esAdministrador = !esAdministrador;

        if (esAdministrador) {
            btnCambiarRol.setText("ROL: ADMIN");
            btnCambiarRol.setStyle("-fx-background-color: #27AE60; -fx-background-radius: 20; -fx-font-weight: bold; -fx-cursor: hand; -fx-text-fill: white;");
        } else {
            btnCambiarRol.setText("ROL: OPERADOR");
            btnCambiarRol.setStyle("-fx-background-color: #E67E22; -fx-background-radius: 20; -fx-font-weight: bold; -fx-cursor: hand; -fx-text-fill: white;");

            // Si el operador cambia y estaba en una vista prohibida, lo redirigimos automáticamente a Ventas
            mostrarVentas(null);
        }

        actualizarPermisosInterfaz();
    }

    private void actualizarPermisosInterfaz() {
        // Ocultar o mostrar botones según el rol actual
        if (btnGestionPeliculas != null) btnGestionPeliculas.setVisible(esAdministrador);
        if (btnPersonas != null) btnPersonas.setVisible(esAdministrador);
        if (btnReportes != null) btnReportes.setVisible(esAdministrador);
    }

    @FXML
    void salirAplicacion(ActionEvent event) {
        Stage stage = (Stage) borderPanePrincipal.getScene().getWindow();
        stage.close();
    }

    private void activarBotonMenuPersonalizado(Button botonActivo) {
        Button[] botones = {btnGestionPeliculas, btnCartelera, btnFunciones, btnVentas, btnSalas, btnPersonas, btnReportes, btnGestionVentas};
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

    public Cine getCine() {
        return cine;
    }
}