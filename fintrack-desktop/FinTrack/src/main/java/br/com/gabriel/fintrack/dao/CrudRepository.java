package br.com.gabriel.fintrack.dao;

import br.com.gabriel.fintrack.model.Transacao;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T> {
    boolean adicionar(T entidade);

    boolean adicionar(Transacao transacao);

    boolean remover(int id) throws SQLException;

    List<T> listar();
}
