# 📊 FinTrack — Controle de Finanças Pessoais

> Sistema de controle de finanças pessoais desenvolvido em Java, evoluindo progressivamente de uma aplicação console até uma interface gráfica com banco de dados persistente.

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-17%2B-orange)
![License](https://img.shields.io/badge/license-MIT-blue)

---

## 📌 Sobre o Projeto

O **FinTrack** nasceu como um projeto de estudos de Java com foco em boas práticas de Programação Orientada a Objetos. O objetivo é construir, de forma incremental, um sistema completo de controle de finanças pessoais — começando por uma versão funcional via console e evoluindo até uma interface gráfica com persistência em banco de dados.

Cada versão introduz novos conceitos e tecnologias, tornando o projeto um portfólio progressivo de aprendizado em Java.

---

## 🗺️ Roadmap de Versões

| Versão | Nome | Status | Tecnologias principais |
|--------|------|--------|------------------------|
| v1 | Console | ✅ Concluída | Java puro, POO, Collections, Exceções |
| v2 | Interface Gráfica + Banco de Dados | 🚧 Em desenvolvimento | JavaFX, FXML, JDBC, SQLite, JUnit 5 |

---

## ✅ Versão 1 — Console

### Funcionalidades

- Cadastrar entradas (receitas) e saídas (despesas)
- Listar todas as transações registradas
- Exibir saldo atual
- Remover uma transação pelo índice
- Menu interativo em loop até o usuário sair

### Menu

```
===== FINTRACK - SEU CONTROLE FINANCEIRO =====
1. Adicionar nova transação
2. Listar transações
3. Mostrar saldo atual
4. Remover transação
5. Sair
```

### Conceitos aplicados

- `Scanner` e `System.out` para I/O via console
- Estruturas de controle: `if`, `switch`, `for`, `while`
- `ArrayList` para armazenamento em memória
- Manipulação de `String` e `double`
- Encapsulamento com getters e setters
- Polimorfismo: `Transacao` e `TransacaoMensal`
- Tratamento de exceções com `try/catch` e exceções personalizadas

### Estrutura de pacotes

```
fintrack/
├── app/
│   └── Main.java                    # Ponto de entrada — menu e execução
├── controller/
│   └── FinTracker.java              # Lógica principal de gerenciamento
├── model/
│   ├── Transacao.java               # Classe base para qualquer transação
│   └── TransacaoMensal.java         # Subclasse para transações recorrentes
├── exceptions/
│   └── EntradaInvalidaException.java  # Exceções personalizadas
└── utils/
    └── Formatador.java              # Utilitários de formatação
```

### Diagrama de classes (v1)

```
Transacao
├── descricao: String
├── valor: double
├── tipo: TipoTransacao (RECEITA | DESPESA)
├── getters / setters
└── toString()

TransacaoMensal extends Transacao
└── recorrente: boolean

FinTracker
├── transacoes: ArrayList<Transacao>
├── adicionarTransacao()
├── listarTransacoes()
├── removerTransacao()
└── calcularSaldoTotal()
```

---

## 🚧 Versão 2 — Interface Gráfica + Banco de Dados

### Novas funcionalidades previstas

- Interface gráfica com JavaFX
- Tabela de transações com filtros
- Tela de nova transação (receita ou despesa)
- Relatório de saldo
- Persistência em banco de dados SQLite via JDBC
- Testes unitários com JUnit 5

### Novos conceitos introduzidos

#### 1. Generics
- Classes genéricas para repositórios e serviços (`Repositorio<T>`)
- Métodos reutilizáveis para adicionar, remover e listar com tipo genérico
- Uso de curingas: `?`, `? extends T`, `? super T`

#### 2. JavaFX — Interface Gráfica
- `FinApp` estende `Application` e inicia o JavaFX via `start(Stage)`
- Telas planejadas:
  - Tela principal com `TableView` de transações
  - Tela de nova transação (receita ou despesa)
  - Tela de relatório e saldo
- Componentes: `Label`, `TextField`, `TextArea`, `Button`, `DatePicker`, `CheckBox`
- Layouts: `VBox`, `HBox`, `GridPane`, `BorderPane`
- Eventos com `setOnAction()`

#### 3. FXML + Scene Builder + CSS
- Telas definidas em arquivos `.fxml` separados
- Design visual criado com Scene Builder
- Lógica nos controllers vinculados por `fx:controller`
- Estilização com arquivos `.css`

#### 4. Acesso a banco de dados — JDBC
- Banco SQLite com tabela `transacoes`
- CRUD completo com `PreparedStatement` e `ResultSet`
- Padrão DAO: `TransacaoDAO` para abstrair a persistência
- `Conexao.java` centralizando o acesso ao banco
- Tratamento de erros SQL com `try/catch` e logs
- Controle de transações com `commit()` e `rollback()`

#### 5. Testes com JUnit 5
- Testes unitários para `Transacao`, `RepositorioGenerico` e `TransacaoDAO`
- SQLite em memória para testes de DAO (sem efeitos colaterais)
- Anotações: `@BeforeEach`, `@AfterEach`, `@Test`
- Validações de saldo, inserção e remoção
- Asserções: `assertEquals()`, `assertThrows()`

### Estrutura de pacotes (v2)

```
fintrack/
├── app/
│   └── FinApp.java                  # Ponto de entrada JavaFX
├── controller/
│   ├── FinTracker.java
│   ├── MainController.java          # Controller da tela principal
│   └── NovaTransacaoController.java
├── model/
│   ├── Transacao.java
│   └── TransacaoMensal.java
├── dao/
│   ├── TransacaoDAO.java            # Operações CRUD no banco
│   └── Conexao.java                 # Gerenciamento da conexão JDBC
├── repository/
│   └── RepositorioGenerico.java     # Repositório genérico <T>
├── exceptions/
│   └── EntradaInvalidaException.java
├── utils/
│   └── Formatador.java
├── resources/
│   ├── fxml/                        # Telas em FXML
│   ├── css/                         # Estilos
│   └── db/                          # Scripts SQL
└── test/
    ├── TransacaoTest.java
    ├── RepositorioGenericoTest.java
    └── TransacaoDAOTest.java
```

---

## 🛠️ Tecnologias utilizadas

| Tecnologia | Versão | Finalidade |
|------------|--------|------------|
| Java | 17+ | Linguagem principal |
| JavaFX | 17+ | Interface gráfica (v2) |
| SQLite + JDBC | — | Persistência (v2) |
| JUnit 5 | 5.x | Testes unitários (v2) |
| Scene Builder | — | Design de telas FXML (v2) |

---

## 📚 Objetivos de aprendizado

Este projeto foi desenvolvido com fins educacionais, cobrindo progressivamente:

- [x] Programação Orientada a Objetos (encapsulamento, herança, polimorfismo)
- [x] Coleções Java (`ArrayList`, iterações)
- [x] Tratamento de exceções (`try/catch`, exceções personalizadas)
- [ ] Generics e curingas
- [ ] Interfaces gráficas com JavaFX e FXML
- [ ] Acesso a banco de dados com JDBC
- [ ] Testes unitários com JUnit 5

---

## 👤 Autor

Desenvolvido por **Gabriel P dos Santos** como projeto de portfólio durante o aprendizado de Java.

[![LinkedIn](https://img.shields.io/badge/LinkedIn-GabrielPdoSantos%20-blue)](https://linkedin.com/in/gabrielPdoSantos)
[![GitHub](https://img.shields.io/badge/GitHub-GabrielPdoSantos%20-black)](https://github.com/GabrielPdoSantos)

---

## 📄 Licença

Este projeto está sob a licença MIT.
