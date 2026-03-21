package cl.playground.biblioteca.dto;

public class CreateLibroDTO {

    private String titulo;
    private Short anioPublicacion;
    private CreateAutorDTO autor;

    public CreateLibroDTO() {}

    public CreateLibroDTO(String titulo, Short anioPublicacion, CreateAutorDTO autor) {
        this.titulo = titulo;
        this.anioPublicacion = anioPublicacion;
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Short getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(Short anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public CreateAutorDTO getAutor() {
        return autor;
    }

    public void setAutor(CreateAutorDTO autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "CreateLibroDTO{" +
            "titulo='" + titulo + '\'' +
            ", anioPublicacion=" + anioPublicacion +
            ", autor=" + autor +
            '}';
    }
}
