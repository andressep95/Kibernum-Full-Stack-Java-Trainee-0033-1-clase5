package cl.playground.biblioteca.dto;

public class ListPrestamosDTO {

    private Integer idPrestamo;
    private String nombreUsuario;
    private String tituloLibro;
    private String fechaPrestamo;

    public ListPrestamosDTO() {
    }

    public ListPrestamosDTO(Integer idPrestamo, String nombreUsuario, String tituloLibro, String fechaPrestamo) {
        this.idPrestamo = idPrestamo;
        this.nombreUsuario = nombreUsuario;
        this.tituloLibro = tituloLibro;
        this.fechaPrestamo = fechaPrestamo;
    }

    public Integer getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(Integer idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public String getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(String fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    @Override
    public String toString() {
        return "ListPrestamosDTO{" +
            "idPrestamo=" + idPrestamo +
            ", nombreUsuario='" + nombreUsuario + '\'' +
            ", tituloLibro='" + tituloLibro + '\'' +
            ", fechaPrestamo='" + fechaPrestamo + '\'' +
            '}';
    }
}
