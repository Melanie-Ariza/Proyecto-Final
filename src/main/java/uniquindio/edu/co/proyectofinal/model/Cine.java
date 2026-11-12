package uniquindio.edu.co.proyectofinal.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    public boolean registrarPelicula(int codigo, String nombre, String genero, int duracion, String estado, Clasificacion clasificacion) {
        if (consultarPelicula(codigo) != null) return false;
        Pelicula nuevaPelicula = new Pelicula(codigo, nombre, genero, duracion, estado, new ArrayList<>(), clasificacion);
        this.listaPeliculas.add(nuevaPelicula);
        return true;
    }

    public Pelicula consultarPelicula(int codigo) {
        for (Pelicula p : this.listaPeliculas) {
            if (p.getCodigo() == codigo) return p;
        }
        return null;
    }

    public boolean modificarPelicula(int codigo, String nuevoNombre, String nuevoGenero, int nuevaDuracion, String nuevoEstado) {
        Pelicula p = consultarPelicula(codigo);
        if (p != null) {
            p.setNombre(nuevoNombre);
            p.setGenero(nuevoGenero);
            p.setDuracion(nuevaDuracion);
            p.setEstado(nuevoEstado);
            return true;
        }
        return false;
    }

    public boolean deshabilitarPelicula(int codigo) {
        Pelicula p = consultarPelicula(codigo);
        if (p != null) {
            p.setEstado("INACTIVO");
            return true;
        }
        return false;
    }

    // ==========================================
    // 2. GESTIÓN DE SALAS (CRUD)
    // ==========================================

    public boolean registrarSala(int codigo, int numero, int capacidad, EstadoSala estado, ColeccionAsientos coleccionAsientos) {
        if (consultarSala(numero) != null) return false;
        Sala nuevaSala = new Sala(codigo, numero, capacidad, estado, coleccionAsientos);
        this.listaSalas.add(nuevaSala);
        return true;
    }

    public Sala consultarSala(int numeroSala) {
        for (Sala s : this.listaSalas) {
            if (s.getNumero() == numeroSala) return s;
        }
        return null;
    }

    public boolean modificarSala(int numeroSala, int nuevaCapacidad, EstadoSala nuevoEstado, ColeccionAsientos nuevaColeccionAsientos) {
        Sala s = consultarSala(numeroSala);
        if (s != null) {
            s.setCapacidad(nuevaCapacidad); // Esto regenera automáticamente los asientos gracias al método de la clase Sala
            s.setEstado(nuevoEstado);
            s.setColeccionAsientos(nuevaColeccionAsientos);
            return true;
        }
        return false;
    }

    public boolean deshabilitarSala(int numeroSala) {
        Sala s = consultarSala(numeroSala);
        if (s != null) {
            s.setEstado(EstadoSala.FUERA_DE_SERVICIO); // Usando el enum correspondiente
            return true;
        }
        return false;
    }

    // ==========================================
    // 3. PROGRAMAR Y GESTIONAR FUNCIONES
    // ==========================================

    public boolean programarFuncion(int codigo, Pelicula pelicula, Sala sala, LocalTime hora, LocalDate fecha, double valorBase, EstadoFuncion estadoFuncion) {
        if (consultarFuncion(codigo) != null || pelicula == null || sala == null) return false;

        Funcion nuevaFuncion = new Funcion(codigo, pelicula, sala, hora, fecha, valorBase, estadoFuncion, new ArrayList<>());
        this.listaFunciones.add(nuevaFuncion);
        pelicula.getListaFunciones().add(nuevaFuncion); // Añade la función a la lista interna de la película
        return true;
    }

    public Funcion consultarFuncion(int codigoFuncion) {
        for (Funcion f : this.listaFunciones) {
            if (f.getCodigo() == codigoFuncion) return f;
        }
        return null;
    }

    // ==========================================
    // 4. CONSULTAS ESPECÍFICAS SOLICITADAS
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

    // Consultar disponibilidad de asientos de una función (aprovechando el metodo nativo de la clase Sala)
    public int consultarDisponibilidadAsientos(int codigoFuncion) {
        Funcion f = consultarFuncion(codigoFuncion);
        if (f != null && f.getSala() != null) {
            return f.getSala().contarAsientosDisponibles();
        }
        return -1; // Función no encontrada
    }

    // Consultar la ocupación de una función específica (Porcentaje)
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
