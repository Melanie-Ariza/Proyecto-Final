package uniquindio.edu.co.proyectofinal.model;

public enum Cargo {
    ADMINISTRADOR (4000000),
    OPERADOR (2000000);

    private final double salario;

    Cargo(double salario) {
        this.salario = salario;
    }

    public double getSalario() {
        return salario;
    }
}
