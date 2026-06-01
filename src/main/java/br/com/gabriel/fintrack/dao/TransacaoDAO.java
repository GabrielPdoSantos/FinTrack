package br.com.gabriel.fintrack.dao;

import br.com.gabriel.fintrack.model.Transacao;
import br.com.gabriel.fintrack.model.TransacaoMensal; // <-- Importante!

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO implements CrudRepository<Transacao>{

    public boolean adicionar(Transacao transacao){
        String sql = "INSERT INTO transacoes (descricao, valor, ehReceita, data, is_mensal, dia_vencimento, data_inicio, ativa) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conexao = null;
        try {
            conexao = Conexao.getConnection();
            conexao.setAutoCommit(false);
            try (PreparedStatement stmt = conexao.prepareStatement(sql)){
                stmt.setString(1, transacao.getDescricao());
                stmt.setDouble(2, transacao.getValor());
                stmt.setBoolean(3, transacao.isEhReceita());
                stmt.setDate(4, java.sql.Date.valueOf(transacao.getData()));

                if (transacao instanceof TransacaoMensal) {
                    TransacaoMensal tm = (TransacaoMensal) transacao;
                    stmt.setBoolean(5, true);
                    stmt.setInt(6, tm.getDiaVencimento());
                    stmt.setDate(7, java.sql.Date.valueOf(tm.getDataInicio()));
                    stmt.setBoolean(8, tm.isAtiva());
                } else {
                    stmt.setBoolean(5, false);
                    stmt.setNull(6, java.sql.Types.INTEGER);
                    stmt.setNull(7, java.sql.Types.DATE);
                    stmt.setNull(8, java.sql.Types.BOOLEAN);
                }

                stmt.executeUpdate();
            }
            conexao.commit();
            return true;
        } catch (SQLException ex) {
            System.out.println("ERRO NO BANCO " + ex.getMessage());
            if (conexao != null) {
                try {
                    conexao.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
        finally {
            if (conexao != null) {
                try{
                    conexao.setAutoCommit(true);
                    conexao.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean remover(int id) throws SQLException {
        String sql = "DELETE FROM transacoes WHERE id = ?";
        Connection conexao = null;
        try {
            conexao = Conexao.getConnection();
            conexao.setAutoCommit(false);

            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int linhasAfetadas = stmt.executeUpdate();
                if (linhasAfetadas == 0) {
                    conexao.rollback();
                    return false;
                }
            }
            conexao.commit();
            return true;
        } catch (SQLException ex) {
            System.out.println("Erro ao remover: " + ex.getMessage());
            if (conexao != null) {
                try {
                    conexao.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conexao != null) {
                try {
                    conexao.setAutoCommit(true);
                    conexao.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Transacao> listar(){
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

                boolean isMensal = rs.getBoolean("is_mensal");

                Transacao t;

                if (isMensal) {
                    int diaVencimento = rs.getInt("dia_vencimento");
                    LocalDate dataInicio = rs.getDate("data_inicio").toLocalDate();
                    boolean ativa = rs.getBoolean("ativa");

                    t = new TransacaoMensal(id, descricao, valor, ehReceita, data, diaVencimento, dataInicio, ativa);
                } else {

                    t = new Transacao(id, descricao, valor, ehReceita, data);
                }

                transacoes.add(t);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return transacoes;
    }

    public Double calcularSaldoTotal() throws SQLException {
        String sql = "SELECT SUM(CASE WHEN ehReceita = true THEN valor ELSE -valor END) AS saldo_total FROM transacoes";
        double saldoTotal = 0.0;

        try (Connection conexao = Conexao.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()
        ) {

            if (rs.next()) {
                saldoTotal = rs.getDouble(1);
            }
        } catch(SQLException e){
            System.out.println("Erro ao calcular saldo " + e.getMessage());
        }
        return saldoTotal;
    }
}