package br.com.gabriel.fintrack.dto;

import br.com.gabriel.fintrack.model.TipoTransacao;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransacaoRequestDTO {
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 1, max = 50)
    private String nome;

    @Size(min=1, max=100)
    private String descricao;

    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "O tipo de transação é obrigatória")
    private TipoTransacao tipo;

    @NotNull(message = "A categoria é obrigatória")
    private Long categoriaId;
}
