package uniquindio.edu.co.proyectofinal.model;

import java.util.ArrayList;
import java.util.List;

public class Pelicula {
    private int codigo;
    private String nombre;
    private String genero;
    private int duracion;
    private String estado;
    private List<Funcion> listaFunciones;

    public Pelicula(int codigo, String nombre, String genero, int duracion, String estado, List<Funcion> listaFunciones) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.genero = genero;
        this.duracion = duracion;
        this.estado = estado;
        this.listaFunciones = new ArrayList<>();
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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Funcion> getListaFunciones() {
        return listaFunciones;
    }

    public void setListaFunciones(List<Funcion> listaFunciones) {
        this.listaFunciones = listaFunciones;
    }

    @Override
    public String toString() {
        return "Pelicula: " +
                "codigo: " + codigo +
                ", nombre: " + nombre +
                ", genero: " + genero +
                ", duracion: " + duracion +
                ", estado: " + estado +
                ", listaFunciones: " + listaFunciones;
    }
}
