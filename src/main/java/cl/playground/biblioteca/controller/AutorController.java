package cl.playground.biblioteca.controller;

import cl.playground.biblioteca.dto.CreateAutorConLibroDTO;
import cl.playground.biblioteca.dto.ListAutorDTO;
import cl.playground.biblioteca.service.AutorService;
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
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService;

    @Autowired
    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @ModelAttribute("createAutorConLibroDTO")
    public CreateAutorConLibroDTO createAutorConLibroDTO() {
        return new CreateAutorConLibroDTO();
    }

    @GetMapping
    public String listarAutores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<ListAutorDTO> autores = autorService.obtenerAutoresPaginado(page, size);
        model.addAttribute("autores", autores);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", autores.getTotalPages());
        return "autores";
    }

    @PostMapping
    public String crearAutor(
            @ModelAttribute CreateAutorConLibroDTO createAutorConLibroDTO,
            RedirectAttributes redirectAttributes) {
        try {
            autorService.CrearAutor(createAutorConLibroDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Autor creado exitosamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/autores";
    }
}