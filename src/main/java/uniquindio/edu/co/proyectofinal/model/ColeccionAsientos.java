package uniquindio.edu.co.proyectofinal.model;

public enum ColeccionAsientos {
    ESTANDAR (0),
    VIP (5000),
    MOVIMIENTO (8000);

    private double valorAsiento;

    ColeccionAsientos(double valorAsiento) {
        this.valorAsiento = valorAsiento;
    }

    public double getValorAsiento() {
        return valorAsiento;
    }
}
