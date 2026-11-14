package uniquindio.edu.co.proyectofinal.controller;

import uniquindio.edu.co.proyectofinal.model.Cine;
import uniquindio.edu.co.proyectofinal.model.Sala;
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
}