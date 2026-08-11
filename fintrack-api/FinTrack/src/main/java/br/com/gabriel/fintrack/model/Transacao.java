package br.com.gabriel.fintrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="tb_transacoes")
@Data
@NoArgsConstructor //gera automaticamente um construtor sem nenhum argumento
@AllArgsConstructor //gera com todos os argumentos
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String descricao;
    private BigDecimal valor;
    private boolean ehReceita;
    private LocalDate data;


    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;




}
