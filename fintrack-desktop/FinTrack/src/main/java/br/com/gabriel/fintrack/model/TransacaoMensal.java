package br.com.gabriel.fintrack.model;

import java.time.LocalDate;

public class TransacaoMensal extends Transacao {

    private int diaVencimento;
    private LocalDate dataInicio;
    private boolean ativa;

    // Construtor completo
    public TransacaoMensal(int id, String descricao, double valor, boolean ehReceita, LocalDate data, int diaVencimento, LocalDate dataInicio, boolean ativa) {
        // Manda os dados básicos lá para a classe Transacao (a mãe)
        super(id, descricao, valor, ehReceita, data);

        // Guarda os dados específicos da classe filha
        this.diaVencimento = diaVencimento;
        this.dataInicio = dataInicio;
        this.ativa = ativa;
    }

    // Getters e Setters
    public int getDiaVencimento() { return diaVencimento; }
    public void setDiaVencimento(int diaVencimento) { this.diaVencimento = diaVencimento; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
}