package uniquindio.edu.co.proyectofinal.model;

import uniquindio.edu.co.proyectofinal.Excepciones.FuncionInexistenteException.FuncionInexistenteException;
import uniquindio.edu.co.proyectofinal.Excepciones.FuncionSinCupoException.FuncionSinCupoException;
import uniquindio.edu.co.proyectofinal.Excepciones.PeliculaDuplicadaException.PeliculaDuplicadaException;
import uniquindio.edu.co.proyectofinal.Excepciones.SalaDuplicadaException.SalaDuplicadaException;

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

    public Cine(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        listaPeliculas = new ArrayList<>();
        listaPersonas = new ArrayList<>();
        listaFunciones = new ArrayList<>();
        listaSalas = new ArrayList<>();
        listaVentas = new ArrayList<>();
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

    public Sala consultarSala(int numero) {
        Sala sala0= null;
        for (Sala sala : listaSalas) {
            if (sala.getCodigo() == numero){
                sala0=sala;
            }
        }
        return sala0;
    }

    public boolean modificarSala(int numero, Sala salaActualizada) {
        boolean estaActualizada= false;
        Sala sala = consultarSala(numero);
        if (sala != null) {
            sala.setCapacidad(salaActualizada.getCapacidad());
            sala.setEstado(salaActualizada.getEstado());
            sala.setColeccionAsientos(salaActualizada.getColeccionAsientos());
            estaActualizada= true;
        }
        return estaActualizada;
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
        boolean programada = false;
        if (pelicula != null && nuevaFuncion != null && consultarFuncion(nuevaFuncion.getCodigo()) == null) {
            if (nuevaFuncion.getSala() != null && nuevaFuncion.getSala().getEstado() == EstadoSala.DISPONIBLE) {
                listaFunciones.add(nuevaFuncion);
                pelicula.getListaFunciones().add(nuevaFuncion);
                programada = true;
            }
        }
        return programada;
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

    // Calcula el valor total
    public double calcularValorTotal(int codigo, Persona cliente) {
        double total = 0;
        double subtotal= 0;

        for (Venta venta: listaVentas){
            if (venta.getCodigo()==codigo){
                for (DetalleVenta detalle: venta.getListaDetalles()){
                    subtotal= detalle.calcularSubtotal();
                    total+=subtotal;
                }
            }
        }

        if (cliente instanceof ClienteVendedor) {
            total= total * ((ClienteVendedor) cliente).calcularDescuento();
        }
        return total;
    }

    public void cancelarVenta(Venta venta) {
        if (venta.getEstadoVenta() == EstadoVenta.ACTIVA) {
            EstadoVenta estadoVenta = EstadoVenta.CANCELADA;
            for (DetalleVenta detalle: venta.getListaDetalles()){
                for (Entrada entrada: detalle.getListaEntradas()){
                    if (entrada.getAsiento() != null) {
                        entrada.getAsiento().setEstado(EstadoAsiento.DISPONIBLE);
                    }
                }
            }

        }
    }

    public void comenzarVenta(Cliente cliente, Vendedor vendedor) {
        Scanner sc = new Scanner(System.in);
        int opcion = 1;
        //Generar un código automatico
        int codigoVenta= listaVentas.getLast().getCodigo()+1;
        if (listaVentas== null || listaVentas.isEmpty()){
            codigo= 1;
        }

        //Mostrar funciones disponibles
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

        //Seleccionar una funcion
        System.out.println("\n Ingrese código de la función deseada: ");
        int codigoFuncion= sc.nextInt();

        Funcion funcionSeleccionada = consultarFuncion(codigoFuncion);
        if (funcionSeleccionada == null) {
            System.out.println("Error: La función ingresada no existe.");
            return;
        }

        //Preguntar por cantidad de entradas
        System.out.println("¿Cuántas entradas desea?: ");
        int cantidad= sc.nextInt();
        sc.nextLine();

        DetalleVenta detalleVenta= new DetalleVenta(cantidad);

        //Generar codigo para entrada
        int codigoEntrada= funcionSeleccionada.getListaEntradas().getLast().getCodigo()+1;
        if (funcionSeleccionada.getListaEntradas()== null || funcionSeleccionada.getListaEntradas().isEmpty()){
            codigo= 1;
        }

        for (int i = 0; i < cantidad; i++) {
            System.out.println("\n--- ENTRADA " + (i + 1) + " de " + cantidad + " ---");

            // Mostrar asientos disponibles de la sala de esa función
            System.out.println("Asientos disponibles:");
            for (Asiento asiento: funcionSeleccionada.getSala().getListaAsientos()){
                if (asiento.getEstado() == EstadoAsiento.DISPONIBLE) {
                    System.out.print("[" + asiento.obtenerPuesto() + "]");
                }
            }
            System.out.print("Ingrese el puesto del asiento que desea (Ej: A1, B5): ");
            String codigoAsiento = sc.nextLine();

            Asiento asientoSeleccionado = funcionSeleccionada.getSala().consultarAsientoPorPuesto(codigoAsiento);
            asientoSeleccionado.setEstado(EstadoAsiento.OCUPADO);

            // Validar si está disponible
            if (asientoSeleccionado.getEstado() == EstadoAsiento.OCUPADO) {
                System.out.println("Error: El asiento ya se encuentra ocupado.");
                break;
            }

            // 5. Seleccionar tipo de cliente
            System.out.println("1. GENERAL, 2. ESTUDIANTE, 3. NIÑO, 4. ADULTO MAYOR");
            System.out.print("¿Qué tipo de entrada es?: ");
            int numeroTipoCliente = sc.nextInt();

            TipoEntrada tipoEntrada=null;
            if (numeroTipoCliente == 1) {
                tipoEntrada = TipoEntrada.GENERAL;
            } else if (numeroTipoCliente == 2) {
                tipoEntrada = TipoEntrada.ESTUDIANTE;
            } else if (numeroTipoCliente  == 3) {
                tipoEntrada = TipoEntrada.NIÑO;
            } else if (numeroTipoCliente == 4) {
                tipoEntrada = TipoEntrada.ADULTO_MAYOR;
            } else {
                System.out.println("Elección no válida. Se asignará GENERAL por defecto.");
                tipoEntrada = TipoEntrada.GENERAL;
            }
            Entrada entrada= new Entrada(codigoEntrada, funcionSeleccionada, asientoSeleccionado, tipoEntrada);
            boolean agregada= detalleVenta.agregarEntrada(entrada);

            if (agregada) {
                System.out.println("Entrada agregada con éxito.");
            }else {
                System.out.println("Error al agregar la entrada. No se contará esta entrada");
            }
        }

        // 6. Calcular el valor a pagar
        double valorTotal= calcularValorTotal(codigoVenta, cliente);

        // 7. Crear la venta y registrarla
        LocalDateTime fechaHoraVenta = LocalDateTime.now();

        Venta nuevaVenta = new Venta(codigoVenta, cliente, fechaHoraVenta, valorTotal, EstadoVenta.ACTIVA, vendedor);

        // Guardar la venta
        listaVentas.add(nuevaVenta);
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
        int maxEntradas = -1;

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
