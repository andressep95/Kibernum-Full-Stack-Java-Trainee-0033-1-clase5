package cl.playground.biblioteca.dto;

import java.util.ArrayList;
import java.util.List;

public class CreateAutorConLibroDTO {

    private String nombre;
    private String apellido;
    private List<CreateLibroSinAutorDTO> libros = new ArrayList<>();

    public CreateAutorConLibroDTO() {
    }

    public CreateAutorConLibroDTO(String nombre, String apellido, List<CreateLibroSinAutorDTO> libros) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.libros = libros;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public List<CreateLibroSinAutorDTO> getLibros() {
        return libros;
    }

    public void setLibros(List<CreateLibroSinAutorDTO> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return "CreateAutorConLibroDTO{" +
            "nombre='" + nombre + '\'' +
            ", apellido='" + apellido + '\'' +
            ", libros=" + libros +
            '}';
    }
}