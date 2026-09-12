package br.com.gabriel.fintrack.exception;

import br.com.gabriel.fintrack.dto.ErroResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TransacaoNaoEncontradaException.class)
    public ResponseEntity<String> tratarTarefaNaoEncontrada(
            TransacaoNaoEncontradaException exception) {

        ErroResponseDTO erro = new ErroResponseDTO(
                404,
                "Not Found",
                exception.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());

    }
    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> tratarUsuarioNaoEncontrado(
            UsuarioNaoEncontradoException exception) {

        ErroResponseDTO erro = new ErroResponseDTO(
                404,
                "Not Found",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(erro);

    }
    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<ErroResponseDTO> tratarEmailJaCadastrado(
            EmailJaCadastradoException exception) {

        ErroResponseDTO erro = new ErroResponseDTO(
                409,
                "Conflict",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(erro);

    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> tratarErroGenerico(
            Exception exception) {

        ErroResponseDTO erro = new ErroResponseDTO(
                500,
                "Internal Server Error",
                "Ocorreu um erro inesperado. Tente novamente mais tarde."
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(erro);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> tratarErroValidacao(
            MethodArgumentNotValidException exception) {

        // Pega a primeira mensagem de erro da lista de erros de validação
        String mensagem = exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        ErroResponseDTO erro = new ErroResponseDTO(
                400,
                "Bad Request",
                mensagem
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erro);

    }
}
