package uniquindio.edu.co.proyectofinal.controller;

import uniquindio.edu.co.proyectofinal.model.*;
import java.util.ArrayList;
import java.util.List;

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
        if (persona instanceof ClienteVendedor cv) {
            return cv.getListaCompras();
        } else if (persona instanceof Cliente c) {
            return c.getListaCompras();
        } else if (persona instanceof Vendedor v) {
            return v.getListaVentas();
        }
        return new ArrayList<>();
    }

    public void eliminarVentaDePersona(Persona persona, Venta venta) {
        if (persona instanceof ClienteVendedor cv) {
            cv.getListaCompras().remove(venta);
        } else if (persona instanceof Cliente c) {
            c.getListaCompras().remove(venta);
        } else if (persona instanceof Vendedor v) {
            v.getListaVentas().remove(venta);
        }
    }
}