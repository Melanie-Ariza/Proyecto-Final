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
    private EstadoVenta estado;
    private Vendedor vendedor;

    public Venta(int codigo, Cliente cliente, Funcion funcion, Asiento asiento, LocalDateTime fechaHoraVenta, double valorPagado, EstadoVenta estado, Vendedor vendedor) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asiento = asiento;
        this.fechaHoraVenta = fechaHoraVenta;
        this.valorPagado = valorPagado;
        this.estado = estado;
        this.vendedor = vendedor;
    }

    public void cancelarVenta() {
        if (this.estado == EstadoVenta.ACTIVA) {
            this.estado = EstadoVenta.CANCELADA;
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

    public EstadoVenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoVenta estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Venta #" + codigo +
                " | Cliente: " + (cliente != null ? cliente.getNombre() : "N/A") +
                " | Asiento: " + (asiento != null ? asiento.getIdentificador() : "N/A") +
                " | Valor: $" + valorPagado +
                " | Fecha: " + fechaHoraVenta.format(formatter) +
                " | Estado: " + estado;
    }
}