package uniquindio.edu.co.proyectofinal.model;

import java.util.ArrayList;
import java.util.List;

public class Cine {
    private int codigo;
    private String nombre;
    private List<Pelicula> listaPeliculas;
    private List<Persona> listaPersonas;

    public Cine(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.listaPeliculas = new ArrayList<>();
        this.listaPersonas = new ArrayList<>();
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

    public List<Pelicula> getListaPeliculas() {
        return listaPeliculas;
    }

    public void setListaPeliculas(List<Pelicula> listaPeliculas) {
        this.listaPeliculas = listaPeliculas;
    }

    public List<Persona> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Persona> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    @Override
    public String toString() {
        return "Cine\n" +
                "Código:" + codigo +
                ", Nombre: " + nombre +
                ", Lista peliculas: " + listaPeliculas+
                ", Lista Personas: "+listaPersonas;
    }
}
