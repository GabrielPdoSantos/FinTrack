package br.com.gabriel.fintrack.dto;

import br.com.gabriel.fintrack.model.TipoTransacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransacaoResponseDTO {
    private String nome;
    private String descricao;
    private BigDecimal valor;
    private TipoTransacao tipo;
    private Long id;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataCriacao;


}
