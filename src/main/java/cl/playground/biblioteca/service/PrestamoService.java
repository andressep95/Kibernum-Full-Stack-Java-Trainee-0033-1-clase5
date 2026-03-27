package cl.playground.biblioteca.service;

import cl.playground.biblioteca.dto.CreatePrestamoDTO;
import cl.playground.biblioteca.dto.ListPrestamosDTO;
import org.springframework.data.domain.Page;

public interface PrestamoService {

    void crearPrestamo(CreatePrestamoDTO createPrestamoDTO);
    Page<ListPrestamosDTO> listarPrestamos(Integer page, Integer size);

}
