package uniquindio.edu.co.proyectofinal.model;

public enum TipoEntrada {
    GENERAL (15000),
    ESTUDIANTE (12000),
    NIÑO (10000),
    ADULTO_MAYOR (11000);

    private double valorEntrada;

    TipoEntrada(double valorEntrada) {
        this.valorEntrada = valorEntrada;
    }

    public double getValorEntrada() {
        return valorEntrada;
    }

}
