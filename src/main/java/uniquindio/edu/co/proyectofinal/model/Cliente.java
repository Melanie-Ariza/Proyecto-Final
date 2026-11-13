package uniquindio.edu.co.proyectofinal.model;

public class Cliente extends Persona{

    public Cliente(int documento, String nombre) {
        super(documento, nombre);
    }
    @Override
    public String toString() {
        return "Cliente: " +
                super.toString();
    }
}
