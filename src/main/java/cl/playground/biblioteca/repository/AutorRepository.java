package cl.playground.biblioteca.repository;

import cl.playground.biblioteca.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Integer> {

    Autor findByNombreAndApellido(String nombre, String apellido);
    boolean existsByNombreAndApellido(String nombre, String apellido);
}
