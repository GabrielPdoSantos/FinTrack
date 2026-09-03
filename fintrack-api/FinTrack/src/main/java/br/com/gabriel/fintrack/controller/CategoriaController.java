package br.com.gabriel.fintrack.controller;


import br.com.gabriel.fintrack.dto.CategoriaResponseDTO;
import br.com.gabriel.fintrack.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    //@PostMapping
    //public ResponseEntity<CategoriaResponseDTO> criar()
}

