package br.com.gabriel.fintrack.repository;

import br.com.gabriel.fintrack.model.TipoTransacao;
import br.com.gabriel.fintrack.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByUsuarioIdAndDataBetween(Long usuarioId, Instant dataInicio, Instant dataFim);


    @Query("SELECT COALESCE(SUM(t.valor),0) FROM Transacao t WHERE t.usuario.id = :usuarioId AND t.tipo = :tipo")
    BigDecimal sumValorByUsuarioIdAndCategoriaId(
            @Param("usuarioId") Long usuarioId,
            @Param("tipo")TipoTransacao tipo
            );
    List<Transacao> findByUsuarioIdAndCategoriaId(Long usuarioId, Long categoriaId);
}
