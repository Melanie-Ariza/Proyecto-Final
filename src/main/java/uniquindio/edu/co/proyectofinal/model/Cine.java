package uniquindio.edu.co.proyectofinal.model;

import java.util.ArrayList;
import java.util.List;

public class Cine {
    private int codigo;
    private String nombre;
    private List<Pelicula> listaPeliculas;

    public Cine(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.listaPeliculas = new ArrayList<>();
    }

    public List<Pelicula> getListaPeliculas() {
        return listaPeliculas;
    }

    public void setListaPeliculas(List<Pelicula> listaPeliculas) {
        this.listaPeliculas = listaPeliculas;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Cine: " +
                "codigo:" + codigo +
                ", nombre: " + nombre +
                ", listaPeliculas: " + listaPeliculas;
    }
}
