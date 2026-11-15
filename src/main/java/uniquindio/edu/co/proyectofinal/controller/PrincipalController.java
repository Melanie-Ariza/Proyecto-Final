package uniquindio.edu.co.proyectofinal.controller;

import uniquindio.edu.co.proyectofinal.model.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PrincipalController {
    private Cine cine;

    public PrincipalController() {
        this.cine = new Cine(1, "CINEMAUQ");
    }

    public Cine getCine() {
        return cine;
    }

    public List<Pelicula> obtenerPeliculasDisponibles() {
        return cine.consultarPeliculasDisponibles();
    }

    public List<Sala> obtenerSalas() {
        return cine.getListaSalas();
    }

    public int obtenerTotalEntradasVendidas() {
        return cine.consultarTotalEntradasVendidas();
    }

    public double obtenerTotalIngresosGenerados() {
        return cine.consultarTotalIngresosGenerados();
    }

    public Pelicula obtenerPeliculaMasVendida() {
        return cine.identificarPeliculaMasVendida();
    }
}