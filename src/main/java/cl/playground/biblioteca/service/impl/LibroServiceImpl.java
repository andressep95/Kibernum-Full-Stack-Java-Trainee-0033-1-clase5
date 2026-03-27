package cl.playground.biblioteca.service.impl;

import cl.playground.biblioteca.dto.CreateAutorDTO;
import cl.playground.biblioteca.dto.CreateLibroDTO;
import cl.playground.biblioteca.dto.ListLibroDTO;
import cl.playground.biblioteca.model.Autor;
import cl.playground.biblioteca.model.Libro;
import cl.playground.biblioteca.repository.AutorRepository;
import cl.playground.biblioteca.repository.LibroRepository;
import cl.playground.biblioteca.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    @Autowired
    public LibroServiceImpl(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    /*
    1. Autor usado anteriormente
    2. Autor nuevo
    3. Autor desconocido
    */

    @Override
    @Transactional
    public void crearLibro(CreateLibroDTO createLibroDTO) {
        // Validamos datos propios del libro
        validarCrearLibro(createLibroDTO);

        Autor autor;
        if (autorRepository.existsByNombreAndApellido(createLibroDTO.getAutor().getNombre(),createLibroDTO.getAutor().getApellido())) {
            autor = autorRepository.findByNombreAndApellido(createLibroDTO.getAutor().getNombre(),createLibroDTO.getAutor().getApellido());
        } else {
            autor = new Autor();
            autor.setNombre(createLibroDTO.getAutor().getNombre());
            autor.setApellido(createLibroDTO.getAutor().getApellido());
            autorRepository.save(autor);
        }

        Libro libro = new Libro();
        libro.setTitulo(createLibroDTO.getTitulo());
        libro.setAnioPublicacion(createLibroDTO.getAnioPublicacion());
        libro.setIdAutor(autor);

        libroRepository.save(libro);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ListLibroDTO> obtenerLibros(Integer page, Integer size) {
        if (page == null || page < 0) {
            throw new IllegalArgumentException("El número de página no puede ser nulo o negativo");
        }
        if (size == null || size <= 0) {
            throw new IllegalArgumentException("El tamaño de página debe ser mayor que cero");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Libro> librosPage = libroRepository.findAll(pageable);

        List<ListLibroDTO> listaDto = librosPage.getContent()
            .stream()
            .map(libro -> {
                ListLibroDTO dto = new ListLibroDTO();
                dto.setIdLibro(libro.getIdLibro());
                dto.setTitulo(libro.getTitulo());
                dto.setAnioPublicacion(String.valueOf(libro.getAnioPublicacion()));
                dto.setAutor(libro.getIdAutor().getNombre() + " " + libro.getIdAutor().getApellido());
                return dto;
            })
            .toList();

        return new PageImpl<>(listaDto, pageable, librosPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListLibroDTO> obtenerTodosLosLibros() {
        return libroRepository.findAll().stream()
            .map(libro -> {
                ListLibroDTO dto = new ListLibroDTO();
                dto.setIdLibro(libro.getIdLibro());
                dto.setTitulo(libro.getTitulo());
                dto.setAnioPublicacion(String.valueOf(libro.getAnioPublicacion()));
                dto.setAutor(libro.getIdAutor().getNombre() + " " + libro.getIdAutor().getApellido());
                return dto;
            })
            .toList();
    }

    private void validarCrearLibro(CreateLibroDTO createLibroDTO) {
        if (createLibroDTO.getTitulo() == null || createLibroDTO.getTitulo().isEmpty()) {
            throw new IllegalArgumentException("El título del libro no puede ser nulo o vacío");
        }
        if (createLibroDTO.getAnioPublicacion() == null) {
            throw new IllegalArgumentException("El año de publicación no puede ser nulo");
        }
    }

}
