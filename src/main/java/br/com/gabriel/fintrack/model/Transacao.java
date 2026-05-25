package br.com.gabriel.fintrack.model;

import java.time.LocalDate;

public class Transacao {
    String descricao;
    double valor;
    boolean ehReceita;
    LocalDate data;


    public Transacao(String descricao, double valor, boolean ehReceita, LocalDate data) {
        this.descricao = descricao;
        this.valor = valor;
        this.ehReceita = ehReceita;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Transacao{" +
                "descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", ehReceita=" + ehReceita +
                ", data=" + data +
                '}';
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
