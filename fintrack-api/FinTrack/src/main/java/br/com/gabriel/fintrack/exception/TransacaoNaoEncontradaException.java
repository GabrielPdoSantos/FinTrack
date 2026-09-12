package br.com.gabriel.fintrack.exception;

public class TransacaoNaoEncontradaException extends RuntimeException{
    public TransacaoNaoEncontradaException(Long id) {
        super("Transação com id " + id + " não encontrada.");
    }

}
