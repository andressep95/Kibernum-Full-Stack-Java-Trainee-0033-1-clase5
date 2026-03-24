package cl.playground.biblioteca.dto;

public class CreateLibroSinAutorDTO {

    private String titulo;
    private Short anioPublicacion;

    public CreateLibroSinAutorDTO() {
    }

    public CreateLibroSinAutorDTO(String titulo, Short anioPublicacion) {
        this.titulo = titulo;
        this.anioPublicacion = anioPublicacion;
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

    @Override
    public String toString() {
        return "CrearLibroSinAutorDTO{" +
            "titulo='" + titulo + '\'' +
            ", anioPublicacion=" + anioPublicacion +
            '}';
    }
}
