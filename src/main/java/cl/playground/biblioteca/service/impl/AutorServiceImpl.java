package cl.playground.biblioteca.service.impl;

import cl.playground.biblioteca.dto.CreateAutorConLibroDTO;
import cl.playground.biblioteca.dto.CreateLibroSinAutorDTO;
import cl.playground.biblioteca.dto.ListAutorDTO;
import cl.playground.biblioteca.model.Autor;
import cl.playground.biblioteca.model.Libro;
import cl.playground.biblioteca.repository.AutorRepository;
import cl.playground.biblioteca.repository.LibroRepository;
import cl.playground.biblioteca.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AutorServiceImpl implements AutorService {

    private final AutorRepository AutorRepository;
    private final LibroRepository libroRepository;

    @Autowired
    public AutorServiceImpl(AutorRepository AutorRepository, LibroRepository libroRepository) {
        this.AutorRepository = AutorRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional(readOnly = true)
    public Page<ListAutorDTO> obtenerAutoresPaginado(Integer page, Integer size) {
        if (page == null || page < 0) {
            throw new IllegalArgumentException("El número de página no puede ser nulo o negativo");
        }
        if (size == null || size <= 0) {
            throw new IllegalArgumentException("El tamaño de página debe ser mayor que cero");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Autor> autoresPage = AutorRepository.findAll(pageable);

        List<ListAutorDTO> listaDto = autoresPage.getContent()
            .stream()
            .map(autor -> {
                ListAutorDTO dto = new ListAutorDTO();
                dto.setIdAutor(autor.getIdAutor());
                dto.setNombre(autor.getNombre());
                dto.setApellido(autor.getApellido());
                return dto;
            })
            .toList();

        return new PageImpl<>(listaDto, pageable, autoresPage.getTotalElements());
    }

    @Override
    @Transactional
    public void CrearAutor(CreateAutorConLibroDTO createAutorConLibroDTO) {
        validarAutorNuevo(createAutorConLibroDTO);
        // Creamos nuevo autor
        Autor autor = new Autor();
        autor.setNombre(createAutorConLibroDTO.getNombre());
        autor.setApellido(createAutorConLibroDTO.getApellido());

        // Retornamos el autor guardado para obtener su ID y luego asociarlo a los libros
        Autor autorGuardado = AutorRepository.save(autor);

        if (createAutorConLibroDTO.getLibros() != null && !createAutorConLibroDTO.getLibros().isEmpty()) {
            validarLibroNuevo(createAutorConLibroDTO);

            // Asociamos cada libro al autor guardado...
            List<CreateLibroSinAutorDTO> librosDTO = createAutorConLibroDTO.getLibros();
            for (CreateLibroSinAutorDTO libro : librosDTO) {
                Libro nuevoLibro = new Libro();
                nuevoLibro.setTitulo(libro.getTitulo());
                nuevoLibro.setAnioPublicacion(libro.getAnioPublicacion());

                nuevoLibro.setIdAutor(autorGuardado);

                // Guardamos el nuevo libro en la base de datos
                libroRepository.save(nuevoLibro);
            }
        }
    }

    private void validarAutorNuevo(CreateAutorConLibroDTO createAutorConLibroDTO) {
        if (createAutorConLibroDTO.getNombre() == null || createAutorConLibroDTO.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del autor es obligatorio.");
        }
        if (createAutorConLibroDTO.getApellido() == null || createAutorConLibroDTO.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido del autor es obligatorio.");
        }
    }

    private void validarLibroNuevo(CreateAutorConLibroDTO createAutorConLibroDTO) {
        List<CreateLibroSinAutorDTO> libros = createAutorConLibroDTO.getLibros();

        for (CreateLibroSinAutorDTO libro : libros) {
            if (libro.getTitulo() == null || libro.getTitulo().isBlank()) {
                throw new IllegalArgumentException("El título del libro es obligatorio.");
            }
            if (libro.getAnioPublicacion() == null) {
                throw new IllegalArgumentException("El año de publicación del libro es obligatorio.");
            }
            if (libro.getAnioPublicacion() != null && libro.getAnioPublicacion() <= 0) {
                throw new IllegalArgumentException("El año de publicación no puede ser negativo.");
            }
        }
    }
}
