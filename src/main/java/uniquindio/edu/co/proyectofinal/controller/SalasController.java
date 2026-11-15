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

    public boolean registrarSala(int codigo, int numero, int capacidad, EstadoSala estado, ColeccionAsientos coleccionAsientos) {
        if (cine == null) return false;

        // Validar si ya existe una sala con el mismo código o el mismo número
        boolean existe = cine.getListaSalas().stream()
                .anyMatch(s -> s.getCodigo() == codigo || s.getNumero() == numero);
        if (existe) return false;

        Sala nuevaSala = new Sala(codigo, numero, capacidad, estado, coleccionAsientos);
        return cine.registrarSala(nuevaSala);
    }

    public boolean modificarSala(Sala salaOriginal, int numero, int capacidad, EstadoSala estado, ColeccionAsientos coleccionAsientos) {
        if (salaOriginal == null || cine == null) return false;

        // Validar si el nuevo número ya pertenece a otra sala diferente
        boolean numeroOcupado = cine.getListaSalas().stream()
                .anyMatch(s -> s.getNumero() == numero && s.getCodigo() != salaOriginal.getCodigo());
        if (numeroOcupado) return false;

        salaOriginal.setNumero(numero);
        salaOriginal.setCapacidad(capacidad);
        salaOriginal.setEstado(estado);
        salaOriginal.setColeccionAsientos(coleccionAsientos);

        salaOriginal.generarAsientos();

        return true;
    }

    public boolean deshabilitarSala(Sala sala) {
        if (sala == null) return false;
        // Dependiendo de tu enumerador, puedes cambiar el estado a INACTIVA o similar
        sala.setEstado(EstadoSala.FUERA_DE_SERVICIO); // O el valor correspondiente en tu app
        return true;
    }
}