package uniquindio.edu.co.proyectofinal.model;

import java.util.List;

public class DetalleVenta {
    private List<Entrada> listaEntradas;
    private int cantidad;

    public DetalleVenta(List<Entrada> listaEntradas, int cantidad) {
        this.listaEntradas = listaEntradas;
        this.cantidad = cantidad;
    }

    public List<Entrada> getListaEntradas() {
        return listaEntradas;
    }

    public void setListaEntradas(List<Entrada> listaEntradas) {
        this.listaEntradas = listaEntradas;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Detalle venta: \n" +
                ", Lista entradas:" + listaEntradas +
                ", Cantidad: " + cantidad;
    }
}
