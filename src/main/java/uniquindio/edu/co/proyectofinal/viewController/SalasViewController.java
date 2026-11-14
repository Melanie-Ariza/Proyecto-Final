package uniquindio.edu.co.proyectofinal.viewController;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import uniquindio.edu.co.proyectofinal.controller.SalasController;
import uniquindio.edu.co.proyectofinal.model.Asiento;
import uniquindio.edu.co.proyectofinal.model.Cine;
import uniquindio.edu.co.proyectofinal.model.EstadoAsiento;
import uniquindio.edu.co.proyectofinal.model.Sala;

public class SalasViewController {

    @FXML private ListView<Sala> listSalas;
    @FXML private GridPane gridAsientos;
    @FXML private Label lblTituloSala;

    private SalasController salasController;

    public void setCine(Cine cine) {
        this.salasController = new SalasController(cine);
        configurarListaSalas();
    }

    private void configurarListaSalas() {
        if (salasController != null) {
            listSalas.getItems().setAll(salasController.obtenerSalas());

            listSalas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, salaSeleccionada) -> {
                if (salaSeleccionada != null) {
                    mostrarMapaAsientos(salaSeleccionada);
                }
            });

            if (!listSalas.getItems().isEmpty()) {
                listSalas.getSelectionModel().select(0);
            }
        }
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
}
