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
            System.out.println("ERRO NO BANCO " + ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean removerTransicao(int id) throws SQLException {
        String sql = "DELETE FROM transacoes WHERE id = ?";
        try(
            Connection conexao = Conexao.getConnection();
            PreparedStatement stmt= conexao.prepareStatement(sql);
        ) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

            return stmt.getUpdateCount() > 0;
        }
    }

    public List<Transacao> listarTransacoes() throws SQLException {
        String sql = "SELECT * FROM transacoes";
        List<Transacao> transacoes = new ArrayList<>();

        try (
                Connection conexao = Conexao.getConnection();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String descricao = rs.getString("descricao");
                Double valor = rs.getDouble("valor");
                LocalDate data = rs.getDate("data").toLocalDate();
                boolean ehReceita = rs.getBoolean("ehReceita");

                Transacao t = new Transacao(id, descricao, valor, ehReceita, data);
                transacoes.add(t);

            }
        }

        return transacoes;
    }

    public Double calcularSaldoTotal() throws SQLException {
        //Percorrer a lista de transações e calcular o dinheiro e retornar a resposta
        // Se for despesa subtrai

        String sql = "SELECT SUM(CASE WHEN ehReceita = true THEN valor ELSE -valor END) AS saldo_total FROM transacoes";

        Connection conexao = Conexao.getConnection();
        PreparedStatement stmt = conexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        double saldoTotal = 0.0;
        if (rs.next()) {
            saldoTotal = rs.getDouble(1);
        }

        return saldoTotal;
    }

}
