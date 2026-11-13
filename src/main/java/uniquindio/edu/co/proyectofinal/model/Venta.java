package uniquindio.edu.co.proyectofinal.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private int codigo;
    private Cliente cliente;
    private LocalDateTime fechaHoraVenta;
    private double valorPagado;
    private EstadoVenta estadoVenta;
    private Vendedor vendedor;
    private List<DetalleVenta> listaDetalles;

    public Venta(int codigo, Cliente cliente, LocalDateTime fechaHoraVenta, double valorPagado, EstadoVenta estadoVenta, Vendedor vendedor) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.fechaHoraVenta = fechaHoraVenta;
        this.valorPagado= valorPagado;
        this.estadoVenta = estadoVenta;
        this.vendedor = vendedor;
        listaDetalles= new ArrayList<>();
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

    public List<DetalleVenta> getListaDetalles() {
        return listaDetalles;
    }

    public void setListaDetalles(List<DetalleVenta> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Venta: \n" +
                " | Código: " + codigo +
                " | Cliente: " + cliente +
                " | Valor: $" + valorPagado +
                " | Fecha: " + fechaHoraVenta.format(formato) +
                " | Estado: " + estadoVenta+
                " | Detalles: "+ listaDetalles;
    }
}