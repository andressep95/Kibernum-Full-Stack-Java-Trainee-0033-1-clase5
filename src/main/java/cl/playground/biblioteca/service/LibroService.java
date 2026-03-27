package cl.playground.biblioteca.service;

import cl.playground.biblioteca.dto.CreateLibroDTO;
import cl.playground.biblioteca.dto.ListLibroDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LibroService {

    void crearLibro(CreateLibroDTO createLibroDTO);

    Page<ListLibroDTO> obtenerLibros(Integer page, Integer size);

    List<ListLibroDTO> obtenerTodosLosLibros();
}
