package uniquindio.edu.co.proyectofinal.model;

import java.util.ArrayList;
import java.util.List;

public class Cine {
    private int codigo;
    private String nombre;
    private List<Pelicula> listaPeliculas;
    private List<Persona> listaPersonas;
    private List<Funcion> listaFunciones;
    private List<Sala> listaSalas;

    public Cine(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.listaPeliculas = new ArrayList<>();
        this.listaPersonas = new ArrayList<>();
        this.listaFunciones = new ArrayList<>();
        this.listaSalas = new ArrayList<>();
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

    public List<Funcion> getListaFunciones() {
        return listaFunciones;
    }

    public void setListaFunciones(List<Funcion> listaFunciones) {
        this.listaFunciones = listaFunciones;
    }

    public List<Sala> getListaSalas() {
        return listaSalas;
    }

    public void setListaSalas(List<Sala> listaSalas) {
        this.listaSalas = listaSalas;
    }

    @Override
    public String toString() {
        return "Cine\n" +
                "Código:" + codigo +
                ", Nombre: " + nombre +
                ", Peliculas: " + listaPeliculas+
                ", Personas: "+listaPersonas+
                ", Funciones: "+listaPersonas+
                ", Salas: "+listaPersonas;
    }
}
