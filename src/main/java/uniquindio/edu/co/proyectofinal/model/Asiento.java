package uniquindio.edu.co.proyectofinal.model;


public class Asiento {
    private int codigo;
    private String fila;
    private int numero;
    private EstadoAsiento estado;

    public Asiento(int codigo, String fila, int numero, EstadoAsiento estado) {
        this.codigo = codigo;
        this.fila = fila;
        this.numero = numero;
        this.estado = EstadoAsiento.DISPONIBLE;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }

    public String obtenerPuesto() {
        return fila + numero; // Ejemplo: "A1", "B3"
    }

    @Override
    public String toString() {
        return "Asiento: " +
                ", Código: " + codigo +
                ", Puesto: " + obtenerPuesto()+
                ", Estado: " + estado;
    }
}

