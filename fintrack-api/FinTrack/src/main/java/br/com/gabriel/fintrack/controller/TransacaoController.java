package br.com.gabriel.fintrack.controller;


import br.com.gabriel.fintrack.dto.TransacaoRequestDTO;
import br.com.gabriel.fintrack.dto.TransacaoResponseDTO;
import br.com.gabriel.fintrack.service.TransacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transacoes")
public class TransacaoController {
    private final TransacaoService transacaoService;


    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @GetMapping
    public ResponseEntity<List<TransacaoResponseDTO>> listar(){
        return ResponseEntity.ok(transacaoService.listarTransacoes());
    }
    @GetMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> buscar(@PathVariable Long id){
        return ResponseEntity.ok(transacaoService.buscarTransacoes(id));
    }
    @PostMapping
    public ResponseEntity<TransacaoResponseDTO> cadastrar(@RequestBody TransacaoRequestDTO transacaoRequestDTO){
        TransacaoResponseDTO responseDTO = transacaoService.criarTransacao(transacaoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping
    public ResponseEntity<TransacaoResponseDTO> atualizar(@PathVariable Long id, @RequestBody TransacaoRequestDTO transacaoRequestDTO){
        return ResponseEntity.ok(transacaoService.atualizarTransacao(id, transacaoRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        transacaoService.deletarTransacao(id);
        return ResponseEntity.noContent().build();
    }
}
