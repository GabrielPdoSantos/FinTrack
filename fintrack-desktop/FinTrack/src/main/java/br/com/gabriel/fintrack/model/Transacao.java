package br.com.gabriel.fintrack.model;

import java.time.LocalDate;

public class Transacao {
    private int id;
    private String descricao;
    private double valor;
    private boolean ehReceita;
    private LocalDate data;


    public Transacao(int id, String descricao, double valor, boolean ehReceita, LocalDate data) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.ehReceita = ehReceita;
        this.data = data;
    }

    public Transacao(String descricao, double valor, boolean ehReceita, LocalDate data) {
        this.descricao = descricao;
        this.valor = valor;
        this.ehReceita = ehReceita;
        this.data = data;
    }

    public Transacao() {

    }

    @Override
    public String toString() {
        return "Transacao{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", ehReceita=" + ehReceita +
                ", data=" + data +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public boolean isEhReceita() {
        return ehReceita;
    }

    public LocalDate getData() {
        return data;
    }
}
