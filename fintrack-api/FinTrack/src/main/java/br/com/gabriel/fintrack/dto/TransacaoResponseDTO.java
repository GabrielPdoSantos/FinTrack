package br.com.gabriel.fintrack.dto;

import br.com.gabriel.fintrack.model.TipoTransacao;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class TransacaoResponseDTO {
    private String nome;
    private String descricao;
    private BigDecimal valor;
    private TipoTransacao tipo;
    private Long id;
    private Instant timeStamp;
}
