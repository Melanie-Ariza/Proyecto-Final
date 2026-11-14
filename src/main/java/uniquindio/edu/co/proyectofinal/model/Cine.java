package uniquindio.edu.co.proyectofinal.model;

import uniquindio.edu.co.proyectofinal.Excepciones.FuncionInexistenteException.FuncionInexistenteException;
import uniquindio.edu.co.proyectofinal.Excepciones.FuncionSinCupoException.FuncionSinCupoException;
import uniquindio.edu.co.proyectofinal.Excepciones.PeliculaDuplicadaException.PeliculaDuplicadaException;
import uniquindio.edu.co.proyectofinal.Excepciones.SalaDuplicadaException.SalaDuplicadaException;
import uniquindio.edu.co.proyectofinal.Excepciones.VentaInexistenteException.VentaInexistenteException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cine {
    private int codigo;
    private String nombre;
    private List<Pelicula> listaPeliculas;
    private List<Persona> listaPersonas;
    private List<Funcion> listaFunciones;
    private List<Sala> listaSalas;
    private List<Venta> listaVentas;
    private List<Cliente> listaClientes;
    private List<Vendedor> listaVendedores;
    private List<ClienteVendedor> listaClienteVendedores;

    public Cine(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        listaPeliculas = new ArrayList<>();
        listaPersonas = new ArrayList<>();
        listaFunciones = new ArrayList<>();
        listaSalas = new ArrayList<>();
        listaVentas = new ArrayList<>();
        listaClientes = new ArrayList<>();
        listaVendedores = new ArrayList<>();
        listaClienteVendedores = new ArrayList<>();
    }

    public List<Funcion> getListaFunciones() {
        return listaFunciones;
    }

    public void setListaFunciones(List<Funcion> listaFunciones) {
        this.listaFunciones = listaFunciones;
    }

    public List<Sala> getListaSalas() {
        return listaSalas;
    }

    public void setListaSalas(List<Sala> listaSalas) {
        this.listaSalas = listaSalas;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Pelicula> getListaPeliculas() {
        return listaPeliculas;
    }

    public void setListaPeliculas(List<Pelicula> listaPeliculas) {
        this.listaPeliculas = listaPeliculas;
    }

    public List<Persona> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Persona> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    public List<Venta> getListaVentas() {
        return listaVentas;
    }

    public void setListaVentas(List<Venta> listaVentas) {
        this.listaVentas = listaVentas;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public List<Vendedor> getListaVendedores() {
        return listaVendedores;
    }

    public void setListaVendedores(List<Vendedor> listaVendedores) {
        this.listaVendedores = listaVendedores;
    }

    public List<ClienteVendedor> getListaClienteVendedores() {
        return listaClienteVendedores;
    }

    public void setListaClienteVendedores(List<ClienteVendedor> listaClienteVendedores) {
        this.listaClienteVendedores = listaClienteVendedores;
    }

    @Override
    public String toString() {
        return "Cine\n" +
                "Código:" + codigo +
                ", Nombre: " + nombre +
                ", Peliculas: " + listaPeliculas+
                ", Personas: "+listaPersonas +
                ", Funciones: "+listaFunciones+
                ", Salas: "+listaSalas+
                ", Ventas: "+ listaVentas;
    }

    // ==========================================
    // 1. GESTIÓN DE PELÍCULAS (CRUD)
    // ==========================================
    public boolean registrarPelicula(Pelicula pelicula) {
        boolean agregada = false;

        // 1. Validar pelicula repetida
        if (consultarPelicula(pelicula.getCodigo()) != null) {
            throw new PeliculaDuplicadaException("La película ya existe en el sistema con el código " + pelicula.getCodigo());
        }

        listaPeliculas.add(pelicula);
        agregada = true;

        return agregada;
    }

    public Pelicula consultarPelicula(int codigo) {
        Pelicula peliculaEncontrada = null;
        for (Pelicula pelicula : listaPeliculas) {
            if (pelicula.getCodigo() == codigo) {
                peliculaEncontrada = pelicula;
                break;
            }
        }
        return peliculaEncontrada;
    }

    public boolean modificarPelicula(int codigo, Pelicula peliculaActualizada) {
        boolean estaActualizada = false;
        Pelicula pelicula = consultarPelicula(codigo);
        if (pelicula != null && peliculaActualizada != null) {
            pelicula.setNombre(peliculaActualizada.getNombre());
            pelicula.setGenero(peliculaActualizada.getGenero());
            pelicula.setDuracion(peliculaActualizada.getDuracion());
            pelicula.setEstado(peliculaActualizada.getEstado());
            pelicula.setClasificacion(peliculaActualizada.getClasificacion());
            estaActualizada = true;
        }
        return estaActualizada;
    }

    public boolean deshabilitarPelicula(int codigo) {
        boolean deshabilitada = false;
        Pelicula pelicula = consultarPelicula(codigo);
        if (pelicula != null) {
            pelicula.setEstado("NO_DISPONIBLE");
            deshabilitada = true;
        }
        return deshabilitada;
    }

    // ==========================================
    // 2. GESTIÓN DE SALAS (CRUD)
    // ==========================================

    public boolean registrarSala(Sala sala) {
        boolean agregada = false;
        if (consultarSala(sala.getCodigo()) != null) {
            throw new SalaDuplicadaException("La sala ya existe en la base de datos con el codigo " + sala.getCodigo());
        }
            listaSalas.add(sala);
            agregada = true;

        return agregada;
    }

    public Sala consultarSala(int codigoSala) {
        for (Sala sala : listaSalas) {
            if (sala.getCodigo() == codigoSala) {
                return sala;
            }
        }
        return null;
    }

    public boolean modificarSala(int codigoSala, Sala salaActualizada) {
        Sala sala = consultarSala(codigoSala);
        if (sala != null && salaActualizada != null) {
            sala.setNumero(salaActualizada.getNumero());
            sala.setCapacidad(salaActualizada.getCapacidad());
            sala.setEstado(salaActualizada.getEstado());
            sala.setColeccionAsientos(salaActualizada.getColeccionAsientos());
            sala.generarAsientos();
            return true;
        }
        return false;
    }

    public boolean deshabilitarSala(int codigo) {
        boolean deshabilitada = false;
        Sala sala = consultarSala(codigo);
        if (sala != null) {
            sala.setEstado(EstadoSala.FUERA_DE_SERVICIO);
            deshabilitada = true;
        }
        return deshabilitada;
    }

    // ==========================================
    // 3. GESTIÓN DE FUNCIONES (CRUD)
    // ==========================================
    public boolean programarFuncion(Pelicula pelicula, Funcion nuevaFuncion) {
        if (pelicula != null && nuevaFuncion != null && consultarFuncion(nuevaFuncion.getCodigo()) == null) {
            if ("ACTIVO".equalsIgnoreCase(pelicula.getEstado())
                    && nuevaFuncion.getSala() != null
                    && nuevaFuncion.getSala().getEstado() == EstadoSala.DISPONIBLE) {
                listaFunciones.add(nuevaFuncion);
                pelicula.getListaFunciones().add(nuevaFuncion);
                return true;
            }
        }
        return false;
    }

    public Funcion consultarFuncion(int codigo) {
        Funcion funcion0= null;
        for (Funcion funcion : listaFunciones) {
            if (funcion.getCodigo() == codigo){
                funcion0=funcion;
            }
        }
        return funcion0;
    }

    public boolean modificarFuncion(int codigo, Funcion funcionActualizada) {
        boolean estaActualizada= false;
        Funcion funcion = consultarFuncion(codigo);
        if (funcion != null) {
            funcion.setPelicula(funcionActualizada.getPelicula());
            funcion.setSala(funcionActualizada.getSala());
            funcion.setFecha(funcionActualizada.getFecha());
            funcion.setHora(funcionActualizada.getHora());
            funcion.setEstadoFuncion(funcionActualizada.getEstadoFuncion());
            estaActualizada= true;
        }
        return estaActualizada;
    }

    public boolean deshabilitarFuncion(int codigo) {
        boolean deshabilitada=false;
        Funcion funcion= consultarFuncion(codigo);
        if (funcion != null) {
            funcion.setEstadoFuncion(EstadoFuncion.CANCELADA);
            deshabilitada=true;
        }
        return deshabilitada;
    }
    // ==========================================
    // 4. GESTION DE VENTAS
    // ==========================================

    // Registrar una venta (usado por la interfaz gráfica y pruebas)
    public boolean registrarVenta(Venta venta) {
        if (venta == null) {
            throw new VentaInexistenteException("La venta no puede ser nula.");
        }

        for (DetalleVenta detalle : venta.getListaDetalles()) {
            for (Entrada entrada : detalle.getListaEntradas()) {
                Funcion funcion = entrada.getFuncion();

                if (funcion == null || consultarFuncion(funcion.getCodigo()) == null) {
                    throw new FuncionInexistenteException("La función asociada a la venta no existe.");
                }

                if (funcion.getSala() == null || funcion.getSala().contarAsientosDisponibles() <= 0) {
                    throw new FuncionSinCupoException("La función " + funcion.getCodigo() + " no tiene cupos disponibles.");
                }
            }
        }

        listaVentas.add(venta);
        return true;
    }

    // Consultar una venta por código (lanza VentaInexistenteException si no existe)
    public Venta consultarVenta(int codigo) {
        for (Venta venta : listaVentas) {
            if (venta.getCodigo() == codigo) {
                return venta;
            }
        }
        throw new VentaInexistenteException("No existe ninguna venta registrada con el código " + codigo);
    }

    // Calcula el valor total
    public double calcularValorTotal(int codigo, Persona cliente) {
        double total = 0;

        for (Venta venta : listaVentas) {
            if (venta.getCodigo() == codigo) {
                for (DetalleVenta detalle : venta.getListaDetalles()) {
                    total += detalle.calcularSubtotal();
                }
            }
        }

        if (cliente instanceof ClienteVendedor) {
            total = total * ((ClienteVendedor) cliente).calcularDescuento();
        }
        return total;
    }

    // Cancelar venta recibiendo el objeto Venta
    public void cancelarVenta(Venta venta) {
        if (venta == null || !listaVentas.contains(venta)) {
            throw new VentaInexistenteException("La venta que intenta cancelar no existe en el sistema.");
        }

        if (venta.getEstadoVenta() == EstadoVenta.ACTIVA) {
            venta.setEstadoVenta(EstadoVenta.CANCELADA);
            for (DetalleVenta detalle : venta.getListaDetalles()) {
                for (Entrada entrada : detalle.getListaEntradas()) {
                    if (entrada.getAsiento() != null) {
                        entrada.getAsiento().setEstado(EstadoAsiento.DISPONIBLE);
                    }
                }
            }
        }
    }

    // Sobrecarga para cancelar venta buscando por su código
    public void cancelarVenta(int codigoVenta) {
        Venta venta = consultarVenta(codigoVenta); // Lanza VentaInexistenteException si no la encuentra
        cancelarVenta(venta);
    }

    public void comenzarVenta(Cliente cliente, Vendedor vendedor) {
        Scanner sc = new Scanner(System.in);

        // Generar un código automático sin fallar si la lista está vacía
        int codigoVenta = (listaVentas == null || listaVentas.isEmpty())
                ? 1
                : listaVentas.get(listaVentas.size() - 1).getCodigo() + 1;

        // Mostrar funciones disponibles
        System.out.println("\n--- FUNCIONES DISPONIBLES ---");
        boolean hayFunciones = false;
        for (Funcion funcion : listaFunciones) {
            if (funcion.getEstadoFuncion() == EstadoFuncion.PROGRAMADA) {
                System.out.println(funcion);
                hayFunciones = true;
            }
        }
        if (!hayFunciones) {
            System.out.println("No hay funciones programadas en este momento.");
            return;
        }

        // Seleccionar una función
        System.out.println("\nIngrese código de la función deseada: ");
        int codigoFuncion = sc.nextInt();

        Funcion funcionSeleccionada = consultarFuncion(codigoFuncion);
        if (funcionSeleccionada == null) {
            throw new FuncionInexistenteException("La función ingresada no existe.");
        }

        // Validar cupo disponible en la función
        int cuposDisponibles = funcionSeleccionada.getSala().contarAsientosDisponibles();
        if (cuposDisponibles <= 0) {
            throw new FuncionSinCupoException("La función seleccionada no tiene cupos disponibles.");
        }

        // Preguntar por cantidad de entradas
        System.out.println("¿Cuántas entradas desea?: ");
        int cantidad = sc.nextInt();
        sc.nextLine();

        if (cantidad > cuposDisponibles) {
            throw new FuncionSinCupoException("Solo quedan " + cuposDisponibles + " cupos disponibles para esta función.");
        }

        DetalleVenta detalleVenta = new DetalleVenta(cantidad);

        // Generar código para entrada sin fallar si la lista está vacía
        int codigoEntrada = (funcionSeleccionada.getListaEntradas() == null || funcionSeleccionada.getListaEntradas().isEmpty())
                ? 1
                : funcionSeleccionada.getListaEntradas().get(funcionSeleccionada.getListaEntradas().size() - 1).getCodigo() + 1;

        for (int i = 0; i < cantidad; i++) {
            System.out.println("\n--- ENTRADA " + (i + 1) + " de " + cantidad + " ---");

            System.out.println("Asientos disponibles:");
            for (Asiento asiento : funcionSeleccionada.getSala().getListaAsientos()) {
                if (asiento.getEstado() == EstadoAsiento.DISPONIBLE) {
                    System.out.print("[" + asiento.obtenerPuesto() + "]");
                }
            }
            System.out.print("\nIngrese el puesto del asiento que desea (Ej: A1, B5): ");
            String codigoAsiento = sc.nextLine();

            Asiento asientoSeleccionado = funcionSeleccionada.getSala().consultarAsientoPorPuesto(codigoAsiento);

            // Validar antes de ocupar
            if (asientoSeleccionado == null || asientoSeleccionado.getEstado() == EstadoAsiento.OCUPADO) {
                System.out.println("Error: El asiento no existe o ya se encuentra ocupado.");
                break;
            }
            asientoSeleccionado.setEstado(EstadoAsiento.OCUPADO);

            // Seleccionar tipo de cliente
            System.out.println("1. GENERAL, 2. ESTUDIANTE, 3. NIÑO, 4. ADULTO MAYOR");
            System.out.print("¿Qué tipo de entrada es?: ");
            int numeroTipoCliente = sc.nextInt();
            sc.nextLine();

            TipoEntrada tipoEntrada;
            if (numeroTipoCliente == 1) {
                tipoEntrada = TipoEntrada.GENERAL;
            } else if (numeroTipoCliente == 2) {
                tipoEntrada = TipoEntrada.ESTUDIANTE;
            } else if (numeroTipoCliente == 3) {
                tipoEntrada = TipoEntrada.NIÑO;
            } else if (numeroTipoCliente == 4) {
                tipoEntrada = TipoEntrada.ADULTO_MAYOR;
            } else {
                System.out.println("Elección no válida. Se asignará GENERAL por defecto.");
                tipoEntrada = TipoEntrada.GENERAL;
            }

            Entrada entrada = new Entrada(codigoEntrada++, funcionSeleccionada, asientoSeleccionado, tipoEntrada);
            boolean agregada = detalleVenta.agregarEntrada(entrada);

            if (agregada) {
                System.out.println("Entrada agregada con éxito.");
            } else {
                System.out.println("Error al agregar la entrada. No se contará esta entrada");
            }
        }

        // Calcular el valor a pagar y guardar la venta
        LocalDateTime fechaHoraVenta = LocalDateTime.now();
        Venta nuevaVenta = new Venta(codigoVenta, cliente, fechaHoraVenta, 0.0, EstadoVenta.ACTIVA, vendedor);
        nuevaVenta.getListaDetalles().add(detalleVenta);
        listaVentas.add(nuevaVenta);

        double valorTotal = calcularValorTotal(codigoVenta, cliente);
        nuevaVenta.setValorPagado(valorTotal);
    }
    // ==========================================
    // 5. CONSULTAS ESPECÍFICAS SOLICITADAS
    // ==========================================

    // Consultar películas disponibles
    public List<Pelicula> consultarPeliculasDisponibles() {
        List<Pelicula> disponibles = new ArrayList<>();
        for (Pelicula p : this.listaPeliculas) {
            if ("ACTIVO".equalsIgnoreCase(p.getEstado())) {
                disponibles.add(p);
            }
        }
        return disponibles;
    }

    // Consultar funciones programadas
    public List<Funcion> consultarFuncionesProgramadas() {
        return this.listaFunciones;
    }

    // Consultar las funciones programadas por fecha
    public List<Funcion> consultarFuncionesPorFecha(LocalDate fecha) {
        List<Funcion> funcionesFecha = new ArrayList<>();
        for (Funcion f : this.listaFunciones) {
            if (f.getFecha().equals(fecha)) {
                funcionesFecha.add(f);
            }
        }
        return funcionesFecha;
    }

    // Consultar disponibilidad de asientos de una función
    public int consultarDisponibilidadAsientos(int codigoFuncion) {
        Funcion f = consultarFuncion(codigoFuncion);
        if (f != null && f.getSala() != null) {
            return f.getSala().contarAsientosDisponibles();
        }
        return -1; // Función no encontrada
    }

    // Consultar la ocupación de una función específica
    public double consultarOcupacionFuncion(int codigoFuncion) {
        Funcion f = consultarFuncion(codigoFuncion);

        if (f == null) {
            throw new FuncionInexistenteException("La función no existe.");
        }

        if (f.getSala().contarAsientosDisponibles() <= 0) {
            throw new FuncionSinCupoException("La función no tiene cupos disponibles.");
        }

        if (f != null && f.getSala() != null && f.getSala().getCapacidad() > 0) {
            int capacidadTotal = f.getSala().getCapacidad();
            int disponibles = f.getSala().contarAsientosDisponibles();
            int vendidos = capacidadTotal - disponibles;
            return (double) vendidos / capacidadTotal * 100.0;
        }

        return 0.0;
    }


    // Identificar la película con mayor cantidad de entradas vendidas
    public Pelicula identificarPeliculaMasVendida() {
        if (listaPeliculas.isEmpty()) return null;

        Pelicula peliculaMasVendida = null;
        int maxEntradas = 0;

        for (Pelicula p : this.listaPeliculas) {
            int totalEntradasPelicula = 0;
            for (Funcion f : p.getListaFunciones()) {
                if (f.getSala() != null) {
                    int vendidos = f.getSala().getCapacidad() - f.getSala().contarAsientosDisponibles();
                    totalEntradasPelicula += vendidos;
                }
            }

            if (totalEntradasPelicula > maxEntradas) {
                maxEntradas = totalEntradasPelicula;
                peliculaMasVendida = p;
            }
        }
        return peliculaMasVendida;
    }

    // Consultar el total de entradas vendidas (solo ventas activas)
    public int consultarTotalEntradasVendidas() {
        int totalEntradas = 0;
        for (Venta venta : this.listaVentas) {
            if (venta != null && venta.getEstadoVenta() == EstadoVenta.ACTIVA) {
                totalEntradas++;
            }
        }
        return totalEntradas;
    }

    // Consultar el total de ingresos generados (solo ventas activas)
    public double consultarTotalIngresosGenerados() {
        double totalIngresos = 0.0;
        for (Venta venta : this.listaVentas) {
            if (venta != null && venta.getEstadoVenta() == EstadoVenta.ACTIVA) {
                totalIngresos += venta.getValorPagado();
            }
        }
        return totalIngresos;
    }

    // ==========================================
    // 6. GESTIÓN DE PERSONAS (CRUD)
    // ==========================================
    public boolean registrarPersona(Persona persona) {
        if (persona != null && consultarPersona(persona.getDocumento()) == null) {
            listaPersonas.add(persona);
            return true;
        }
        return false; // Ya existe una persona con ese documento o es nula
    }

    public Persona consultarPersona(int documento) {
        for (Persona persona : listaPersonas) {
            if (persona.getDocumento() == documento) {
                return persona;
            }
        }
        return null;
    }

    public boolean modificarPersona(int documento, Persona personaActualizada) {
        Persona persona = consultarPersona(documento);
        if (persona != null && personaActualizada != null) {
            // Actualizar nombre (común para todos)
            persona.setNombre(personaActualizada.getNombre());

            // Si es un Vendedor o ClienteVendedor, actualizar también el cargo
            if (persona instanceof Vendedor && personaActualizada instanceof Vendedor) {
                ((Vendedor) persona).setCargo(((Vendedor) personaActualizada).getCargo());
            }
            if (persona instanceof ClienteVendedor && personaActualizada instanceof ClienteVendedor) {
                ((ClienteVendedor) persona).setCargo(((ClienteVendedor) personaActualizada).getCargo());
            }

            return true;
        }
        return false;
    }

    public boolean eliminarPersona(int documento) {
        Persona persona = consultarPersona(documento);
        if (persona != null) {
            listaPersonas.remove(persona);
            return true;
        }
        return false; // Retorno necesario para evitar errores de compilación
    }
}

//------------------DOCUMENTACIÓN DE LOS MÉTODOS-------------------------

//1. GESTIÓN DE PELÍCULAS
// Permite administrar el catálogo de películas que ofrece el cine.

// registrarPelicula: Primero valida que no exista una película con el mismo código llamando a consultarPelicula(codigo). Si no existe, instancia un objeto Pelicula pasándole una lista de funciones vacía (new ArrayList<>()) por defecto, ya que al registrarse aún no tiene funciones asignadas.

// consultarPelicula: Recorre listaPeliculas buscando la coincidencia mediante el código único de la película.

//modificarPelicula: Busca la película y actualiza sus atributos de texto o numéricos (nombre, género, duración, estado).

// deshabilitarPelicula: Modifica el estado de la película a "INACTIVO" en lugar de borrarla físicamente, manteniendo la integridad referencial histórica.



// 2. GESTIÓN DE SALAS
// Administra la infraestructura física de las salas del cine basándose en la clase Sala.

// registrarSala: Recibe los parámetros necesarios para instanciar Sala (incluyendo su EstadoSala y ColeccionAsientos). Cabe destacar que al instanciar Sala, su constructor interno ejecuta automáticamente el metodo generarAsientos(), el cual crea la matriz de asientos (filas y columnas) basada en la capacidad.

//  modificarSala: Actualiza la capacidad, el estado o la colección de asientos. Si se cambia la capacidad, la propia clase Sala se encarga de recalcular y regenerar los asientos automáticamente.

//deshabilitarSala: Cambia el estado de la sala utilizando directamente la clase ENUM EstadoSala.INACTIVO.

// 4. PROGRAMACIÓN Y GESTIÓN DE FUNCIONES
// Vincula una película con una sala en un horario y fecha determinados, utilizando la estructura de la clase Funcion.

// programarFuncion: Crea una nueva Funcion exigiendo los objetos relacionales (Pelicula y Sala), la hora (LocalTime), la fecha (LocalDate), el valor base y su estado (EstadoFuncion).
// Relación bidireccional: Un detalle clave es la línea pelicula.getListaFunciones().add(nuevaFuncion);, la cual asegura que la función no solo quede registrada en el cine, sino también dentro del historial de funciones de esa película específica.

// 5. Consultas Específicas y Métodos de Negocio
// Estos métodos resuelven la lógica analítica y de consulta solicitada para el funcionamiento del cine:

// consultarPeliculasDisponibles: Filtra el listado general y retorna únicamente aquellas cuyo estado sea "ACTIVO".consultarFuncionesPorFecha: Compara la fecha ingresada por parámetro con el atributo LocalDate fecha de cada función registrada.

// consultarDisponibilidadAsientos: Consulta la función y delega el cálculo directamente al metodo nativo contarAsientosDisponibles() de la clase Sala, el cual recorre los asientos analizando cuáles se encuentran con EstadoAsiento.DISPONIBLE.

//  consultarOcupacionFuncion: Realiza un cálculo matemático basado en la capacidad total de la sala y los asientos disponibles para arrojar el porcentaje exacto de ocupación

//  identificarPeliculaMasVendida:Itera sobre cada película del cine.Recorre todas las funciones asociadas a esa película.Calcula los boletos vendidos por cada función restando la capacidad total menos los asientos disponibles de su respectiva sala.   Acumula el total de ventas por película y compara para determinar cuál acumula la mayor cantidad de entradas vendidas.((Verficiar si esta funcion va en esta clase))
