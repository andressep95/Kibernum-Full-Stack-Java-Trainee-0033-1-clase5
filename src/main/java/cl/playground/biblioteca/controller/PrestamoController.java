package cl.playground.biblioteca.controller;

import cl.playground.biblioteca.dto.CreatePrestamoDTO;
import cl.playground.biblioteca.dto.ListPrestamosDTO;
import cl.playground.biblioteca.service.LibroService;
import cl.playground.biblioteca.service.PrestamoService;
import cl.playground.biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {

    private final LibroService libroService;
    private final PrestamoService prestamoService;
    private final UsuarioService usuarioService;

    @Autowired
    public PrestamoController(LibroService libroService, PrestamoService prestamoService, UsuarioService usuarioService) {
        this.libroService = libroService;
        this.prestamoService = prestamoService;
        this.usuarioService = usuarioService;
    }

    @ModelAttribute("createPrestamoDTO")
    public CreatePrestamoDTO createPrestamoDTO() {
        return new CreatePrestamoDTO();
    }

    @GetMapping
    public String listarPrestamos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<ListPrestamosDTO> prestamos = prestamoService.listarPrestamos(page, size);
        model.addAttribute("prestamos", prestamos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", prestamos.getTotalPages());
        model.addAttribute("libros", libroService.obtenerTodosLosLibros());
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        return "prestamos";
    }

    @PostMapping
    public String crearPrestamo(
            @ModelAttribute CreatePrestamoDTO createPrestamoDTO,
            RedirectAttributes redirectAttributes) {
        try {
            prestamoService.crearPrestamo(createPrestamoDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Préstamo registrado exitosamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/prestamos";
    }
}