package uniquindio.edu.co.proyectofinal.model;

public enum TipoCliente {
    GENERAL (15000),
    ESTUDIANTE (12000),
    NINIO (10000),
    ADULTO_MAYOR (11000);

    private double valorEntrada;

    TipoCliente(double valorEntrada) {
        this.valorEntrada = valorEntrada;
    }

    public double getValorEntrada() {
        return valorEntrada;
    }
}
