package uniquindio.edu.co.proyectofinal.controller;

import uniquindio.edu.co.proyectofinal.model.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class VentaController {
    private Cine cine;

    public VentaController(Cine cine) {
        this.cine = cine;
        inicializarDatosPruebaSiVacio();
    }

    private void inicializarDatosPruebaSiVacio() {
        if (cine != null && cine.getListaPersonas().isEmpty()) {
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

    public List<Persona> obtenerVendedoresDisponibles() {
        List<Persona> vendedores = new ArrayList<>();
        if (cine != null && cine.getListaPersonas() != null) {
            for (Persona p : cine.getListaPersonas()) {
                if (p instanceof Vendedor || p instanceof ClienteVendedor) {
                    vendedores.add(p);
                }
            }
        }
        return vendedores;
    }

    public List<Funcion> obtenerFuncionesProgramadas() {
        List<Funcion> programadas = new ArrayList<>();
        if (cine != null && cine.getListaFunciones() != null) {
            for (Funcion f : cine.getListaFunciones()) {
                if (f.getEstadoFuncion() == EstadoFuncion.PROGRAMADA) {
                    programadas.add(f);
                }
            }
        }
        return programadas;
    }

    public List<Asiento> obtenerAsientosDisponibles(Funcion funcion) {
        List<Asiento> disponibles = new ArrayList<>();
        if (funcion != null && funcion.getSala() != null) {
            for (Asiento a : funcion.getSala().getListaAsientos()) {
                if (a.getEstado() == EstadoAsiento.DISPONIBLE) {
                    disponibles.add(a);
                }
            }
        }
        return disponibles;
    }

    public double calcularPrecioEstimado(Funcion funcion, TipoEntrada tipo) {
        if (funcion != null && tipo != null) {
            return tipo.getValorEntrada() + funcion.getSala().getColeccionAsientos().getValorAsiento();
        }
        return 0.0;
    }

    public Venta registrarVenta(Persona vendedorSeleccionado, int documentoCliente, String nombreCliente,
                                Funcion funcion, Asiento asiento, TipoEntrada tipoEntrada) {
        Persona personaExistente = cine.consultarPersona(documentoCliente);
        Cliente clienteFinal;

        if (personaExistente != null) {
            if (personaExistente instanceof Cliente c) {
                clienteFinal = c;
            } else {
                throw new IllegalArgumentException("El documento pertenece a un empleado registrado que no es cliente.");
            }
        } else {
            clienteFinal = new Cliente(documentoCliente, nombreCliente);
            if (!cine.registrarPersona(clienteFinal)) {
                throw new IllegalArgumentException("Ya existe una persona registrada con ese número de documento.");
            }
        }

        asiento.setEstado(EstadoAsiento.OCUPADO);
        int codigoEntrada = funcion.getListaEntradas().size() + 1;
        Entrada entrada = new Entrada(codigoEntrada, funcion, asiento, tipoEntrada);
        funcion.getListaEntradas().add(entrada);

        DetalleVenta detalle = new DetalleVenta(1);
        detalle.agregarEntrada(entrada);
        double valorTotal = detalle.calcularSubtotal();

        Vendedor vendedorObj = (vendedorSeleccionado instanceof Vendedor && !(vendedorSeleccionado instanceof ClienteVendedor))
                ? (Vendedor) vendedorSeleccionado : null;

        int codigoVenta = cine.getListaVentas().size() + 1;
        Venta venta = new Venta(codigoVenta, clienteFinal, LocalDateTime.now(), valorTotal, EstadoVenta.ACTIVA, vendedorObj);
        venta.getListaDetalles().add(detalle);
        cine.registrarVenta(venta);

        if (clienteFinal instanceof ClienteVendedor cv) {
            cv.getListaCompras().add(venta);
        } else {
            clienteFinal.getListaCompras().add(venta);
        }

        if (vendedorSeleccionado instanceof ClienteVendedor cv) {
            cv.getListaVentas().add(venta);
        } else if (vendedorSeleccionado instanceof Vendedor v) {
            v.getListaVentas().add(venta);
        }

        return venta;
    }
}