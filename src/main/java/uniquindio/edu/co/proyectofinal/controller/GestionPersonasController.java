package uniquindio.edu.co.proyectofinal.controller;

import uniquindio.edu.co.proyectofinal.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestionPersonasController {
    private Cine cine;

    public GestionPersonasController(Cine cine) {
        this.cine = cine;
    }

    public List<Persona> obtenerTodasLasPersonas() {
        return (cine != null) ? cine.getListaPersonas() : new ArrayList<>();
    }

    public List<Cliente> obtenerClientes() {
        return obtenerTodasLasPersonas().stream()
                .filter(p -> p instanceof Cliente && !(p instanceof ClienteVendedor))
                .map(p -> (Cliente) p)
                .toList();
    }

    public List<Vendedor> obtenerVendedores() {
        return obtenerTodasLasPersonas().stream()
                .filter(p -> p instanceof Vendedor && !(p instanceof ClienteVendedor))
                .map(p -> (Vendedor) p)
                .toList();
    }

    public List<ClienteVendedor> obtenerClienteVendedores() {
        return obtenerTodasLasPersonas().stream()
                .filter(p -> p instanceof ClienteVendedor)
                .map(p -> (ClienteVendedor) p)
                .toList();
    }

    public boolean guardarCliente(int documento, String nombre, boolean esNuevo) {
        Cliente cliente = new Cliente(documento, nombre);
        return esNuevo ? cine.registrarPersona(cliente) : cine.modificarPersona(documento, cliente);
    }

    public boolean guardarVendedor(int documento, String nombre, Cargo cargo, boolean esNuevo) {
        Vendedor vendedor = new Vendedor(documento, nombre, cargo.getSalario(), cargo);
        return esNuevo ? cine.registrarPersona(vendedor) : cine.modificarPersona(documento, vendedor);
    }

    public boolean guardarClienteVendedor(int documento, String nombre, Cargo cargo, boolean esNuevo) {
        ClienteVendedor cv = new ClienteVendedor(documento, nombre, cargo);
        return esNuevo ? cine.registrarPersona(cv) : cine.modificarPersona(documento, cv);
    }

    public boolean eliminarPersona(int documento) {
        return cine.eliminarPersona(documento);
    }

    public List<Venta> obtenerVentasDePersona(Persona persona) {
        List<Venta> ventas = new ArrayList<>();
        if (persona instanceof ClienteVendedor cv) {
            ventas = cv.getListaCompras();
        } else if (persona instanceof Cliente c) {
            ventas = c.getListaCompras();
        } else if (persona instanceof Vendedor v) {
            ventas = v.getListaVentas();
        }
        // Retornamos solo las ventas ACTIVAS para el historial principal
        return ventas.stream()
                .filter(v -> v.getEstadoVenta() == EstadoVenta.ACTIVA)
                .collect(Collectors.toList());
    }

    public List<Venta> obtenerVentasCanceladasDePersona(Persona persona) {
        List<Venta> ventas = new ArrayList<>();
        if (persona instanceof ClienteVendedor cv) {
            ventas = cv.getListaCompras();
        } else if (persona instanceof Cliente c) {
            ventas = c.getListaCompras();
        } else if (persona instanceof Vendedor v) {
            ventas = v.getListaVentas();
        }
        // Retornamos el apartado de ventas CANCELADAS
        return ventas.stream()
                .filter(v -> v.getEstadoVenta() == EstadoVenta.CANCELADA)
                .collect(Collectors.toList());
    }

    public void cancelarVentaDePersona(Venta venta) {
        if (venta != null) {
            // 1. Cambiar el estado de la venta a CANCELADA
            venta.setEstadoVenta(EstadoVenta.CANCELADA);

            // 2. Liberar los asientos asociados a esta venta
            if (venta.getListaDetalles() != null) {
                for (DetalleVenta detalle : venta.getListaDetalles()) {
                    if (detalle.getListaEntradas() != null) {
                        for (Entrada entrada : detalle.getListaEntradas()) {
                            if (entrada.getAsiento() != null) {
                                entrada.getAsiento().setEstado(EstadoAsiento.DISPONIBLE);
                            }
                        }
                    }
                }
            }
        }
    }
}