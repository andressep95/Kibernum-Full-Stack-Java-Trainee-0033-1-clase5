package cl.playground.biblioteca.dto;

import java.time.LocalDate;

public class CreatePrestamoDTO {

    private Integer idLibro;

    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;

    public CreatePrestamoDTO() {
    }

    public CreatePrestamoDTO(Integer idLibro, Integer idUsuario, String nombre, String apellido, String correo) {
        this.idLibro = idLibro;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
    }

    public Integer getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Integer idLibro) {
        this.idLibro = idLibro;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return "CreatePrestamo{" +
            ", idLibro=" + idLibro +
            ", idUsuario=" + idUsuario +
            ", nombre='" + nombre + '\'' +
            ", apellido='" + apellido + '\'' +
            ", correo='" + correo + '\'' +
            '}';
    }
}
