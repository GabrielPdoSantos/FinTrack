package br.com.gabriel.fintrack.dao;

import br.com.gabriel.fintrack.model.Transacao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
class TransacaoDAOTest {
    private TransacaoDAO dao;
    private Connection conexaoMestra;

    @BeforeEach
    void setUp() throws SQLException {
        Conexao.setModoTeste(true);
        dao = new TransacaoDAO();


        conexaoMestra = Conexao.getConnection();
        try(Statement stmt = conexaoMestra.createStatement()) {
            String sql = "CREATE TABLE transacoes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "descricao TEXT, " +
                    "valor REAL, " +
                    "ehReceita BOOLEAN, " +
                    "data DATE)";
            stmt.execute(sql);

        }
    }
    @AfterEach
    void tearDown() throws SQLException {
        try(Statement stmt = conexaoMestra.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS transacoes");
        }
        if (conexaoMestra != null) {
            conexaoMestra.close();
        }
        Conexao.setModoTeste(false);
    }

    @Test
    void testAdicionarTransacao(){
        Transacao t = new Transacao(0, "Salaário", 5000.0, true, LocalDate.now());

        boolean sucesso = dao.adicionar(t);

        assertTrue(sucesso, "O método adicionar deveria retornar true!");

        assertEquals(1, dao.listar().size(), "Lista deveria ter 1 transição");
    }

    @Test
    void testCalcularSaldoTotal() throws SQLException {
        dao.adicionar(new Transacao(0, "Salário", 3000.0, true, LocalDate.now()));
        dao.adicionar(new Transacao(0, "Conta de Luz", 200.0, false, LocalDate.now()));

        Double saldo = dao.calcularSaldoTotal();

        assertEquals(2800.0, saldo, "O saldo calculado está errado!");
    }
  
}