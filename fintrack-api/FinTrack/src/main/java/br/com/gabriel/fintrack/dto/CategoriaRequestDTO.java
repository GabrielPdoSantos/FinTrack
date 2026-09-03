package br.com.gabriel.fintrack.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoriaRequestDTO {
    @NotNull
    @Size(min = 1, max = 50, message = "Tamanho do nome da categoria está fora das normas")
    private String nome;
}
