package uniquindio.edu.co.proyectofinal.controller;

import uniquindio.edu.co.proyectofinal.model.Cine;
import uniquindio.edu.co.proyectofinal.model.Clasificacion;
import uniquindio.edu.co.proyectofinal.model.Pelicula;
import java.util.ArrayList;
import java.util.List;

public class GestionPeliculasController {
    private Cine cine;

    public GestionPeliculasController(Cine cine) {
        this.cine = cine;
    }

    public List<Pelicula> obtenerPeliculas() {
        return (cine != null) ? cine.getListaPeliculas() : new ArrayList<>();
    }

    public boolean registrarPelicula(int codigo, String nombre, String genero, int duracion, Clasificacion clasificacion, String estado) {
        String estadoFinal = (estado == null || estado.isEmpty()) ? "ACTIVO" : estado;
        Pelicula nueva = new Pelicula(codigo, nombre, genero, duracion, estadoFinal,
                new ArrayList<>(), clasificacion, "/uniquindio/edu/co/proyectofinal/images/default.jpg");
        return cine.registrarPelicula(nueva);
    }

    public boolean modificarPelicula(Pelicula original, String nombre, String genero, int duracion, Clasificacion clasificacion, String estado) {
        Pelicula actualizada = new Pelicula(original.getCodigo(), nombre, genero, duracion, estado,
                original.getListaFunciones(), clasificacion, original.getImagenRuta());
        return cine.modificarPelicula(original.getCodigo(), actualizada);
    }

    public boolean deshabilitarPelicula(int codigo) {
        return cine.deshabilitarPelicula(codigo);
    }
}