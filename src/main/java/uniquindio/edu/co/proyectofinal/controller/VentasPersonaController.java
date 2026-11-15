package uniquindio.edu.co.proyectofinal.controller;

import uniquindio.edu.co.proyectofinal.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VentasPersonaController {
    private Cine cine;

    public VentasPersonaController(Cine cine) {
        this.cine = cine;
    }

    public List<Persona> obtenerTodasLasPersonas() {
        return (cine != null) ? cine.getListaPersonas() : new ArrayList<>();
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