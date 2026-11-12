package uniquindio.edu.co.proyectofinal.model;

public class Vendedor extends Persona implements IVendedor{
    Cargo cargo;

    public Vendedor(int documento, String nombre, double salario, Cargo cargo) {
        super(documento, nombre);
        this.cargo = cargo;
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
