package br.com.gabriel.fintrack.repository;

import br.com.gabriel.fintrack.model.Transacao;
import br.com.gabriel.fintrack.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
     List<Transacao> findByUsuario_IdAndDataBetween(Long usuarioId, LocalDate dataInicio, LocalDate dataFim);
     List<Transacao> findByUsuario_IdAndCategoriaId(Long usuarioId, int categoriaId);

    @Query("SELECT SUM(t.valor) AS valor_total FROM Transacao t WHERE t.usuario.id = :usuarioId AND t.ehReceita = :ehReceita")
    BigDecimal sumValorByUsuarioIdAndTipo(@Param("usuarioId") Long usuarioId, @Param("ehReceita") boolean ehReceita);
}
