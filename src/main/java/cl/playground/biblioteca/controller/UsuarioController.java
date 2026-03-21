package cl.playground.biblioteca.controller;

import cl.playground.biblioteca.dto.CreateUsuarioDTO;
import cl.playground.biblioteca.dto.ListUsuarioDTO;
import cl.playground.biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute("createUsuarioDTO")
    public CreateUsuarioDTO createUsuarioDTO() {
        return new CreateUsuarioDTO();
    }

    @GetMapping
    public String listarUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<ListUsuarioDTO> usuarios = usuarioService.listarUsuarios(page, size);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usuarios.getTotalPages());
        return "index";
    }

    @PostMapping
    public String crearUsuario(
            @ModelAttribute CreateUsuarioDTO createUsuarioDTO,
            RedirectAttributes redirectAttributes) {
        try {
            usuarioService.crearUsuario(createUsuarioDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario creado exitosamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/";
    }
}
