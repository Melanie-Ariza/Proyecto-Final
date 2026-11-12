package uniquindio.edu.co.proyectofinal.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Funcion {
    private int codigo;
    Pelicula pelicula;
    Sala sala;
    private LocalDate fecha;
    private LocalTime hora;
    private double valorBase;
    EstadoFuncion estadoFuncion;
    private List<Venta> listaVentas;

    public Funcion(int codigo, Pelicula pelicula, Sala sala, LocalTime hora, LocalDate fecha, double valorBase, EstadoFuncion estadoFuncion, List<Venta> listaVentas) {
        this.codigo = codigo;
        this.pelicula = pelicula;
        this.sala = sala;
        this.hora = hora;
        this.fecha = fecha;
        this.valorBase = valorBase;
        this.estadoFuncion = estadoFuncion;
        this.listaVentas = listaVentas;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }

    public EstadoFuncion getEstadoFuncion() {
        return estadoFuncion;
    }

    public void setEstadoFuncion(EstadoFuncion estadoFuncion) {
        this.estadoFuncion = estadoFuncion;
    }

    public List<Venta> getListaVentas() {
        return listaVentas;
    }

    public void setListaVentas(List<Venta> listaVentas) {
        this.listaVentas = listaVentas;
    }

    @Override
    public String toString() {
        return "Función\n" +
                "Código: " + codigo +
                ", Pelicula: " + pelicula +
                ", Sala:" + sala +
                ", Fecha: " + fecha +
                ", Hora: " + hora +
                ", Valor base: " + valorBase +
                ", Estado función: " + estadoFuncion +
                ", Lista ventas:" + listaVentas;
    }
}
