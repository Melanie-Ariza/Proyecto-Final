package uniquindio.edu.co.proyectofinal.model;

public class Cliente extends Persona{
    TipoCliente tipoCliente;

    public Cliente(int documento, String nombre, TipoCliente tipoCliente) {
        super(documento, nombre);
        this.tipoCliente = tipoCliente;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    @Override
    public String toString() {
        return "Cliente: " +
                super.toString()+
                ", Tipo de cliente: " + tipoCliente;
    }
}
