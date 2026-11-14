package uniquindio.edu.co.proyectofinal.model;

import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona{

    private List<Venta> listaCompras;

    public Cliente(int documento, String nombre) {
        super(documento, nombre);
        listaCompras= new ArrayList<>();
    }

    public List<Venta> getListaCompras() {
        return listaCompras;
    }

    public void setListaCompras(List<Venta> listaCompras) {
        this.listaCompras = listaCompras;
    }

    @Override
    public String toString() {
        return "Cliente: " +
                super.toString();
    }
}
