package br.com.gabriel.fintrack.model;

import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class TransacaoMensal extends Transacao {

    private int diaVencimento;
    private LocalDate dataInicio;
    private boolean ativa;

    // Getters e Setters
    public int getDiaVencimento() { return diaVencimento; }
    public void setDiaVencimento(int diaVencimento) { this.diaVencimento = diaVencimento; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
}