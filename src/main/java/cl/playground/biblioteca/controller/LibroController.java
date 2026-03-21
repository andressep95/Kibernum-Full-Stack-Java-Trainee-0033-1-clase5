package cl.playground.biblioteca.controller;

import cl.playground.biblioteca.dto.CreateAutorDTO;
import cl.playground.biblioteca.dto.CreateLibroDTO;
import cl.playground.biblioteca.dto.ListAutorDTO;
import cl.playground.biblioteca.dto.ListLibroDTO;
import cl.playground.biblioteca.service.AutorService;
import cl.playground.biblioteca.service.LibroService;
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

import java.util.List;

@Controller
@RequestMapping("/libros")
public class LibroController {

    private final LibroService libroService;
    private final AutorService autorService;

    @Autowired
    public LibroController(LibroService libroService, AutorService autorService) {
        this.libroService = libroService;
        this.autorService = autorService;
    }

    @ModelAttribute("createLibroDTO")
    public CreateLibroDTO createLibroDTO() {
        CreateLibroDTO dto = new CreateLibroDTO();
        dto.setAutor(new CreateAutorDTO());
        return dto;
    }

    @GetMapping
    public String listarLibros(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<ListLibroDTO> libros = libroService.obtenerLibros(page, size);
        List<ListAutorDTO> autores = autorService.obtenerAutores();
        model.addAttribute("libros", libros);
        model.addAttribute("autores", autores);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", libros.getTotalPages());
        return "libros";
    }

    @PostMapping
    public String crearLibro(
            @ModelAttribute CreateLibroDTO createLibroDTO,
            RedirectAttributes redirectAttributes) {
        try {
            libroService.crearLibro(createLibroDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Libro creado exitosamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/libros";
    }
}
