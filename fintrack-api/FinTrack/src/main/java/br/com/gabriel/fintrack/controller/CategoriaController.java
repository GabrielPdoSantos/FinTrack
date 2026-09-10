package br.com.gabriel.fintrack.controller;


import br.com.gabriel.fintrack.dto.CategoriaRequestDTO;
import br.com.gabriel.fintrack.dto.CategoriaResponseDTO;
import br.com.gabriel.fintrack.dto.TransacaoRequestDTO;
import br.com.gabriel.fintrack.dto.TransacaoResponseDTO;
import br.com.gabriel.fintrack.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@RequestBody CategoriaRequestDTO categoriaRequestDTO){
        CategoriaResponseDTO categoriaResponseDTO = categoriaService.criarCategoria(categoriaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaResponseDTO);
    }
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar(){
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscar(@PathVariable Long id){
        return ResponseEntity.ok(categoriaService.buscarCategoriaPorID(id));
    }
    @PutMapping
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @RequestBody CategoriaRequestDTO categoriaRequestDTO){
        return ResponseEntity.ok(categoriaService.atualizarCategoria(id, categoriaRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        categoriaService.excluirCategoria(id);
        return ResponseEntity.noContent().build();
    }
}

