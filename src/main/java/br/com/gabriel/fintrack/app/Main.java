package br.com.gabriel.fintrack.app;

import br.com.gabriel.fintrack.controller.FinTracker;
import br.com.gabriel.fintrack.dao.Conexao;
import br.com.gabriel.fintrack.model.Transacao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        FinTracker finTracker = new FinTracker();
        Transacao transacao = new Transacao("Transação teste", 100.00, true, LocalDate.of(2026, 05, 20));
        // finTracker.adicionarTransicao(transacao);
        finTracker.listarTransacoes();
    }

    public static void menu(){
        Scanner input = new Scanner(System.in);
        System.out.println("===== FINTRACK - SEU CONTROLE FINANCEIRO =====");
        System.out.println("1. Adicionar nova transição");
        System.out.println("2. Listar transações");
        System.out.println("3. Mostrar saldo atual");
        System.out.println("4. Remover transação");
        System.out.println("5. Sair");
        do {
            int opcao = input.nextInt();
            switch (opcao) {
                case 1:
                    System.out.println("1");
                    break;
                case 2:
                    System.out.println("2");
                    break;
                case 3:
                    System.out.println("3");
                    break;
                case 4:
                    System.out.println("4");
                    break;
                case 5:
                    System.out.println("Sair");
                    break;
                default:
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println("Digite uma das opções acima");
                    menu();
            }
        } while (true);
    }
}