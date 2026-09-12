package br.com.gabriel.fintrack.exception;

public class UsuarioNaoAutorizadoException extends RuntimeException {
    public UsuarioNaoAutorizadoException(String mensagem) {
        super(mensagem);
    }
}
