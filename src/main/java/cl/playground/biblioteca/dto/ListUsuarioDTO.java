package cl.playground.biblioteca.dto;

public class ListUsuarioDTO {

    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;

    public ListUsuarioDTO() {}

    public ListUsuarioDTO(Integer idUsuario, String nombre, String apellido, String correo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
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
        return "ListUsuarioDTO{" +
            "idUsuario=" + idUsuario +
            ", nombre='" + nombre + '\'' +
            ", apellido='" + apellido + '\'' +
            ", correo='" + correo + '\'' +
            '}';
    }
}
