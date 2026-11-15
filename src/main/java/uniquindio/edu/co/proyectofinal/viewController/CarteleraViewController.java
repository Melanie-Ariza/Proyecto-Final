package uniquindio.edu.co.proyectofinal.viewController;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import uniquindio.edu.co.proyectofinal.controller.CarteleraController;
import uniquindio.edu.co.proyectofinal.model.Cine;
import uniquindio.edu.co.proyectofinal.model.Pelicula;
import java.io.InputStream;

public class CarteleraViewController {

    @FXML private FlowPane flowPanePeliculas;
    private CarteleraController carteleraController;

    public void setCine(Cine cine) {
        this.carteleraController = new CarteleraController(cine);
        cargarTarjetasPeliculas();
    }

    private void cargarTarjetasPeliculas() {
        if (carteleraController != null) {
            flowPanePeliculas.getChildren().clear();
            for (Pelicula p : carteleraController.obtenerPeliculasDisponibles()) {
                VBox tarjeta = new VBox(8);
                tarjeta.setPrefWidth(210);
                tarjeta.setPrefHeight(360);
                tarjeta.setAlignment(Pos.TOP_LEFT);
                tarjeta.setPadding(new Insets(12));
                tarjeta.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);");

                ImageView imageView = new ImageView();
                imageView.setFitWidth(186);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(false);

                // --- LÓGICA INTELIGENTE DE CARGA DE IMÁGENES ---
                InputStream imageStream = null;

                // 1. Intentar cargar la ruta definida en la película (si existe)
                if (p.getImagenRuta() != null && !p.getImagenRuta().isEmpty()) {
                    imageStream = getClass().getResourceAsStream(p.getImagenRuta());
                }

                // 2. Si no hay imagen asignada, buscar automáticamente por el nombre de la película
                if (imageStream == null && p.getNombre() != null) {
                    // Normalizar nombre: minúsculas, sin espacios ni caracteres especiales
                    String nombreLimpio = p.getNombre().toLowerCase()
                            .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
                            .replaceAll("[^a-z0-9]", "");

                    String rutaJpg = "/uniquindio/edu/co/proyectofinal/images/" + nombreLimpio + ".jpg";
                    String rutaPng = "/uniquindio/edu/co/proyectofinal/images/" + nombreLimpio + ".png";

                    imageStream = getClass().getResourceAsStream(rutaJpg);
                    if (imageStream == null) {
                        imageStream = getClass().getResourceAsStream(rutaPng);
                    }
                }

                // 3. Si aun así no se encuentra ninguna, usar la imagen por defecto
                if (imageStream == null) {
                    imageStream = getClass().getResourceAsStream("/uniquindio/edu/co/proyectofinal/images/default.jpg");
                }

                // Asignar la imagen al ImageView si se encontró un flujo válido
                if (imageStream != null) {
                    imageView.setImage(new Image(imageStream));
                }
                // -----------------------------------------------

                Label lblClasificacion = new Label(p.getClasificacion().toString());
                lblClasificacion.setFont(Font.font("System", FontWeight.BOLD, 10));
                lblClasificacion.setTextFill(Color.WHITE);
                lblClasificacion.setStyle("-fx-background-color: #C8102E; -fx-background-radius: 4; -fx-padding: 3 8 3 8;");

                Label lblNombre = new Label(p.getNombre());
                lblNombre.setFont(Font.font("System", FontWeight.BOLD, 14));
                lblNombre.setTextFill(Color.web("#C8102E"));
                lblNombre.setWrapText(true);

                Label lblGenero = new Label("Género: " + p.getGenero());
                lblGenero.setFont(Font.font("System", 11));
                lblGenero.setTextFill(Color.web("#555555"));
                lblGenero.setWrapText(true);

                Label lblDuracion = new Label("Duración: " + p.getDuracion() + " min");
                lblDuracion.setFont(Font.font("System", 11));
                lblDuracion.setTextFill(Color.web("#777777"));

                tarjeta.getChildren().addAll(imageView, lblClasificacion, lblNombre, lblGenero, lblDuracion);
                flowPanePeliculas.getChildren().add(tarjeta);
            }
        }
    }
}