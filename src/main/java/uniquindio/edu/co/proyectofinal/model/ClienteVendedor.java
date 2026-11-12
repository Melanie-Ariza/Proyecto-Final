package uniquindio.edu.co.proyectofinal.model;

public class ClienteVendedor extends  Cliente implements IVendedor{
    Cargo cargo;
    public ClienteVendedor(int documento, String nombre, TipoCliente tipoCliente) {
        super(documento, nombre, tipoCliente);
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return "Cliente vendedor: " +
                super.toString()+
                ", Cargo: " + cargo;
    }

    @Override
    public double calcularDescuento() {
        return cargo.getSalario()*DESCUENTO;
    }
}
