package uniquindio.edu.co.proyectofinal.model;

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
        this.listaPeliculas = new ArrayList<>();
        this.listaPersonas = new ArrayList<>();
        this.listaFunciones = new ArrayList<>();
        this.listaSalas = new ArrayList<>();
        this.listaVentas = new ArrayList<>();
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
        if (pelicula != null && consultarPelicula(pelicula.getCodigo()) == null) {
            listaPeliculas.add(pelicula);
            agregada = true;
        }
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
        if (sala != null && consultarSala(sala.getCodigo()) == null) {
            listaSalas.add(sala);
            agregada = true;
        }
        return agregada;
    }

    public Sala consultarSala(int numero) {
        Sala sala0= null;
        for (Sala sala : listaSalas) {
            if (sala.getCodigo() == codigo){
                sala0=sala;
            }
        }
        return sala0;
    }

    public boolean modificarSala(int numero, Sala salaActualizada) {
        boolean estaActualizada= false;
        Sala sala = consultarSala(codigo);
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

    // Calcula el valor total multiplicando la tarifa del TipoCliente por la cantidad de boletas y el valor del asiento
    public double calcularValorTotal(Cliente cliente, int cantidad, Funcion funcion) {
        double total = 0.0;
        if (cliente != null && cliente.getTipoCliente() != null && cantidad > 0) {

            // Tarifa base según el tipo de cliente + recargo del asiento seleccionado
            double tarifaUnitaria = cliente.getTipoCliente().getValorEntrada();
            double recargoAsiento = funcion.sala.getColeccionAsientos().getValorAsiento();

            double subtotalPorBoleta = tarifaUnitaria + recargoAsiento;
            total = subtotalPorBoleta * cantidad;

            // Si el cliente también es vendedor (ClienteVendedor), se aplica su descuento
            if (cliente instanceof IVendedor) {
                total -= ((IVendedor) cliente).calcularDescuento();
            }
        }
        return total;
    }

    public void cancelarVenta(Venta venta) {
        if (venta.getEstadoVenta() == EstadoVenta.ACTIVA) {
            EstadoVenta estadoVenta = EstadoVenta.CANCELADA;
            if (venta.getAsiento() != null) {
                venta.getAsiento().setEstado(EstadoAsiento.DISPONIBLE);
            }
        }
    }

    public void comenzarVenta(Cliente cliente, Vendedor vendedor) {
        Scanner sc = new Scanner(System.in);
        int opcion = 1;
        //Generar un código automatico
        int codigo= listaVentas.getLast().getCodigo()+1;
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
        System.out.println("\n Ingrese código de la función seleccionada: ");
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

        for (int i = 0; i < cantidad; i++) {
            System.out.println("\n--- ENTRADA " + (i + 1) + " de " + cantidad + " ---");

            // Mostrar asientos disponibles de la sala de esa función
            System.out.println("Asientos disponibles:");
            funcionSeleccionada.getSala().getListaAsientos().forEach(asiento -> {
                if (asiento.getEstado() == EstadoAsiento.DISPONIBLE) {
                    System.out.print("[" + asiento.obtenerPuesto() + "]");
                }
            });
            System.out.println();

            System.out.print("Ingrese el puesto del asiento que desea (Ej: A1, B5): ");
            String codigoAsiento = sc.nextLine();

            // Buscar el asiento en la función/sala
            Asiento asientoSeleccionado = funcionSeleccionada.getSala().consultarAsientoPorPuesto(codigoAsiento);
            if (asientoSeleccionado == null) {
                System.out.println("Error: El asiento no existe.");
                break;
            }

            // Validar si está disponible
            if (asientoSeleccionado.getEstado() != EstadoAsiento.DISPONIBLE) {
                System.out.println("Error: El asiento ya se encuentra ocupado.");
                break;
            }

            // 5. Seleccionar tipo de cliente
            System.out.println("1. GENERAL, 2. ESTUDIANTE, 3. NIÑO, 4. ADULTO MAYOR");
            System.out.print("¿Qué tipo de entrada es?: ");
            int numeroTipoCliente = sc.nextInt();

            TipoCliente tipoClienteEntrada;
            if (numeroTipoCliente == 1) {
                tipoClienteEntrada = TipoCliente.GENERAL;
            } else if (numeroTipoCliente == 2) {
                tipoClienteEntrada = TipoCliente.ESTUDIANTE;
            } else if (numeroTipoCliente == 3) {
                tipoClienteEntrada = TipoCliente.NINIO;
            } else if (numeroTipoCliente == 4) {
                tipoClienteEntrada = TipoCliente.ADULTO_MAYOR;
            } else {
                System.out.println("Elección no válida. Se asignará GENERAL por defecto.");
                tipoClienteEntrada = TipoCliente.GENERAL;
            }

            // 6. Calcular el valor a pagar
            double valorTotal= calcularValorTotal(cliente, cantidad, funcionSeleccionada);

            // 7. Crear la venta y registrarla
            LocalDateTime fechaHoraVenta = LocalDateTime.now();

            Venta nuevaVenta = new Venta(codigo, cliente, funcionSeleccionada, asientoSeleccionado,fechaHoraVenta, EstadoVenta.ACTIVA, vendedor, cantidad);

            // Cambiar estado del asiento a ocupado
            asientoSeleccionado.setEstado(EstadoAsiento.OCUPADO);

            // Guardar la venta en la lista general del cine/sistema
            listaVentas.add(nuevaVenta);

            System.out.println("¡Entrada agregada y venta registrada con éxito! Código: " + codigo);
        }

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
