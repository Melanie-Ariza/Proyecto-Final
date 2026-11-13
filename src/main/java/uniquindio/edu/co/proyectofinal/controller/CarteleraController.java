package uniquindio.edu.co.proyectofinal.controller;

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
import uniquindio.edu.co.proyectofinal.model.Cine;
import uniquindio.edu.co.proyectofinal.model.Pelicula;

import java.io.InputStream;

public class CarteleraController {

    @FXML private FlowPane flowPanePeliculas;
    private Cine cine;

    public void setCine(Cine cine) {
        this.cine = cine;
        cargarTarjetasPeliculas();
    }

    private void cargarTarjetasPeliculas() {
        if (cine != null) {
            flowPanePeliculas.getChildren().clear();
            for (Pelicula p : cine.consultarPeliculasDisponibles()) {
                // Tarjeta estilo póster
                VBox tarjeta = new VBox(8);
                tarjeta.setPrefWidth(210);
                tarjeta.setPrefHeight(360);
                tarjeta.setAlignment(Pos.TOP_LEFT);
                tarjeta.setPadding(new Insets(12));
                tarjeta.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 2);");

                // 1. Configurar el ImageView para la imagen de la película
                ImageView imageView = new ImageView();
                imageView.setFitWidth(186);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(false); // Cambiado correctamente a setPreserveRatio

                // Cargamos la imagen utilizando la ruta que viene del objeto Pelicula
                if (p.getImagenRuta() != null && !p.getImagenRuta().isEmpty()) {
                    try {
                        InputStream imageStream = getClass().getResourceAsStream(p.getImagenRuta());
                        if (imageStream != null) {
                            imageView.setImage(new Image(imageStream));
                        } else {
                            System.out.println("No se encontró el archivo de imagen en la ruta: " + p.getImagenRuta());
                        }
                    } catch (Exception e) {
                        System.out.println("Error al cargar la imagen para: " + p.getNombre());
                    }
                }

                // 2. Cabecera con la clasificación
                Label lblClasificacion = new Label(p.getClasificacion().toString());
                lblClasificacion.setFont(Font.font("System", FontWeight.BOLD, 10));
                lblClasificacion.setTextFill(Color.WHITE); // Letra en blanco
                lblClasificacion.setStyle("-fx-background-color: #C8102E; -fx-background-radius: 4; -fx-padding: 3 8 3 8;"); // Fondo rojo

                // 3. Nombre de la película
                Label lblNombre = new Label(p.getNombre());
                lblNombre.setFont(Font.font("System", FontWeight.BOLD, 14));
                lblNombre.setTextFill(Color.web("#C8102E"));
                lblNombre.setWrapText(true);

                // 4. Género
                Label lblGenero = new Label("Género: " + p.getGenero());
                lblGenero.setFont(Font.font("System", 11));
                lblGenero.setTextFill(Color.web("#555555"));
                lblGenero.setWrapText(true);

                // 5. Duración
                Label lblDuracion = new Label("Duración: " + p.getDuracion() + " min");
                lblDuracion.setFont(Font.font("System", 11));
                lblDuracion.setTextFill(Color.web("#777777"));

                // Añadimos todos los componentes a la tarjeta visual
                tarjeta.getChildren().addAll(imageView, lblClasificacion, lblNombre, lblGenero, lblDuracion);
                flowPanePeliculas.getChildren().add(tarjeta);
            }
        }
    }
}