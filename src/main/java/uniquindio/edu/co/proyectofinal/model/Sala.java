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

    @Override
    public String toString() {
        return "Sala: " +
                "Código: " + codigo +
                ", Número: " + numero +
                ", Capacidad: " + capacidad +
                ", Estado: " + estado +
                ", Colección asientos: " + coleccionAsientos +
                ", Lista asientos: " + listaAsientos;
    }

    public void generarAsientos() {
        listaAsientos.clear();
        int sillasPorFila = 10;
        int contadorCodigo = 1;

        // Calculamos el total de filas necesarias según la capacidad
        int filas = (int) Math.ceil((double) capacidad / sillasPorFila);
        int asientosCreados = 0;

        for (int i = 0; i < filas; i++) {
            // Determinamos la letra de la fila
            char letraFila = (char) ('A' + i);
            String nombreFila = String.valueOf(letraFila);

            for (int j = 1; j <= sillasPorFila && asientosCreados < capacidad; j++) {
                int numeroSilla = j;
                listaAsientos.add(new Asiento(contadorCodigo++, nombreFila, numeroSilla, EstadoAsiento.DISPONIBLE));
                asientosCreados++;
            }
        }
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
}
