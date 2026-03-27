package cl.playground.biblioteca.service.impl;


import cl.playground.biblioteca.dto.CreatePrestamoDTO;
import cl.playground.biblioteca.dto.ListPrestamosDTO;
import cl.playground.biblioteca.model.Libro;
import cl.playground.biblioteca.model.Prestamo;
import cl.playground.biblioteca.model.Usuario;
import cl.playground.biblioteca.repository.LibroRepository;
import cl.playground.biblioteca.repository.PrestamoRepository;
import cl.playground.biblioteca.repository.UsuarioRepository;
import cl.playground.biblioteca.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public PrestamoServiceImpl(LibroRepository libroRepository, PrestamoRepository prestamoRepository, UsuarioRepository usuarioRepository) {
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public void crearPrestamo(CreatePrestamoDTO createPrestamoDTO) {
        // Validar los datos del préstamo
        validarPrestamo(createPrestamoDTO);

        // Obtenemos el libro a traves del ID que trae el DTO
        Libro libroOptional = libroRepository.findById(createPrestamoDTO.getIdLibro())
            .orElseThrow(
                () -> new IllegalArgumentException("El libro con ID " + createPrestamoDTO.getIdLibro() + " no existe")
                        );

        // Obtenemos el usuario a traves del ID que trae el DTO o lo creamos si no existe
        Usuario usuario = crearUsuarioPrestamo(createPrestamoDTO);

        // Creamos el préstamo
        Prestamo prestamo = new Prestamo();
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setIdLibro(libroOptional);
        prestamo.setIdUsuario(usuario);

        // Guardamos el préstamo en la base de datos
        prestamoRepository.save(prestamo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ListPrestamosDTO> listarPrestamos(Integer page, Integer size) {
        if (page == null || page < 0) {
            throw new IllegalArgumentException("El número de página no puede ser nulo o negativo");
        }
        if (size == null || size <= 0) {
            throw new IllegalArgumentException("El tamaño de página debe ser mayor que cero");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Prestamo> prestamosPage = prestamoRepository.findAll(pageable);

        List<ListPrestamosDTO> prestamosDTO = prestamosPage.getContent().stream()
            .map(prestamo -> {
                ListPrestamosDTO dto = new ListPrestamosDTO();
                dto.setIdPrestamo(prestamo.getIdPrestamo());
                dto.setFechaPrestamo(String.valueOf(prestamo.getFechaPrestamo()));
                dto.setTituloLibro(prestamo.getIdLibro().getTitulo());
                dto.setNombreUsuario(prestamo.getIdUsuario().getNombre() + " " + prestamo.getIdUsuario().getApellido());
                return dto;
            })
            .toList();

        return new PageImpl<>(prestamosDTO, pageable, prestamosPage.getTotalElements());
    }

    private Usuario crearUsuarioPrestamo(CreatePrestamoDTO createPrestamoDTO) {
        // Si el DTO trae un ID de usuario, intentamos obtenerlo. Si no existe, lanzamos una excepción
        if (createPrestamoDTO.getIdUsuario() != null) {
            return usuarioRepository.findById(createPrestamoDTO.getIdUsuario())
                .orElseThrow(
                    () -> new IllegalArgumentException("El usuario con ID " + createPrestamoDTO.getIdUsuario() + " no existe")
                            );
        }

        // Si el DTO no trae un ID de usuario, validamos que el correo no esté registrado y creamos un nuevo usuario
        if (usuarioRepository.existsByCorreo(createPrestamoDTO.getCorreo())) {
            throw new IllegalArgumentException("El correo " + createPrestamoDTO.getCorreo() + " ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(createPrestamoDTO.getNombre());
        usuario.setApellido(createPrestamoDTO.getApellido());
        usuario.setCorreo(createPrestamoDTO.getCorreo());

        return usuarioRepository.save(usuario);
    }

    private void validarPrestamo(CreatePrestamoDTO createPrestamoDTO) {
        if (createPrestamoDTO.getIdLibro() == null) {
            throw new IllegalArgumentException("El ID del libro es obligatorio");
        }
        if (createPrestamoDTO.getNombre() == null || createPrestamoDTO.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio");
        }
        if (createPrestamoDTO.getApellido() == null || createPrestamoDTO.getApellido().isEmpty()) {
            throw new IllegalArgumentException("El apellido del usuario es obligatorio");
        }
        if (createPrestamoDTO.getCorreo() == null || createPrestamoDTO.getCorreo().isEmpty()) {
            throw new IllegalArgumentException("El correo del usuario es obligatorio");
        }
    }

}