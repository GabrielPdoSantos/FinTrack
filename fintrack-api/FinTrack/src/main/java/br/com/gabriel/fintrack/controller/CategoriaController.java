package br.com.gabriel.fintrack.controller;


import br.com.gabriel.fintrack.dto.CategoriaRequestDTO;
import br.com.gabriel.fintrack.dto.CategoriaResponseDTO;
import br.com.gabriel.fintrack.dto.TransacaoRequestDTO;
import br.com.gabriel.fintrack.dto.TransacaoResponseDTO;
import br.com.gabriel.fintrack.service.CategoriaService;
import br.com.gabriel.fintrack.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categorias", description = "Operações relacionadas ao gerenciamento de categorias")
@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final TransacaoService transacaoService;

    public CategoriaController(CategoriaService categoriaService, TransacaoService transacaoService) {
        this.categoriaService = categoriaService;
        this.transacaoService = transacaoService;
    }

    @Operation(
            summary = "Cadastrar categoria",
            description = "Cria uma nova categoria para o usuário autenticado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@RequestBody CategoriaRequestDTO categoriaRequestDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        CategoriaResponseDTO categoriaResponseDTO = categoriaService.criarCategoria(categoriaRequestDTO, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaResponseDTO);
    }
    @Operation(
            summary = "Listar categorias",
            description = "Retorna todas as categorias do usuário autenticado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(categoriaService.listarCategorias(email));
    }
    @Operation(
            summary = "Atualizar categoria",
            description = "Atualiza uma categoria existente pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @RequestBody CategoriaRequestDTO categoriaRequestDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(categoriaService.atualizarCategoria(id, categoriaRequestDTO, email));
    }
    @Operation(
            summary = "Deletar categoria",
            description = "Remove uma categoria pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        categoriaService.excluirCategoria(id, email);
        return ResponseEntity.noContent().build();
    }
}

