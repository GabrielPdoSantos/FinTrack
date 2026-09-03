package br.com.gabriel.fintrack.repository;

import br.com.gabriel.fintrack.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
