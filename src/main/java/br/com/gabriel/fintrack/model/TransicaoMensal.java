package br.com.gabriel.fintrack.model;

import java.time.LocalDate;

public class TransicaoMensal extends Transacao {
    private int diaVencimento;
    private LocalDate dataInicio;
    private boolean ativa;


    public TransicaoMensal() {
        super();
    }

}
