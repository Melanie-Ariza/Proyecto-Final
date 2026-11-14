package uniquindio.edu.co.proyectofinal.model;

import java.util.ArrayList;
import java.util.List;

public class Vendedor extends Persona implements IVendedor{
    Cargo cargo;
    private List<Venta> listaVentas;
    public Vendedor(int documento, String nombre, double salario, Cargo cargo) {
        super(documento, nombre);
        this.cargo = cargo;
        listaVentas= new ArrayList<>();
    }
    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public List<Venta> getListaVentas() {
        return listaVentas;
    }

    public void setListaVentas(List<Venta> listaVentas) {
        this.listaVentas = listaVentas;
    }

    @Override
    public String toString() {
        return "Vendedor: " +
                super.toString()+
                ", Salario: " + cargo.getSalario()+
                ", Cargo: " + cargo;
    }

    @Override
    public double calcularDescuento() {
        return cargo.getSalario()*DESCUENTO;
    }
}
