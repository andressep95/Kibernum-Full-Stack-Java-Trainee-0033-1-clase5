package cl.playground.biblioteca.service;

import cl.playground.biblioteca.dto.ListAutorDTO;

import java.util.List;

public interface AutorService {

    // SOLO PARA DESPLEGABLE DE LIBROS, NO PARA CREAR AUTOR
    List<ListAutorDTO> obtenerAutores();

}
