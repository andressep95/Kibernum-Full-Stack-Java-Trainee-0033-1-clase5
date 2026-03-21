package cl.playground.biblioteca.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro")
    private Integer idLibro;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "anio_publicacion")
    private Short anioPublicacion;

    @ManyToOne
    @JoinColumn(
        name = "id_autor",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_libros_id_autor")
    )
    private Autor idAutor;

    @OneToMany(
        mappedBy = "idLibro",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<Prestamo> prestamos = new HashSet<>();

    public Libro() {}

    public Libro(Integer idLibro, String titulo, Short anioPublicacion, Autor idAutor) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.anioPublicacion = anioPublicacion;
        this.idAutor = idAutor;
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

    public Short getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(Short anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public Autor getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(Autor idAutor) {
        this.idAutor = idAutor;
    }

    public Set<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(Set<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

    @Override
    public String toString() {
        return "Libro{" +
            "idLibro=" + idLibro +
            ", titulo='" + titulo + '\'' +
            ", anioPublicacion=" + anioPublicacion +
            ", idAutor=" + idAutor +
            ", prestamos=" + prestamos +
            '}';
    }
}