package br.com.gabriel.fintrack.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="tb_usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;


    //Pede para o hiberanate olhar o usuario presente na classe Transação
    // Veja qual coluna está o id
    //E use essa coluna para filtrar e montar uma lista
    @OneToMany(mappedBy = "usuario")
    private List<Transacao> transacoes = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<Categoria> categorias = new ArrayList<>();

}
