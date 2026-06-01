package br.com.gabriel.fintrack.dao;

import br.com.gabriel.fintrack.model.Transacao;
import br.com.gabriel.fintrack.model.TransacaoMensal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class TransacaoDAOTest {
    private TransacaoDAO dao;
    private Connection conexaoMestra;

    @BeforeEach
    public void setUp() throws SQLException {
        dao = new TransacaoDAO();
        conexaoMestra = Conexao.getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS transacoes (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "descricao TEXT, " +
                "valor REAL, " +
                "ehReceita BOOLEAN, " +
                "data DATE, " +
                "is_mensal BOOLEAN, " +
                "dia_vencimento INTEGER, " +
                "data_inicio DATE, " +
                "ativa BOOLEAN)";

        try (Statement stmt = conexaoMestra.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS transacoes");
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
    @Test
    public void deveAdicionarEListarTransacaoMensalComSucesso() {
        LocalDate hoje = LocalDate.now();
        TransacaoMensal contaLuz = new TransacaoMensal(0, "Conta de Luz", 120.50, false, hoje, 15, hoje, true);
        boolean sucesso = dao.adicionar(contaLuz);
        assertTrue(sucesso, "A transação mensal deveria ser salva com sucesso");
        List<Transacao> doBanco = dao.listar();
        assertEquals(1, doBanco.size(), "Deve haver 1 transação no banco");
        Transacao transacaoSalva = doBanco.get(0);
        assertTrue(transacaoSalva instanceof TransacaoMensal, "O objeto retornado deve ser uma TransacaoMensal");
        TransacaoMensal tmSalva = (TransacaoMensal) transacaoSalva;
        assertEquals(15, tmSalva.getDiaVencimento(), "O dia de vencimento deve ser 15");
        assertTrue(tmSalva.isAtiva(), "A assinatura deve estar ativa");
    }
}