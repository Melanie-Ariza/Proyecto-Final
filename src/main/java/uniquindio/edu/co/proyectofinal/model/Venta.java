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
    private int cantidad;

    public Venta(int codigo, Cliente cliente, Funcion funcion, Asiento asiento, LocalDateTime fechaHoraVenta, EstadoVenta estadoVenta, Vendedor vendedor, int cantidad) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asiento = asiento;
        this.fechaHoraVenta = fechaHoraVenta;
        this.estadoVenta = estadoVenta;
        this.vendedor = vendedor;
        this.cantidad = cantidad;
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

    public void setEstadoVenta(EstadoVenta estadoVenta) {
        this.estadoVenta = estadoVenta;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Venta: \n" +
                " | Código: " + codigo +
                " | Cliente: " + cliente +
                " | Asiento: " + (asiento != null ? asiento.obtenerPuesto() : "N/A") +
                " | Cantidad: " + cantidad +
                " | Valor: $" + valorPagado +
                " | Fecha: " + fechaHoraVenta.format(formato) +
                " | Estado: " + estadoVenta;
    }
}