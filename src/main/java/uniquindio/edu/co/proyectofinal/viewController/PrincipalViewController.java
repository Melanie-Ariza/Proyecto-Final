package uniquindio.edu.co.proyectofinal.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import uniquindio.edu.co.proyectofinal.controller.PrincipalController;
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
    @FXML private Button btnPersonas;
    @FXML private Button btnGestionPeliculas;
    @FXML private Button btnReportes;
    @FXML private Button btnSalir;

    private Node vistaReportesInicial;
    private PrincipalController principalController;

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
        this.principalController = new PrincipalController();
        this.vistaReportesInicial = borderPanePrincipal.getCenter();

        actualizarDatosEnPantalla();
    }

    @FXML
    void actualizarReportes(ActionEvent event) {
        actualizarDatosEnPantalla();
    }

    private void actualizarDatosEnPantalla() {
        if (principalController != null) {
            lblTotalEntradas.setText(String.valueOf(principalController.obtenerTotalEntradasVendidas()));
            lblTotalIngresos.setText("$ " + String.format("%,.0f", principalController.obtenerTotalIngresosGenerados()));

            Pelicula topPelicula = principalController.obtenerPeliculaMasVendida();
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
            carteleraViewController.setCine(principalController.getCine());
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
            funcionViewController.setCine(principalController.getCine());
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
            ventaViewController.setCine(principalController.getCine());
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
            salasViewController.setCine(principalController.getCine());
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
                gestionPersonasViewController = loader.getController();
            }
            gestionPersonasViewController.setCine(principalController.getCine());
            gestionPersonasViewController.actualizarTablas();
            borderPanePrincipal.setCenter(nodoPersonas);
            activarBotonMenuPersonalizado(btnPersonas);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar la gestión de personas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void mostrarGestionPeliculas(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/uniquindio/edu/co/proyectofinal/gestion-peliculas-view.fxml"));
        Parent vista = loader.load();
        GestionPeliculasViewController controller = loader.getController();
        controller.setCine(principalController.getCine());
        borderPanePrincipal.setCenter(vista);
        activarBotonMenuPersonalizado(btnGestionPeliculas);
    }

    @FXML
    void mostrarReportes(ActionEvent event) {
        borderPanePrincipal.setCenter(vistaReportesInicial);
        actualizarDatosEnPantalla();
        activarBotonMenuPersonalizado(btnReportes);
    }

    @FXML
    void salirAplicacion(ActionEvent event) {
        Stage stage = (Stage) borderPanePrincipal.getScene().getWindow();
        stage.close();
    }

    private void activarBotonMenuPersonalizado(Button botonActivo) {
        Button[] botones = {btnGestionPeliculas, btnCartelera, btnFunciones, btnVentas, btnSalas, btnPersonas, btnReportes};
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
}