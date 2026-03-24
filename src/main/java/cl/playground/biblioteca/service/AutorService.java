package cl.playground.biblioteca.service;

import cl.playground.biblioteca.dto.CreateAutorConLibroDTO;
import cl.playground.biblioteca.dto.ListAutorDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AutorService {

    List<ListAutorDTO> obtenerAutores();
    Page<ListAutorDTO> obtenerAutoresPaginado(Integer page, Integer size);
    void CrearAutor(CreateAutorConLibroDTO createAutorConLibroDTO);

}
