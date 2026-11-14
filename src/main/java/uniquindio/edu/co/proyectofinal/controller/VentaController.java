package uniquindio.edu.co.proyectofinal.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.util.StringConverter;
import uniquindio.edu.co.proyectofinal.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class VentaController {

    @FXML private ComboBox<Persona> cmbVendedor;
    @FXML private TextField txtDocumentoCliente;
    @FXML private TextField txtNombreCliente;
    @FXML private ComboBox<Funcion> cmbFuncion;
    @FXML private ComboBox<Asiento> cmbAsiento;
    @FXML private ComboBox<TipoEntrada> cmbTipoEntrada;
    @FXML private Label lblTotalPagar;
    @FXML private Label lblMensaje;

    private Cine cine;

    public void setCine(Cine cine) {
        this.cine = cine;
        cargarDatosInicialesSiVacio();
        cargarVendedoresEnCombo();
        cargarFuncionesEnCombo();
    }

    @FXML
    public void initialize() {
        // Cargar las opciones del enum TipoEntrada
        cmbTipoEntrada.getItems().setAll(TipoEntrada.values());
        cmbTipoEntrada.setValue(TipoEntrada.GENERAL);

        // Convertidor para mostrar nombres en el ComboBox de Vendedores
        cmbVendedor.setConverter(new StringConverter<Persona>() {
            @Override
            public String toString(Persona p) {
                if (p == null) return "";
                String tipo = (p instanceof ClienteVendedor) ? "Cliente-Vendedor" : "Vendedor";
                return p.getNombre() + " (" + tipo + " - Doc: " + p.getDocumento() + ")";
            }
            @Override
            public Persona fromString(String s) { return null; }
        });

        // Solución robusta para el ComboBox de Asientos
        Callback<ListView<Asiento>, ListCell<Asiento>> asientoFactory = param -> new ListCell<Asiento>() {
            @Override
            protected void updateItem(Asiento asiento, boolean empty) {
                super.updateItem(asiento, empty);
                if (empty || asiento == null) {
                    setText(null);
                } else {
                    setText("Puesto " + asiento.obtenerPuesto());
                }
            }
        };

        cmbAsiento.setCellFactory(asientoFactory);
        cmbAsiento.setButtonCell(asientoFactory.call(null));

        cmbAsiento.setConverter(new StringConverter<Asiento>() {
            @Override
            public String toString(Asiento a) {
                return (a == null) ? "" : "Puesto " + a.obtenerPuesto();
            }
            @Override
            public Asiento fromString(String s) { return null; }
        });

        // Formato para mostrar cada Función en el ComboBox
        cmbFuncion.setConverter(new StringConverter<Funcion>() {
            @Override
            public String toString(Funcion f) {
                if (f == null) return "";
                return f.getPelicula().getNombre() + " | Sala " + f.getSala().getNumero()
                        + " (" + f.getSala().getColeccionAsientos() + ") - " + f.getHora();
            }
            @Override
            public Funcion fromString(String s) { return null; }
        });
    }

    private void cargarDatosInicialesSiVacio() {
        if (cine != null && cine.getListaPersonas().isEmpty()) {
            // Datos de prueba iniciales si no hay usuarios creados
            Vendedor vendedorPrueba = new Vendedor(1001, "Taquillero UQ", Cargo.OPERADOR.getSalario(), Cargo.OPERADOR);
            cine.registrarPersona(vendedorPrueba);
        }

        if (cine != null && cine.getListaFunciones().isEmpty()) {
            Sala sala1 = new Sala(1, 1, 20, EstadoSala.DISPONIBLE, ColeccionAsientos.ESTANDAR);
            Sala salaVip = new Sala(2, 2, 15, EstadoSala.DISPONIBLE, ColeccionAsientos.VIP);
            cine.registrarSala(sala1);
            cine.registrarSala(salaVip);

            Pelicula peli1 = new Pelicula(101, "Rocky", "Drama / Acción", 130, "ACTIVO", new ArrayList<>(), Clasificacion.MAYORES_12, "/uniquindio/edu/co/proyectofinal/images/rocky.jpg");
            Pelicula peli2 = new Pelicula(102, "Encanto", "Animación", 102, "ACTIVO", new ArrayList<>(), Clasificacion.TODOS, "/uniquindio/edu/co/proyectofinal/images/encanto.gif");

            cine.registrarPelicula(peli1);
            cine.registrarPelicula(peli2);

            Funcion f1 = new Funcion(1, peli1, sala1, LocalTime.of(18, 30), LocalDate.now(), 15000, EstadoFuncion.PROGRAMADA);
            Funcion f2 = new Funcion(2, peli2, salaVip, LocalTime.of(20, 0), LocalDate.now(), 15000, EstadoFuncion.PROGRAMADA);
            cine.programarFuncion(peli1, f1);
            cine.programarFuncion(peli2, f2);
        }
    }

    private void cargarVendedoresEnCombo() {
        cmbVendedor.getItems().clear();
        if (cine != null && cine.getListaPersonas() != null) {
            for (Persona p : cine.getListaPersonas()) {
                // Filtrar personas que puedan actuar como vendedores (Vendedor o ClienteVendedor)
                if (p instanceof Vendedor || p instanceof ClienteVendedor) {
                    cmbVendedor.getItems().add(p);
                }
            }
        }
    }

    private void cargarFuncionesEnCombo() {
        cmbFuncion.getItems().clear();
        if (cine != null && cine.getListaFunciones() != null) {
            for (Funcion f : cine.getListaFunciones()) {
                if (f.getEstadoFuncion() == EstadoFuncion.PROGRAMADA) {
                    cmbFuncion.getItems().add(f);
                }
            }
        }
    }

    @FXML
    void alSeleccionarFuncion(ActionEvent event) {
        Funcion funcion = cmbFuncion.getValue();
        cmbAsiento.getItems().clear();

        if (funcion != null && funcion.getSala() != null) {
            List<Asiento> asientosDisponibles = new ArrayList<>();
            for (Asiento a : funcion.getSala().getListaAsientos()) {
                if (a.getEstado() == EstadoAsiento.DISPONIBLE) {
                    asientosDisponibles.add(a);
                }
            }
            cmbAsiento.getItems().setAll(asientosDisponibles);
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
            Persona vendedorSeleccionado = cmbVendedor.getValue();
            String docTexto = txtDocumentoCliente.getText().trim();
            String nombreCliente = txtNombreCliente.getText().trim();
            Funcion funcion = cmbFuncion.getValue();
            Asiento asiento = cmbAsiento.getValue();
            TipoEntrada tipoEntrada = cmbTipoEntrada.getValue();

            if (vendedorSeleccionado == null || docTexto.isEmpty() || nombreCliente.isEmpty() || funcion == null || asiento == null || tipoEntrada == null) {
                lblMensaje.setStyle("-fx-text-fill: #C8102E;");
                lblMensaje.setText("Por favor complete el vendedor, los datos del cliente, función, asiento y tipo de entrada.");
                return;
            }

            int documento = Integer.parseInt(docTexto);

            // 1. Verificar si el cliente ya existe en el sistema
            Persona personaExistente = cine.consultarPersona(documento);
            Cliente clienteFinal = null;

            if (personaExistente != null) {
                if (personaExistente instanceof Cliente) {
                    clienteFinal = (Cliente) personaExistente;
                } else {
                    lblMensaje.setStyle("-fx-text-fill: #C8102E;");
                    lblMensaje.setText("El documento pertenece a un empleado registrado que no es cliente.");
                    return;
                }
            } else {
                // Si no existe, se crea y se registra en el cine validando duplicados
                clienteFinal = new Cliente(documento, nombreCliente);
                boolean registrado = cine.registrarPersona(clienteFinal);
                if (!registrado) {
                    lblMensaje.setStyle("-fx-text-fill: #C8102E;");
                    lblMensaje.setText("Error: Ya existe una persona registrada con ese número de documento.");
                    return;
                }
            }

            // 2. Ocupar asiento y crear Entrada
            asiento.setEstado(EstadoAsiento.OCUPADO);
            int codigoEntrada = funcion.getListaEntradas().size() + 1;
            Entrada entrada = new Entrada(codigoEntrada, funcion, asiento, tipoEntrada);
            funcion.getListaEntradas().add(entrada);

            // 3. Crear DetalleVenta y calcular subtotal
            DetalleVenta detalle = new DetalleVenta(1);
            detalle.agregarEntrada(entrada);
            double valorTotal = detalle.calcularSubtotal();

            // 4. Determinar el vendedor para la venta (Vendedor o ClienteVendedor)
            Vendedor vendedorObj = null;
            if (vendedorSeleccionado instanceof Vendedor && !(vendedorSeleccionado instanceof ClienteVendedor)) {
                vendedorObj = (Vendedor) vendedorSeleccionado;
            }

            // 5. Crear Venta y registrarla en el Cine
            int codigoVenta = cine.getListaVentas().size() + 1;
            Venta venta = new Venta(codigoVenta, clienteFinal, LocalDateTime.now(), valorTotal, EstadoVenta.ACTIVA, vendedorObj);
            venta.getListaDetalles().add(detalle);
            cine.getListaVentas().add(venta);

            // 6. Conectar de forma cruzada con las clases heredadas:
            // Registrar compra en el cliente o cliente-vendedor
            if (clienteFinal instanceof ClienteVendedor) {
                ((ClienteVendedor) clienteFinal).getListaCompras().add(venta);
            } else {
                clienteFinal.getListaCompras().add(venta);
            }

            // Registrar venta en el vendedor o cliente-vendedor
            if (vendedorSeleccionado instanceof ClienteVendedor) {
                ((ClienteVendedor) vendedorSeleccionado).getListaVentas().add(venta);
            } else if (vendedorSeleccionado instanceof Vendedor) {
                ((Vendedor) vendedorSeleccionado).getListaVentas().add(venta);
            }

            lblMensaje.setStyle("-fx-text-fill: #1E7E34;");
            lblMensaje.setText("¡Venta #" + codigoVenta + " exitosa! Puesto " + asiento.obtenerPuesto()
                    + " asignado a " + clienteFinal.getNombre() + " por $" + String.format("%,.0f", valorTotal));

            // Refrescar asientos disponibles
            alSeleccionarFuncion(null);
            txtDocumentoCliente.clear();
            txtNombreCliente.clear();

        } catch (NumberFormatException e) {
            lblMensaje.setStyle("-fx-text-fill: #C8102E;");
            lblMensaje.setText("El documento del cliente debe ser numérico.");
        }
    }
}