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

    /**
     * Calcula de forma automática el siguiente código entero disponible para una nueva película.
     */
    public int obtenerSiguienteCodigo() {
        int max = 0;
        List<Pelicula> peliculas = obtenerPeliculas();
        if (peliculas != null) {
            for (Pelicula p : peliculas) {
                if (p.getCodigo() > max) {
                    max = p.getCodigo();
                }
            }
        }
        return max + 1;
    }

    public boolean registrarPelicula(int codigo, String nombre, String genero, int duracion, Clasificacion clasificacion, String estado) {
        String estadoFinal = (estado == null || estado.isEmpty()) ? "ACTIVO" : estado;
        Pelicula nueva = new Pelicula(codigo, nombre, genero, duracion, estadoFinal, clasificacion, "/uniquindio/edu/co/proyectofinal/images/deflt.jpg");
        return cine.registrarPelicula(nueva);
    }

    public boolean modificarPelicula(Pelicula original, String nombre, String genero, int duracion, Clasificacion clasificacion, String estado) {
        Pelicula actualizada = new Pelicula(original.getCodigo(), nombre, genero, duracion, estado, clasificacion, original.getImagenRuta());
        return cine.modificarPelicula(original.getCodigo(), actualizada);
    }

    public boolean deshabilitarPelicula(int codigo) {
        return cine.deshabilitarPelicula(codigo);
    }
}