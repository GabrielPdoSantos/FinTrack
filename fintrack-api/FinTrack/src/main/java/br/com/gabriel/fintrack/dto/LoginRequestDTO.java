package br.com.gabriel.fintrack.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginRequestDTO {
    private String email;
    private String senha;


}
