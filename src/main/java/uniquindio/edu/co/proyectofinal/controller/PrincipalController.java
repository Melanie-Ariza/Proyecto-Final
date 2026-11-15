package uniquindio.edu.co.proyectofinal.controller;

import uniquindio.edu.co.proyectofinal.model.Cine;

public class PrincipalController {

    private Cine cine;

    public PrincipalController() {
        this.cine = new Cine(1, "CINEMAUQ");
    }

    public Cine getCine() {
        return cine;
    }

    public void setCine(Cine cine) {
        this.cine = cine;
    }
}