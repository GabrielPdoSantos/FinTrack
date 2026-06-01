package br.com.gabriel.fintrack.controller;

import br.com.gabriel.fintrack.dao.TransacaoDAO;
import br.com.gabriel.fintrack.model.Transacao;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

    public class TransacaoController {



        @FXML private Label labelSaldoAtual;
        @FXML private Label labelMensagem;

        // Campos do Formulário
        @FXML private TextField campoDescricao;
        @FXML private TextField campoValor;
        @FXML private DatePicker campoData;
        @FXML private RadioButton radioReceita;

        // Componentes da Tabela
        @FXML private TableView<Transacao> tabelaTransacoes;
        @FXML private TableColumn<Transacao, Integer> colunaId;
        @FXML private TableColumn<Transacao, String> colunaDescricao;
        @FXML private TableColumn<Transacao, Double> colunaValor;
        @FXML private TableColumn<Transacao, String> colunaTipo; // Transformaremos true/false em texto
        @FXML private TableColumn<Transacao, LocalDate> colunaData;


        private TransacaoDAO dao = new TransacaoDAO();
        private ObservableList<Transacao> listaTransacoesJavaFX;


        @FXML
        public void initialize() throws SQLException {
            colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
            colunaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
            colunaData.setCellValueFactory(new PropertyValueFactory<>("data"));

            colunaTipo.setCellValueFactory(cellData -> {
                boolean receita = cellData.getValue().isEhReceita();
                return new SimpleStringProperty(receita ? "Receita" : "Despesa");
            });

            campoData.setValue(LocalDate.now());

            atualizarTabela();
            atualizarSaldo();
        }

        @FXML
        public void btnSalvarClicado() {
            try {
                // 1. Pega os valores que o usuário digitou
                String descricao = campoDescricao.getText();
                // Troca vírgula por ponto para não dar erro no Double
                double valor = Double.parseDouble(campoValor.getText().replace(",", "."));
                LocalDate data = campoData.getValue();
                boolean ehReceita = radioReceita.isSelected();

                // 2. Valida se a descrição não está vazia
                if (descricao.trim().isEmpty()) {
                    labelMensagem.setText("Erro: A descrição não pode ser vazia!");
                    labelMensagem.setStyle("-fx-text-fill: red;");
                    return;
                }

                // 3. Cria o objeto Model
                Transacao novaTransacao = new Transacao(0, descricao, valor, ehReceita, data);

                // 4. Manda a "cozinha" (DAO) salvar no banco
                boolean sucesso = dao.adicionar(novaTransacao);

                if (sucesso) {
                    labelMensagem.setText("Transação salva com sucesso!");
                    labelMensagem.setStyle("-fx-text-fill: green;");

                    // 5. Limpa a tela e atualiza os números
                    limparFormulario();
                    atualizarTabela();
                    atualizarSaldo();
                } else {
                    labelMensagem.setText("Erro ao salvar no banco de dados.");
                    labelMensagem.setStyle("-fx-text-fill: red;");
                }

            } catch (NumberFormatException | SQLException e) {
                labelMensagem.setText("Erro: Digite um valor numérico válido!");
                labelMensagem.setStyle("-fx-text-fill: red;");
            }
        }


        private void atualizarTabela() {
            List<Transacao> transacoesDoBanco = dao.listar();

            listaTransacoesJavaFX = FXCollections.observableArrayList(transacoesDoBanco);

            tabelaTransacoes.setItems(listaTransacoesJavaFX);
        }

        private void atualizarSaldo() throws SQLException {
            Double saldo = dao.calcularSaldoTotal();
            labelSaldoAtual.setText(String.format("R$ %.2f", saldo));

            if (saldo >= 0) {
                labelSaldoAtual.setStyle("-fx-text-fill: #2ecc71;"); // Verde
            } else {
                labelSaldoAtual.setStyle("-fx-text-fill: #e74c3c;"); // Vermelho
            }
        }
        @FXML
        public void btnRemoverClicado() throws SQLException {
            Transacao transacaoSelecionada = tabelaTransacoes.getSelectionModel().getSelectedItem();

            if (transacaoSelecionada == null) {
                labelMensagem.setText("Erro: Selecione uma transação na tabela primeiro!");
                labelMensagem.setStyle("-fx-text-fill: #e74c3c;"); // Vermelho
                return;
            }

            boolean sucesso = dao.remover(transacaoSelecionada.getId());

            if (sucesso) {
                labelMensagem.setText("Transação removida com sucesso!");
                labelMensagem.setStyle("-fx-text-fill: #2ecc71;"); // Verde
                atualizarTabela();
                atualizarSaldo();
            } else {
                labelMensagem.setText("Erro ao remover no banco de dados.");
                labelMensagem.setStyle("-fx-text-fill: #e74c3c;");
            }
        }

        private void limparFormulario() {
            campoDescricao.clear();
            campoValor.clear();
            campoData.setValue(LocalDate.now());
            radioReceita.setSelected(true);
        }
}
