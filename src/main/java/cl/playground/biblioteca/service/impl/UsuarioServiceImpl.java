package cl.playground.biblioteca.service.impl;

import cl.playground.biblioteca.dto.CreateUsuarioDTO;
import cl.playground.biblioteca.dto.ListUsuarioDTO;
import cl.playground.biblioteca.model.Usuario;
import cl.playground.biblioteca.repository.UsuarioRepository;
import cl.playground.biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void crearUsuario(CreateUsuarioDTO createUsuarioDTO) {
        // Validamos datos del nuevo usuario (DTO)
        validarUsuarioNuevo(createUsuarioDTO);

        // Si la validación es exitosa, se puede proceder a crear el usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(createUsuarioDTO.getNombre());
        nuevoUsuario.setApellido(createUsuarioDTO.getApellido());
        nuevoUsuario.setCorreo(createUsuarioDTO.getCorreo());

        // Guardamos el nuevo usuario en la base de datos
        usuarioRepository.save(nuevoUsuario);
    }

    @Override
    public Page<ListUsuarioDTO> listarUsuarios(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("El número de página debe ser mayor o igual a 0 y el tamaño debe ser mayor a 0.");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Usuario> usuariosPage = usuarioRepository.findAll(pageable);

        List<ListUsuarioDTO> listaDto = usuariosPage.getContent()
            .stream()
            .map(
                usuario -> {
                    ListUsuarioDTO dto = new ListUsuarioDTO();
                    dto.setIdUsuario(usuario.getIdUsuario());
                    dto.setNombre(usuario.getNombre());
                    dto.setApellido(usuario.getApellido());
                    dto.setCorreo(usuario.getCorreo());
                    return dto;
                })
            .toList();

        return new PageImpl<>(listaDto, pageable, usuariosPage.getTotalElements());
    }

    private void validarUsuarioNuevo(CreateUsuarioDTO createUsuarioDTO) {
        if (createUsuarioDTO.getNombre() == null || createUsuarioDTO.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio.");
        }
        if (createUsuarioDTO.getApellido() == null || createUsuarioDTO.getApellido().isEmpty()) {
            throw new IllegalArgumentException("El apellido del usuario es obligatorio.");
        }
        if (createUsuarioDTO.getCorreo() == null || createUsuarioDTO.getCorreo().isEmpty()) {
            throw new IllegalArgumentException("El correo del usuario es obligatorio.");
        }
        if (usuarioRepository.existsByCorreo(createUsuarioDTO.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }
    }
}
