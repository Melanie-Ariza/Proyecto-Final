package uniquindio.edu.co.proyectofinal.model;

import java.util.ArrayList;
import java.util.List;

public class Pelicula {
    private int codigo;
    private String nombre;
    private String genero;
    private int duracion;
    private String estado;
    private List<Funcion> listaFunciones;
    private Clasificacion clasificacion;

    public Pelicula(int codigo, String nombre, String genero, int duracion, String estado, List<Funcion> listaFunciones, Clasificacion clasificacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.genero = genero;
        this.duracion = duracion;
        this.estado = estado;
        this.listaFunciones = new ArrayList<>();
        this.clasificacion = clasificacion;
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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Funcion> getListaFunciones() {
        return listaFunciones;
    }

    public void setListaFunciones(List<Funcion> listaFunciones) {
        this.listaFunciones = listaFunciones;
    }

    @Override
    public String toString() {
        return "Pelicula: " +
                "codigo: " + codigo +
                ", nombre: " + nombre +
                ", genero: " + genero +
                ", duracion: " + duracion +
                ", estado: " + estado +
                ", listaFunciones: " + listaFunciones;
    }
   //Gestionar Peliculas
    public class GestorPeliculas {
        private List<Pelicula> listaPeliculas = new ArrayList<>();

        // 1. Registrar Película
        public boolean registrarPelicula(int codigo, String nombre, String genero, int duracion, String estado, Clasificacion clasificacion) {
            // Validar si ya existe una película con el mismo código
            if (consultarPelicula(codigo) != null) {
                return false; // Ya existe
            }
            Pelicula nuevaPelicula = new Pelicula(codigo, nombre, genero, duracion, estado, new ArrayList<>(), clasificacion);
            listaPeliculas.add(nuevaPelicula);
            return true;
        }

        // 2. Consultar Película por código
        public Pelicula consultarPelicula(int codigo) {
            for (Pelicula pelicula : listaPeliculas) {
                if (pelicula.getCodigo() == codigo) {
                    return pelicula;
                }
            }
            return null; // No encontrada
        }

        // 3. Modificar Película
        public boolean modificarPelicula(int codigo, String nuevoNombre, String nuevoGenero, int nuevaDuracion, String nuevoEstado, Clasificacion nuevaClasificacion) {
            Pelicula pelicula = consultarPelicula(codigo);
            if (pelicula != null) {
                pelicula.setNombre(nuevoNombre);
                pelicula.setGenero(nuevoGenero);
                pelicula.setDuracion(nuevaDuracion);
                pelicula.setEstado(nuevoEstado);
                // Si en tu modelo Pelicula tienes el setter para clasificacion, úsalo aquí:
                // pelicula.setClasificacion(nuevaClasificacion);
                return true;
            }
            return false; // No encontrada para modificar
        }

        // 4. Deshabilitar Película (cambiar estado a "INACTIVO" o "DESHABILITADO")
        public boolean deshabilitarPelicula(int codigo) {
            Pelicula pelicula = consultarPelicula(codigo);
            if (pelicula != null) {
                pelicula.setEstado("INACTIVO"); // O el valor que manejes para el estado
                return true;
            }
            return false; // No encontrada
        }
    }
      }
