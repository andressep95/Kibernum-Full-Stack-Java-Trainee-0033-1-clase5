package cl.playground.biblioteca.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Integer idPrestamo;

    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate fechaPrestamo;

    @ManyToOne
    @JoinColumn(
        name = "id_libro",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_prestamos_id_libro")
    )
    private Libro idLibro;

    @ManyToOne
    @JoinColumn(
        name = "id_usuario",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_prestamos_id_usuario")
    )
    private Usuario idUsuario;

    public Prestamo() {}

    public Prestamo(Integer idPrestamo, LocalDate fechaPrestamo, Libro idLibro, Usuario idUsuario) {
        this.idPrestamo = idPrestamo;
        this.fechaPrestamo = fechaPrestamo;
        this.idLibro = idLibro;
        this.idUsuario = idUsuario;
    }

    public Integer getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(Integer idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Libro getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Libro idLibro) {
        this.idLibro = idLibro;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
            "idPrestamo=" + idPrestamo +
            ", fechaPrestamo=" + fechaPrestamo +
            ", idLibro=" + idLibro +
            ", idUsuario=" + idUsuario +
            '}';
    }
}