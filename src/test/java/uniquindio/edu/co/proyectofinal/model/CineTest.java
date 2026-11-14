package uniquindio.edu.co.proyectofinal.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniquindio.edu.co.proyectofinal.Excepciones.FuncionInexistenteException.FuncionInexistenteException;
import uniquindio.edu.co.proyectofinal.Excepciones.PeliculaDuplicadaException.PeliculaDuplicadaException;
import uniquindio.edu.co.proyectofinal.Excepciones.SalaDuplicadaException.SalaDuplicadaException;
import uniquindio.edu.co.proyectofinal.Excepciones.VentaInexistenteException.VentaInexistenteException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CineTest {

    private Cine cine;

    @BeforeEach
    public void setUp() {
        cine = new Cine(1, "Cinema Central");
    }

    @Test
    public void testRegistrarYConsultarPeliculaExitoso() {
        Pelicula pelicula = new Pelicula(
                1,
                "Avatar",
                "Ciencia Ficción",
                160,
                "ACTIVO",
                new ArrayList<>(),
                null,
                "/img/avatar.jpg"
        );

        boolean resultado = cine.registrarPelicula(pelicula);

        assertTrue(resultado);
        Pelicula encontrada = cine.consultarPelicula(1);
        assertEquals("Avatar", encontrada.getNombre());
        assertEquals("Ciencia Ficción", encontrada.getGenero());
        assertEquals(160, encontrada.getDuracion());
    }

    @Test
    public void testRegistrarPeliculaDuplicadaExcepcion() {
        Pelicula pelicula1 = new Pelicula(1, "Avatar", "Ciencia Ficción", 160, "ACTIVO", new ArrayList<>(), null, "/img/avatar.jpg");
        Pelicula pelicula2 = new Pelicula(1, "Avatar 2", "Ciencia Ficción", 180, "ACTIVO", new ArrayList<>(), null, "/img/avatar2.jpg");

        cine.registrarPelicula(pelicula1);

        assertThrows(PeliculaDuplicadaException.class, () -> {
            cine.registrarPelicula(pelicula2);
        });
    }

    @Test
    public void testRegistrarYConsultarSalaConAsientosVipExitoso() {
        Sala sala = new Sala(1, 1, 20, EstadoSala.DISPONIBLE, ColeccionAsientos.VIP);

        boolean resultado = cine.registrarSala(sala);

        assertTrue(resultado);
        Sala encontrada = cine.consultarSala(1);
        assertEquals(20, encontrada.getCapacidad());
        assertEquals(ColeccionAsientos.VIP, encontrada.getColeccionAsientos());
        assertEquals(5000.0, encontrada.getColeccionAsientos().getValorAsiento());
    }

    @Test
    public void testRegistrarSalaDuplicadaExcepcion() {
        Sala sala1 = new Sala(1, 1, 20, EstadoSala.DISPONIBLE, ColeccionAsientos.ESTANDAR);
        Sala sala2 = new Sala(1, 2, 30, EstadoSala.DISPONIBLE, ColeccionAsientos.ESTANDAR);

        cine.registrarSala(sala1);

        assertThrows(SalaDuplicadaException.class, () -> {
            cine.registrarSala(sala2);
        });
    }

    @Test
    public void testProgramarFuncionExitoso() {
        Pelicula pelicula = new Pelicula(1, "Matrix", "Acción", 136, "ACTIVO", new ArrayList<>(), null, "/img/matrix.jpg");
        cine.registrarPelicula(pelicula);

        Sala sala = new Sala(1, 1, 20, EstadoSala.DISPONIBLE, ColeccionAsientos.ESTANDAR);
        cine.registrarSala(sala);

        Funcion funcion = new Funcion(
                1,
                pelicula,
                sala,
                LocalTime.of(18, 0),
                LocalDate.now(),
                15000.0,
                EstadoFuncion.PROGRAMADA
        );

        boolean programada = cine.programarFuncion(pelicula, funcion);

        assertTrue(programada);
        assertEquals(1, cine.getListaFunciones().size());
        assertEquals(15000.0, funcion.getValorBase());
    }

    @Test
    public void testConsultarTotalIngresosGeneradosConVentasActivas() {
        // Prepara los objetos necesarios para registrar una venta simulada
        Cliente cliente = new Cliente(1001, "Carlos Pérez");
        Cargo cargo = Cargo.ADMINISTRADOR;
        Vendedor vendedor = new Vendedor(102, "Ana Gomez", 2000000, Cargo.ADMINISTRADOR);

        Venta venta = new Venta(1, cliente, LocalDateTime.now(), 25000.0, EstadoVenta.ACTIVA, vendedor);
        cine.getListaVentas().add(venta);

        double ingresos = cine.consultarTotalIngresosGenerados();
        int totalEntradas = cine.consultarTotalEntradasVendidas();

        assertEquals(25000.0, ingresos);
        assertEquals(1, totalEntradas);
    }

    @Test
    public void testCancelarVentaInexistenteExcepcion() {
        // Valida que al intentar cancelar una venta usando un código que no existe salte la excepción
        assertThrows(VentaInexistenteException.class, () -> {
            cine.cancelarVenta(888);
        });
    }

    @Test
    public void testConsultarPeliculasDisponiblesFiltroActivo() {
        Pelicula peliculaActiva = new Pelicula(1, "Batman", "Acción", 140, "ACTIVO", new ArrayList<>(), null, "/img/batman.jpg");
        Pelicula peliculaInactiva = new Pelicula(2, "Antigua", "Drama", 120, "NO_DISPONIBLE", new ArrayList<>(), null, "/img/old.jpg");

        cine.registrarPelicula(peliculaActiva);
        cine.registrarPelicula(peliculaInactiva);

        // Debe retornar únicamente las que tienen estado ACTIVO
        assertEquals(1, cine.consultarPeliculasDisponibles().size());
        assertEquals("Batman", cine.consultarPeliculasDisponibles().get(0).getNombre());
    }

    @Test
    public void testRegistrarVentaNulaExcepcion() {
        assertThrows(VentaInexistenteException.class, () -> {
            cine.registrarVenta(null);
        });
    }

    @Test
    public void testConsultarVentaInexistenteExcepcion() {
        assertThrows(VentaInexistenteException.class, () -> {
            cine.consultarVenta(999);
        });
    }

    @Test
    public void testConsultarOcupacionFuncionInexistenteExcepcion() {
        assertThrows(FuncionInexistenteException.class, () -> {
            cine.consultarOcupacionFuncion(999);
        });
    }

    @Test
    public void testConsultarDisponibilidadAsientos() {
        Pelicula pelicula = new Pelicula(1, "Matrix", "Acción", 136, "ACTIVO", new ArrayList<>(), null, "/img/matrix.jpg");
        cine.registrarPelicula(pelicula);

        Sala sala = new Sala(1, 1, 10, EstadoSala.DISPONIBLE, ColeccionAsientos.MOVIMIENTO);
        cine.registrarSala(sala);

        Funcion funcion = new Funcion(
                1,
                pelicula,
                sala,
                LocalTime.of(18, 0),
                LocalDate.now(),
                18000.0,
                EstadoFuncion.PROGRAMADA
        );
        cine.programarFuncion(pelicula, funcion);

        int disponibles = cine.consultarDisponibilidadAsientos(1);

        assertEquals(10, disponibles);
    }
    @Test
    public void testCancelarVentaExitosoYLiberaAsientos() {
        // Preparamos película, sala con 1 asiento, y función
        Pelicula pelicula = new Pelicula(1, "Avatar", "Ciencia Ficción", 160, "ACTIVO", new ArrayList<>(), null, "/img/avatar.jpg");
        cine.registrarPelicula(pelicula);

        Sala sala = new Sala(1, 1, 1, EstadoSala.DISPONIBLE, ColeccionAsientos.ESTANDAR);
        cine.registrarSala(sala);

        Funcion funcion = new Funcion(1, pelicula, sala, LocalTime.of(20, 0), LocalDate.now(), 10000.0, EstadoFuncion.PROGRAMADA);
        cine.programarFuncion(pelicula, funcion);

        // Creamos una venta simulada asociada a la función
        Cliente cliente = new Cliente(123, "Juan Pérez");
        Vendedor vendedor = new Vendedor(456, "Maria Lopez", 2000000, Cargo.OPERADOR);

        Venta venta = new Venta(1, cliente, LocalDateTime.now(), 10000.0, EstadoVenta.ACTIVA, vendedor);

        // Agregamos un detalle y una entrada que ocupe el asiento
        Asiento asiento = sala.getListaAsientos().get(0);
        asiento.setEstado(EstadoAsiento.OCUPADO);

        Entrada entrada = new Entrada(1, funcion, asiento, TipoEntrada.GENERAL);
        DetalleVenta detalle = new DetalleVenta(1);
        detalle.agregarEntrada(entrada);
        venta.getListaDetalles().add(detalle);

        cine.getListaVentas().add(venta);

        // Ejecutamos la cancelación de la venta por código
        cine.cancelarVenta(1);

        // Verificamos que la venta esté cancelada y el asiento haya vuelto a estar DISPONIBLE
        assertEquals(EstadoVenta.CANCELADA, venta.getEstadoVenta());
        assertEquals(EstadoAsiento.DISPONIBLE, asiento.getEstado());
    }

    @Test
    public void testRegistrarVentaFuncionSinCupoExcepcion() {
        Pelicula pelicula = new Pelicula(1, "Avatar", "Ciencia Ficción", 160, "ACTIVO", new ArrayList<>(), null, "/img/avatar.jpg");
        cine.registrarPelicula(pelicula);

        // Sala con capacidad de 0 o simulando cupos agotados
        Sala sala = new Sala(1, 1, 0, EstadoSala.DISPONIBLE, ColeccionAsientos.ESTANDAR);
        cine.registrarSala(sala);

        Funcion funcion = new Funcion(1, pelicula, sala, LocalTime.of(20, 0), LocalDate.now(), 10000.0, EstadoFuncion.PROGRAMADA);
        cine.programarFuncion(pelicula, funcion);

        Cliente cliente = new Cliente(123, "Juan Pérez");
        Vendedor vendedor = new Vendedor(456, "Maria Lopez", 2000000, Cargo.OPERADOR);

        Venta venta = new Venta(1, cliente, LocalDateTime.now(), 10000.0, EstadoVenta.ACTIVA, vendedor);


        Asiento asientoFicticio = new Asiento(1, "A", 1, EstadoAsiento.DISPONIBLE);
        Entrada entrada = new Entrada(1, funcion, asientoFicticio, TipoEntrada.GENERAL);
        DetalleVenta detalle = new DetalleVenta(1);
        detalle.agregarEntrada(entrada);
        venta.getListaDetalles().add(detalle);
        
        assertThrows(uniquindio.edu.co.proyectofinal.Excepciones.FuncionSinCupoException.FuncionSinCupoException.class, () -> {
            cine.registrarVenta(venta);
        });
    }
}