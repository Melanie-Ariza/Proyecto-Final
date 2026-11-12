package uniquindio.edu.co.proyectofinal.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Venta {
    private int codigo;
    private Cliente cliente;
    private Funcion funcion;
    private Asiento asiento;
    private LocalDateTime fechaHoraVenta;
    private double valorPagado;
    private EstadoVenta estadoVenta;
    private Vendedor vendedor;

    public Venta(int codigo, Cliente cliente, Funcion funcion, Asiento asiento, LocalDateTime fechaHoraVenta, double valorPagado, EstadoVenta estadoVenta, Vendedor vendedor) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asiento = asiento;
        this.fechaHoraVenta = fechaHoraVenta;
        this.valorPagado = valorPagado;
        this.estadoVenta = estadoVenta;
        this.vendedor = vendedor;
    }

    public void cancelarVenta() {
        if (this.estadoVenta == EstadoVenta.ACTIVA) {
            this.estadoVenta = EstadoVenta.CANCELADA;
            if (this.asiento != null) {
                this.asiento.setEstado(EstadoAsiento.DISPONIBLE);
            }
        }
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public LocalDateTime getFechaHoraVenta() {
        return fechaHoraVenta;
    }

    public void setFechaHoraVenta(LocalDateTime fechaHoraVenta) {
        this.fechaHoraVenta = fechaHoraVenta;
    }

    public double getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(double valorPagado) {
        this.valorPagado = valorPagado;
    }

    public EstadoVenta getEstadoVenta() {
        return estadoVenta;
    }

    public void setEstadoVenta(EstadoVenta estado) {
        this.estadoVenta = estado;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Venta: \n" +
                " | Código: "+codigo +
                " | Cliente: " + cliente +
                " | Asiento: " + asiento.obtenerPuesto() +
                " | Valor: $" + valorPagado +
                " | Fecha: " + fechaHoraVenta.format(formato) +
                " | Estado: " + estadoVenta;
    }
}