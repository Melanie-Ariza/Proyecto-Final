package uniquindio.edu.co.proyectofinal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;
import uniquindio.edu.co.proyectofinal.Excepciones.FuncionInexistenteException.FuncionInexistenteException;
import uniquindio.edu.co.proyectofinal.Excepciones.FuncionSinCupoException.FuncionSinCupoException;
import uniquindio.edu.co.proyectofinal.Excepciones.VentaInexistenteException.VentaInexistenteException;
import uniquindio.edu.co.proyectofinal.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
        cmbTipoEntrada.getItems().setAll(TipoEntrada.values());
        cmbTipoEntrada.setValue(TipoEntrada.GENERAL);

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
            public Asiento fromString(String s) {
                return null;
            }
        });

        cmbFuncion.setConverter(new StringConverter<Funcion>() {
            @Override
            public String toString(Funcion f) {
                if (f == null) return "";
                return f.getPelicula().getNombre() + " | Sala " + f.getSala().getNumero()
                        + " (" + f.getSala().getColeccionAsientos() + ") - " + f.getHora();
            }
            @Override
            public Funcion fromString(String s) {
                return null;
            }
        });
    }

    private void cargarDatosInicialesSiVacio() {
        this.vendedorActivo = new Vendedor(1001, "Taquillero UQ", Cargo.OPERADOR.getSalario(), Cargo.OPERADOR);

        if (cine.getListaFunciones().isEmpty()) {
            Sala sala1 = new Sala(1, 1, 20, EstadoSala.DISPONIBLE, ColeccionAsientos.ESTANDAR);
            Sala salaVip = new Sala(2, 2, 15, EstadoSala.DISPONIBLE, ColeccionAsientos.VIP);
            cine.registrarSala(sala1);
            cine.registrarSala(salaVip);

            Pelicula peli1 = new Pelicula(101, "Rocky", "Drama / Acción", 130, "ACTIVO", new ArrayList<>(), Clasificacion.MAYORES_12, "/uniquindio/edu/co/proyectofinal/images/rocky.jpg");
            Pelicula peli2 = new Pelicula(102, "Encanto", "Animación", 102, "ACTIVO", new ArrayList<>(), Clasificacion.TODOS,"/uniquindio/edu/co/proyectofinal/images/encanto.gif");

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

        if (funcion != null) {
            Sala sala = funcion.getSala();
            if (sala != null) {
                List<Asiento> asientosDisponibles = new ArrayList<>();
                for (Asiento a : sala.getListaAsientos()) {
                    if (a.getEstado() == EstadoAsiento.DISPONIBLE) {
                        asientosDisponibles.add(a);
                    }
                }
                cmbAsiento.getItems().setAll(asientosDisponibles);
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
    // Referencia para comunicarse con el controlador principal y cambiar de pestaña automáticamente
    private Runnable onVentaExitosaCallback;

    public void setOnVentaExitosaCallback(Runnable callback) {
        this.onVentaExitosaCallback = callback;
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
                mostrarAlerta("Campos Incompletos", "Por favor completa todos los datos para registrar la venta.", Alert.AlertType.WARNING);
                return;
            }

            int documento = Integer.parseInt(docTexto);

            // Buscar si el cliente ya existe en el cine para mantener su historial
            Cliente cliente = null;
            for (Persona p : cine.getListaPersonas()) {
                if (p.getDocumento() == documento && p instanceof Cliente) {
                    cliente = (Cliente) p;
                    break;
                }
            }

            // Si no existe, crearlo y registrarlo en el cine
            if (cliente == null) {
                cliente = new Cliente(documento, nombreCliente);
                cine.registrarPersona(cliente);
            }

            // Validar cupos mediante excepciones del modelo
            if (funcion.getSala() == null || funcion.getSala().contarAsientosDisponibles() <= 0) {
                throw new uniquindio.edu.co.proyectofinal.Excepciones.FuncionSinCupoException.FuncionSinCupoException("La función seleccionada no tiene cupos disponibles.");
            }

            // Validar asiento ocupado
            if (asiento.getEstado() == EstadoAsiento.OCUPADO) {
                mostrarAlerta("Asiento Ocupado", "El asiento seleccionado ya se encuentra ocupado.", Alert.AlertType.WARNING);
                return;
            }

            DetalleVenta detalle = new DetalleVenta(1);
            int codigoEntrada = (funcion.getListaEntradas() == null || funcion.getListaEntradas().isEmpty())
                    ? 1
                    : funcion.getListaEntradas().get(funcion.getListaEntradas().size() - 1).getCodigo() + 1;

            Entrada entrada = new Entrada(codigoEntrada, funcion, asiento, tipoEntrada);
            detalle.agregarEntrada(entrada);

            int codigoVenta = (cine.getListaVentas() == null || cine.getListaVentas().isEmpty())
                    ? 1
                    : cine.getListaVentas().get(cine.getListaVentas().size() - 1).getCodigo() + 1;

            Venta venta = new Venta(codigoVenta, cliente, java.time.LocalDateTime.now(), 0.0, EstadoVenta.ACTIVA, null);
            venta.getListaDetalles().add(detalle);

            // Calcular valor total usando la lógica del cine
            double valorTotal = cine.calcularValorTotal(codigoVenta, cliente);
            if (valorTotal == 0.0) {
                valorTotal = detalle.calcularSubtotal();
            }
            venta.setValorPagado(valorTotal);

            // Registrar venta a través del modelo Cine
            cine.registrarVenta(venta);

            // VINCULAR LA VENTA AL HISTORIAL DEL CLIENTE
            cliente.getListaCompras().add(venta);

            // Ocupar asiento físicamente
            asiento.setEstado(EstadoAsiento.OCUPADO);
            if (funcion.getListaEntradas() == null) {
                funcion.setListaEntradas(new java.util.ArrayList<>());
            }
            funcion.getListaEntradas().add(entrada);

            mostrarAlerta("Éxito", "¡Venta registrada con éxito y vinculada al cliente!", Alert.AlertType.INFORMATION);

            // Limpieza interna sin errores
            limpiarCampos();

        } catch (uniquindio.edu.co.proyectofinal.Excepciones.FuncionSinCupoException.FuncionSinCupoException |
                 uniquindio.edu.co.proyectofinal.Excepciones.VentaInexistenteException.VentaInexistenteException e) {
            mostrarAlerta("Error de Negocio", e.getMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            mostrarAlerta("Formato Inválido", "El documento debe ser un número entero válido.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al registrar la venta: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    // Método para el botón del FXML
    @FXML
    void limpiarCampos(ActionEvent event) {
        limpiarCampos();
    }

    // Método auxiliar interno que vacía los campos sin errores
    public void limpiarCampos() {
        if (txtDocumentoCliente != null) txtDocumentoCliente.clear();
        if (txtNombreCliente != null) txtNombreCliente.clear();
        if (cmbFuncion != null) cmbFuncion.setValue(null);
        if (cmbAsiento != null) cmbAsiento.setValue(null);
        if (cmbTipoEntrada != null) cmbTipoEntrada.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}