package br.com.gabriel.fintrack.repository;

import br.com.gabriel.fintrack.model.Categoria;
import br.com.gabriel.fintrack.model.Transacao;
import br.com.gabriel.fintrack.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByUsuario(Usuario usuario);

}
