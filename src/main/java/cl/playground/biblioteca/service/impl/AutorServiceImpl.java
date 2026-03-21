package cl.playground.biblioteca.service.impl;

import cl.playground.biblioteca.dto.ListAutorDTO;
import cl.playground.biblioteca.model.Autor;
import cl.playground.biblioteca.repository.AutorRepository;
import cl.playground.biblioteca.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorServiceImpl implements AutorService {

    private final AutorRepository AutorRepository;

    @Autowired
    public AutorServiceImpl(AutorRepository AutorRepository) {
        this.AutorRepository = AutorRepository;
    }

    @Override
    public List<ListAutorDTO> obtenerAutores() {
        List<Autor> autores = AutorRepository.findAll();

        List<ListAutorDTO> autoresDTO = autores.stream()
                .map(autor -> {
                    ListAutorDTO dto = new ListAutorDTO();
                    dto.setIdAutor(autor.getIdAutor());
                    dto.setNombre(autor.getNombre());
                    dto.setApellido(autor.getApellido());
                    return dto;
                })
            .toList();


        return autoresDTO;
    }
}
