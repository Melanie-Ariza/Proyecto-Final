package uniquindio.edu.co.proyectofinal.model;
import java.util.ArrayList;
import java.util.List;

public class Sala {
    private int codigo;
    private int numero;
    private int capacidad;
    private EstadoSala estado;
    private ColeccionAsientos coleccionAsientos;
    private List<Asiento> listaAsientos;

    public Sala(int codigo, int numero, int capacidad, EstadoSala estado, ColeccionAsientos coleccionAsientos) {
        this.codigo = codigo;
        this.numero = numero;
        this.capacidad = capacidad;
        this.estado = estado;
        this.coleccionAsientos = coleccionAsientos;
        this.listaAsientos = new ArrayList<>();
        generarAsientos();
    }

    private void generarAsientos() {
        listaAsientos.clear();
        int sillasPorFila = 10;
        int contadorCodigo = 1;

        for (int i = 0; i < capacidad; i++) {
            char letraFila = (char) ('A' + (i / sillasPorFila));
            int numeroSilla = (i % sillasPorFila) + 1;
            listaAsientos.add(new Asiento(contadorCodigo++, String.valueOf(letraFila), numeroSilla));
        }
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
        generarAsientos();
    }

    public EstadoSala getEstado() {
        return estado;
    }

    public void setEstado(EstadoSala estado) {
        this.estado = estado;
    }

    public ColeccionAsientos getColeccionAsientos() {
        return coleccionAsientos;
    }

    public void setColeccionAsientos(ColeccionAsientos coleccionAsientos) {
        this.coleccionAsientos = coleccionAsientos;
    }

    public List<Asiento> getListaAsientos() {
        return listaAsientos;
    }

    public void setListaAsientos(List<Asiento> listaAsientos) {
        this.listaAsientos = listaAsientos;
    }

    public boolean estaDisponible() {
        return this.estado == EstadoSala.DISPONIBLE;
    }

    public int contarAsientosDisponibles() {
        int disponibles = 0;
        for (Asiento asiento : listaAsientos) {
            if (asiento.getEstado() == EstadoAsiento.DISPONIBLE) {
                disponibles++;
            }
        }
        return disponibles;
    }

    @Override
    public String toString() {
        return "Sala " + numero + " (" + coleccionAsientos + " - Cap: " + capacidad + " - " + estado + ")";
    }
}
