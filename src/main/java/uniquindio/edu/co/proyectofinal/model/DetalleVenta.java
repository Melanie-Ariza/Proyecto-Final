package uniquindio.edu.co.proyectofinal.model;

import java.util.ArrayList;
import java.util.List;

public class DetalleVenta {
    private List<Entrada> listaEntradas;
    private int cantidad;

    public DetalleVenta(int cantidad) {
        listaEntradas = new ArrayList<>();
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
                ", Cantidad: " + cantidad+
                ", Subtotal: "+ calcularSubtotal();
    }

    public double calcularSubtotal(){
        double subtotalFinal=0;
        double subtotal=0;
        for (Entrada entrada: listaEntradas){
            double valorEntrada= entrada.getTipoEntrada().getValorEntrada();
            double valorAsiento= entrada.getFuncion().getSala().getColeccionAsientos().getValorAsiento();
            subtotal+= valorEntrada + valorAsiento;
        }
        subtotalFinal= subtotal*cantidad;
        return subtotalFinal;
    }

    public boolean agregarEntrada(Entrada entrada){
        boolean agregada=true;
        for (Entrada entrada0: listaEntradas){
            if (entrada0.getCodigo()== entrada.getCodigo()){
                agregada=false;
                break;
            }
        }
        if (agregada){
            listaEntradas.add(entrada);
        }
        return agregada;
    }
}
