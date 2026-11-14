package uniquindio.edu.co.proyectofinal.controller;

import uniquindio.edu.co.proyectofinal.model.Cine;
import uniquindio.edu.co.proyectofinal.model.Pelicula;
import java.util.ArrayList;
import java.util.List;

public class CarteleraController {
    private Cine cine;

    public CarteleraController(Cine cine) {
        this.cine = cine;
    }

    public List<Pelicula> obtenerPeliculasDisponibles() {
        return (cine != null) ? cine.consultarPeliculasDisponibles() : new ArrayList<>();
    }
}