package br.com.gabriel.fintrack.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RegisterRequestDTO {
    private String nome;
    private String email;
    private String senha;
}
