package br.com.gabriel.fintrack.controller;


import br.com.gabriel.fintrack.dto.TransacaoRequestDTO;
import br.com.gabriel.fintrack.dto.TransacaoResponseDTO;
import br.com.gabriel.fintrack.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
import java.util.List;

@Tag(name="Transações", description = "Operações relacionadas ao gerenciamento de transações")
@RestController
@RequestMapping("/api/v1/transacoes")
public class TransacaoController {
    private final TransacaoService transacaoService;


    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }
    @Operation(
            summary = "Listar transações",
            description = "Retorna todas as transações do usuário autenticado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping
    public ResponseEntity<List<TransacaoResponseDTO>> listar(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<TransacaoResponseDTO> transacaos = transacaoService.listarTransacoesPorUsuario(email);
        return ResponseEntity.ok(transacaos);
    }

    @Operation(
            summary = "Cadastrar transação",
            description = "Cria uma nova transação para o usuário autenticado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PostMapping
    public ResponseEntity<TransacaoResponseDTO> cadastrar(@RequestBody TransacaoRequestDTO transacaoRequestDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        TransacaoResponseDTO responseDTO = transacaoService.criarTransacao(transacaoRequestDTO, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Operation(
            summary = "Atualizar transação",
            description = "Atualiza uma transação existente pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> atualizar(@PathVariable Long id, @RequestBody TransacaoRequestDTO transacaoRequestDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(transacaoService.atualizarTransacao(id, transacaoRequestDTO, email));
    }
    @Operation(
            summary = "Deletar transação",
            description = "Remove uma transação pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transação deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        transacaoService.deletarTransacao(id, email);
        return ResponseEntity.noContent().build();
    }
}
