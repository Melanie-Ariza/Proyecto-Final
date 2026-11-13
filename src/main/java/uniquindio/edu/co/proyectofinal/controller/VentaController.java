package uniquindio.edu.co.proyectofinal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import uniquindio.edu.co.proyectofinal.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class VentaController {

    @FXML private TextField txtDocumentoCliente;
    @FXML private TextField txtNombreCliente;
    @FXML private ComboBox<Funcion> cmbFuncion;
    @FXML private ComboBox<Asiento> cmbAsiento;
    @FXML private ComboBox<TipoEntrada> cmbTipoEntrada;
    @FXML private Label lblTotalPagar;
    @FXML private Label lblMensaje;

    private Cine cine;
    private Vendedor vendedorActivo;

    public void setCine(Cine cine) {
        this.cine = cine;
        cargarDatosInicialesSiVacio();
        cargarFuncionesEnCombo();
    }

    @FXML
    public void initialize() {
        // Cargar las opciones del enum TipoEntrada
        cmbTipoEntrada.getItems().setAll(TipoEntrada.values());
        cmbTipoEntrada.setValue(TipoEntrada.GENERAL);

        // Formato para mostrar cada Funcion en el ComboBox
        cmbFuncion.setConverter(new StringConverter<>() {
            @Override
            public String toString(Funcion f) {
                if (f == null) return "";
                return f.getPelicula().getNombre() + " | Sala " + f.getSala().getNumero()
                        + " (" + f.getSala().getColeccionAsientos() + ") - " + f.getHora();
            }
            @Override
            public Funcion fromString(String s) { return null; }
        });

        // Formato para mostrar cada Asiento A1, A2
        cmbAsiento.setConverter(new StringConverter<>() {
            @Override
            public String toString(Asiento a) {
                return (a == null) ? "" : "Puesto " + a.obtenerPuesto();
            }
            @Override
            public Asiento fromString(String s) { return null; }
        });
    }

    private void cargarDatosInicialesSiVacio() {
        this.vendedorActivo = new Vendedor(1001, "Taquillero UQ", Cargo.OPERADOR.getSalario(), Cargo.OPERADOR);

        //prueba para poder vender ya
        if (cine.getListaFunciones().isEmpty()) {
            Sala sala1 = new Sala(1, 1, 20, EstadoSala.DISPONIBLE, ColeccionAsientos.ESTANDAR);
            Sala salaVip = new Sala(2, 2, 15, EstadoSala.DISPONIBLE, ColeccionAsientos.VIP);
            cine.registrarSala(sala1);
            cine.registrarSala(salaVip);

            Pelicula peli1 = new Pelicula(101, "Rocky", "Drama / Acción", 130, "ACTIVO", new ArrayList<>(), Clasificacion.MAYORES_12);
            Pelicula peli2 = new Pelicula(102, "Encanto", "Animación", 102, "ACTIVO", new ArrayList<>(), Clasificacion.TODOS);
            cine.registrarPelicula(peli1);
            cine.registrarPelicula(peli2);

            Funcion f1 = new Funcion(1, peli1, sala1, LocalTime.of(18, 30), LocalDate.now(), 15000, EstadoFuncion.PROGRAMADA);
            Funcion f2 = new Funcion(2, peli2, salaVip, LocalTime.of(20, 0), LocalDate.now(), 15000, EstadoFuncion.PROGRAMADA);
            cine.programarFuncion(peli1, f1);
            cine.programarFuncion(peli2, f2);
        }
    }

    private void cargarFuncionesEnCombo() {
        cmbFuncion.getItems().clear();
        for (Funcion f : cine.getListaFunciones()) {
            if (f.getEstadoFuncion() == EstadoFuncion.PROGRAMADA) {
                cmbFuncion.getItems().add(f);
            }
        }
    }

    @FXML
    void alSeleccionarFuncion(ActionEvent event) {
        Funcion funcion = cmbFuncion.getValue();
        cmbAsiento.getItems().clear();
        if (funcion != null && funcion.getSala() != null) {
            for (Asiento a : funcion.getSala().getListaAsientos()) {
                if (a.getEstado() == EstadoAsiento.DISPONIBLE) {
                    cmbAsiento.getItems().add(a);
                }
            }
        }
        actualizarPrecioEstimado(null);
    }

    @FXML
    void actualizarPrecioEstimado(ActionEvent event) {
        Funcion funcion = cmbFuncion.getValue();
        TipoEntrada tipo = cmbTipoEntrada.getValue();
        if (funcion != null && tipo != null) {
            double total = tipo.getValorEntrada() + funcion.getSala().getColeccionAsientos().getValorAsiento();
            lblTotalPagar.setText("$ " + String.format("%,.0f", total));
        } else {
            lblTotalPagar.setText("$ 0");
        }
    }

    @FXML
    void registrarVenta(ActionEvent event) {
        try {
            String docTexto = txtDocumentoCliente.getText().trim();
            String nombreCliente = txtNombreCliente.getText().trim();
            Funcion funcion = cmbFuncion.getValue();
            Asiento asiento = cmbAsiento.getValue();
            TipoEntrada tipoEntrada = cmbTipoEntrada.getValue();

            if (docTexto.isEmpty() || nombreCliente.isEmpty() || funcion == null || asiento == null || tipoEntrada == null) {
                lblMensaje.setStyle("-fx-text-fill: #C8102E;");
                lblMensaje.setText("Completa los datos del cliente, la función, el asiento y el tipo de entrada.");
                return;
            }

            int documento = Integer.parseInt(docTexto);
            Cliente cliente = new Cliente(documento, nombreCliente);

            // 1. Ocupar asiento y crear Entrada
            asiento.setEstado(EstadoAsiento.OCUPADO);
            int codigoEntrada = funcion.getListaEntradas().size() + 1;
            Entrada entrada = new Entrada(codigoEntrada, funcion, asiento, tipoEntrada);
            funcion.getListaEntradas().add(entrada);

            // 2. Crear DetalleVenta y calcular subtotal real desde el modelo
            DetalleVenta detalle = new DetalleVenta(1);
            detalle.agregarEntrada(entrada);
            double valorTotal = detalle.calcularSubtotal();

            // 3. Crear Venta y agregarla al Cine
            int codigoVenta = cine.getListaVentas().size() + 1;
            Venta venta = new Venta(codigoVenta, cliente, LocalDateTime.now(), valorTotal, EstadoVenta.ACTIVA, vendedorActivo);
            venta.getListaDetalles().add(detalle);
            cine.getListaVentas().add(venta);

            lblMensaje.setStyle("-fx-text-fill: #1E7E34;");
            lblMensaje.setText("¡Venta #" + codigoVenta + " exitosa! Puesto " + asiento.obtenerPuesto()
                    + " asignado a " + nombreCliente + " por $" + String.format("%,.0f", valorTotal));

            // Refrescar asientos disponibles para que ese puesto ya no salga libre
            alSeleccionarFuncion(null);

        } catch (NumberFormatException e) {
            lblMensaje.setStyle("-fx-text-fill: #C8102E;");
            lblMensaje.setText("El documento del cliente debe ser numérico.");
        }
    }
}