package br.com.gabriel.fintrack.controller;

import br.com.gabriel.fintrack.dao.Conexao;
import br.com.gabriel.fintrack.model.Transacao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinTracker {
    public boolean adicionarTransicao(Transacao transacao) throws SQLException {
        String sql = "INSERT INTO transacoes (descricao, valor, ehReceita, data) VALUES (?, ?, ?, ?)";

        try (
                Connection conexao = Conexao.getConnection();
                PreparedStatement stmt = conexao.prepareStatement(sql);
        ) {
            stmt.setString(1, transacao.getDescricao());
            stmt.setDouble(2, transacao.getValor());
            stmt.setBoolean(3, transacao.isEhReceita());
            stmt.setDate(4, java.sql.Date.valueOf(transacao.getData()));

            stmt.executeUpdate();

        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    public boolean removerTransicao() {
        return true;
    }

    public List<Transacao> listarTransacoes() throws SQLException {
        String sql = "SELECT * FROM transacoes";
        List<Transacao> transacoes;
        try (
                Connection conexao = Conexao.getConnection();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            transacoes = new ArrayList<>();

            while (rs.next()) {
                String descricao = rs.getString("descricao");
                Double valor = rs.getDouble("valor");
                LocalDate data = rs.getDate("data").toLocalDate();
                boolean ehReceita = rs.getBoolean("ehReceita");

                Transacao t = new Transacao(descricao, valor, ehReceita, data);
                transacoes.add(t);
            }
        }
        return transacoes;
    }

    public Double calcularSaldoTotal() {
        return 0.0;
    }

}
