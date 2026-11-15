package uniquindio.edu.co.proyectofinal.controller;

import uniquindio.edu.co.proyectofinal.model.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FuncionController {
    private Cine cine;

    public FuncionController(Cine cine) {
        this.cine = cine;
    }

    public List<Funcion> obtenerTodasLasFunciones() {
        return (cine != null && cine.getListaFunciones() != null) ? cine.getListaFunciones() : new ArrayList<>();
    }

    public List<Pelicula> obtenerPeliculasDisponibles() {
        return (cine != null) ? cine.consultarPeliculasDisponibles() : new ArrayList<>();
    }

    public List<Sala> obtenerSalasDisponibles() {
        List<Sala> disponibles = new ArrayList<>();
        if (cine != null && cine.getListaSalas() != null) {
            for (Sala s : cine.getListaSalas()) {
                // Solo retorna las salas que NO estén fuera de servicio
                if (s.getEstado() == EstadoSala.DISPONIBLE) {
                    disponibles.add(s);
                }
            }
        }
        return disponibles;
    }

    public List<Funcion> consultarFuncionesPorFecha(LocalDate fecha) {
        if (cine != null && fecha != null) {
            return cine.consultarFuncionesPorFecha(fecha);
        }
        return new ArrayList<>();
    }

    /**
     * Valida si la sala está fuera de servicio o ocupada por otra función en la misma fecha y horario.
     * Retorna un mensaje de error si no está disponible, o null si se puede usar.
     */
    public String validarDisponibilidadSala(Sala sala, Pelicula pelicula, LocalDate fecha, LocalTime horaInicio, Integer codigoExcluir) {
        if (sala.getEstado() == EstadoSala.FUERA_DE_SERVICIO) {
            return "La Sala #" + sala.getNumero() + " se encuentra FUERA DE SERVICIO.";
        }

        // Calcular hora de fin según la duración de la película (si no tiene duración, se asumen 120 min)
        int duracionMin = (pelicula != null && pelicula.getDuracion() > 0) ? pelicula.getDuracion() : 120;
        LocalTime horaFin = horaInicio.plusMinutes(duracionMin);

        for (Funcion f : obtenerTodasLasFunciones()) {
            // Si estamos modificando una función, ignoramos la misma función en la comparación
            if (codigoExcluir != null && f.getCodigo() == codigoExcluir) {
                continue;
            }

            // Solo validar contra funciones que estén activas/programadas en la misma sala y fecha
            if (f.getEstadoFuncion() == EstadoFuncion.PROGRAMADA
                    && f.getSala() != null && f.getSala().getNumero() == sala.getNumero()
                    && f.getFecha() != null && f.getFecha().equals(fecha)) {

                int duracionExistente = (f.getPelicula() != null && f.getPelicula().getDuracion() > 0)
                        ? f.getPelicula().getDuracion() : 120;
                LocalTime inicioExistente = f.getHora();
                LocalTime finExistente = inicioExistente.plusMinutes(duracionExistente);

                // Verificar si los rangos de tiempo se cruzan
                boolean hayCruce = horaInicio.isBefore(finExistente) && horaFin.isAfter(inicioExistente);
                if (hayCruce) {
                    return "La Sala #" + sala.getNumero() + " ya está en uso por la película '"
                            + f.getPelicula().getNombre() + "' (" + inicioExistente + " - " + finExistente + ").";
                }
            }
        }
        return null; // Sala disponible
    }

    public String programarFuncion(Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora, double valorBase) {
        if (cine == null || pelicula == null || sala == null || fecha == null || hora == null) {
            return "Datos incompletos para programar la función.";
        }

        // Validar estado de la sala y cruce de funciones
        String errorSala = validarDisponibilidadSala(sala, pelicula, fecha, hora, null);
        if (errorSala != null) {
            return errorSala;
        }

        int codigo = cine.getListaFunciones().size() + 1;
        while (true) {
            int codigoTemp = codigo;
            boolean existe = cine.getListaFunciones().stream().anyMatch(f -> f.getCodigo() == codigoTemp);
            if (!existe) break;
            codigo++;
        }

        Funcion nuevaFuncion = new Funcion(codigo, pelicula, sala, hora, fecha, valorBase, EstadoFuncion.PROGRAMADA);
        boolean registrada = cine.programarFuncion(pelicula, nuevaFuncion);
        return registrada ? null : "No se pudo registrar la función en el sistema.";
    }

    public String modificarFuncion(int codigo, Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora, double valorBase, EstadoFuncion estado) {
        if (cine == null) return "Error de conexión con el sistema.";

        // Si el estado resultante será PROGRAMADA, validar que la sala no esté fuera de servicio ni ocupada
        if (estado == EstadoFuncion.PROGRAMADA) {
            String errorSala = validarDisponibilidadSala(sala, pelicula, fecha, hora, codigo);
            if (errorSala != null) {
                return errorSala;
            }
        }

        for (Funcion f : cine.getListaFunciones()) {
            if (f.getCodigo() == codigo) {
                f.setPelicula(pelicula);
                f.setSala(sala);
                f.setFecha(fecha);
                f.setHora(hora);
                f.setValorBase(valorBase);
                f.setEstadoFuncion(estado);
                return null;
            }
        }
        return "No se encontró la función seleccionada.";
    }
}