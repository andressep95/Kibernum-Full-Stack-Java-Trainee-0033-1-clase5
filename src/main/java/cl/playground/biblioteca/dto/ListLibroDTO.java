package cl.playground.biblioteca.dto;

public class ListLibroDTO {

    private Integer idLibro;
    private String titulo;
    private String anioPublicacion;
    private String autor;

    public ListLibroDTO() {}

    public ListLibroDTO(Integer idLibro, String titulo, String anioPublicacion, String autor) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.anioPublicacion = anioPublicacion;
        this.autor = autor;
    }

    public Integer getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Integer idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(String anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "ListLibroDTO{" +
            "idLibro=" + idLibro +
            ", titulo='" + titulo + '\'' +
            ", anioPublicacion='" + anioPublicacion + '\'' +
            ", autor='" + autor + '\'' +
            '}';
    }
}
