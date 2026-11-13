package uniquindio.edu.co.proyectofinal.model;

public class Entrada {
    private int codigo;
    private Funcion funcion;
    private Asiento asiento;
    private TipoEntrada tipoEntrada;

    public Entrada(int codigo, Funcion funcion, Asiento asiento, TipoEntrada tipoEntrada) {
        this.codigo = codigo;
        this.funcion = funcion;
        this.asiento = asiento;
        this.tipoEntrada = tipoEntrada;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public TipoEntrada getTipoEntrada() {
        return tipoEntrada;
    }

    public void setTipoEntrada(TipoEntrada tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    @Override
    public String toString() {
        return "Entrada: " +
                "Código: " + codigo +
                ", Funcion : " + funcion +
                ", Asiento: " + asiento +
                ", Tipo de entrada:" + tipoEntrada;
    }
}
