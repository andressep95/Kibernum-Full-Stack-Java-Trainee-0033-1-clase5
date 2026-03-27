package cl.playground.biblioteca.service;

import cl.playground.biblioteca.dto.CreateUsuarioDTO;
import cl.playground.biblioteca.dto.ListUsuarioDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UsuarioService {

    void crearUsuario(CreateUsuarioDTO createUsuarioDTO);

    Page<ListUsuarioDTO> listarUsuarios(int page, int size);

    List<ListUsuarioDTO> obtenerTodosLosUsuarios();

}
