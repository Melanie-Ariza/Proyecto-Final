package uniquindio.edu.co.proyectofinal.controller;

import uniquindio.edu.co.proyectofinal.model.*;
import java.util.ArrayList;
import java.util.List;

public class SalasController {
    private Cine cine;

    public SalasController(Cine cine) {
        this.cine = cine;
    }

    public List<Sala> obtenerSalas() {
        return (cine != null) ? cine.getListaSalas() : new ArrayList<>();
    }

    public String registrarSala(int codigo, int numero, int capacidad, EstadoSala estado, ColeccionAsientos coleccion) {
        if (cine == null) return "Error de conexión con el cine.";

        // Verificar si ya existe el número o código de sala
        for (Sala s : cine.getListaSalas()) {
            if (s.getNumero() == numero || s.getCodigo() == codigo) {
                return "Ya existe una sala registrada con ese código o número.";
            }
        }

        Sala nuevaSala = new Sala(codigo, numero, capacidad, estado, coleccion);
        generarAsientosParaSala(nuevaSala);

        if (cine.registrarSala(nuevaSala)) {
            return null; // Éxito
        }
        return "No se pudo registrar la sala.";
    }

    public String modificarSala(Sala salaOriginal, int numero, int capacidad, EstadoSala estado, ColeccionAsientos coleccionAsientos) {
        if (salaOriginal == null || cine == null) return "Seleccione una sala válida.";

        // Validar si el nuevo número ya pertenece a otra sala diferente
        boolean numeroOcupado = cine.getListaSalas().stream()
                .anyMatch(s -> s.getNumero() == numero && s.getCodigo() != salaOriginal.getCodigo());
        if (numeroOcupado) return "Ya existe otra sala registrada con el número " + numero;

        salaOriginal.setNumero(numero);
        salaOriginal.setCapacidad(capacidad);
        salaOriginal.setEstado(estado);
        salaOriginal.setColeccionAsientos(coleccionAsientos);

        // Regenerar los asientos con la nueva capacidad o colección
        generarAsientosParaSala(salaOriginal);

        // Si la sala pasa a estar fuera de servicio, cancelar sus funciones programadas
        if (estado == EstadoSala.FUERA_DE_SERVICIO && cine.getListaFunciones() != null) {
            for (Funcion f : cine.getListaFunciones()) {
                if (f.getSala() != null && f.getSala().getNumero() == salaOriginal.getNumero()) {
                    if (f.getEstadoFuncion() == EstadoFuncion.PROGRAMADA) {
                        f.setEstadoFuncion(EstadoFuncion.CANCELADA);
                    }
                }
            }
        }

        return null; // Éxito
    }

    private void generarAsientosParaSala(Sala sala) {
        List<Asiento> asientos = new ArrayList<>();
        int capacidad = sala.getCapacidad();
        int maxColumnas = 10;
        char filaChar = 'A';
        int numeroEnFila = 1;

        // Generador de código automático e independiente (ej: 1, 2, 3... hasta la capacidad total)
        int codigoIncremental = 1;

        for (int i = 1; i <= capacidad; i++) {
            // El código del asiento es un número único independiente de su posición
            int codigo = codigoIncremental++;

            // Se crea el asiento manteniendo su fila (ej. 'A') y su número en la fila (ej. 2)
            Asiento asiento = new Asiento(codigo, String.valueOf(filaChar), numeroEnFila, EstadoAsiento.DISPONIBLE);
            asientos.add(asiento);

            numeroEnFila++;
            if (numeroEnFila > maxColumnas) {
                numeroEnFila = 1;
                filaChar++;
            }
        }
        sala.setListaAsientos(asientos);
    }

    public String deshabilitarSala(Sala salaSeleccionada) {
        if (salaSeleccionada == null) return "Seleccione una sala para deshabilitar.";

        salaSeleccionada.setEstado(EstadoSala.FUERA_DE_SERVICIO);

        if (cine != null && cine.getListaFunciones() != null) {
            for (Funcion f : cine.getListaFunciones()) {
                if (f.getSala() != null && f.getSala().getNumero() == salaSeleccionada.getNumero()) {
                    if (f.getEstadoFuncion() == EstadoFuncion.PROGRAMADA) {
                        f.setEstadoFuncion(EstadoFuncion.CANCELADA);
                    }
                }
            }
        }
        return null; // Éxito
    }
}