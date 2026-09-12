# FinTrack API (ainda em desenvolvimento)

API RESTful multiusuário para controle de finanças pessoais, construída com Spring Boot. É a evolução do FinTrack desktop (JavaFX + JDBC): a lógica de negócio e a persistência saem do cliente e passam a viver num back-end stateless, autenticado via JWT, com isolamento total de dados entre usuários.

## O que essa versão resolve

A versão desktop tinha um problema estrutural: um único banco local, sem conceito de usuário. A API resolve isso com três garantias:

- **Multiusuário real**: toda transação e categoria pertence a um `Usuario`. Não existe endpoint que devolva dados sem filtrar pelo dono do recurso.
- **Autorização em nível de método**: `@PreAuthorize` garante que o usuário autenticado só acessa `/usuarios/{id}/...` onde `{id}` é o próprio ID — mesmo que ele descubra ou adivinhe o ID de outro usuário, a chamada retorna 403.
- **Documentação viva**: Swagger/OpenAPI gerado a partir do código, então o contrato da API nunca fica desatualizado em relação à implementação.

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem / Framework | Java 21 + Spring Boot 3 |
| Persistência | Spring Data JPA (Hibernate) |
| Banco de dados | H2 (arquivo local, via `spring.datasource.url=jdbc:h2:file:./data/fintrack`) |
| Segurança | Spring Security + JWT (jjwt) |
| Documentação | springdoc-openapi-ui |
| Build | Maven |
| Testes | JUnit 5 + Mockito + Spring Boot Test |

## Arquitetura

```
Controller → Service → Repository → Entity
     ↓           ↓
    DTO      Regras de negócio
```

- **Controller**: recebe/devolve DTOs, nunca entidades JPA diretamente (evita vazar estrutura de banco no contrato da API e problemas de serialização com relacionamentos lazy).
- **Service**: onde vive o cálculo de saldo, validação de saldo insuficiente e o mapeamento DTO ↔ Entidade.
- **Repository**: interfaces `JpaRepository` com queries JPQL customizadas para relatórios.

## Modelo de dados

```
Usuario (1) ──< (N) Categoria
Usuario (1) ──< (N) Transacao
Categoria (1) ──< (N) Transacao
```

- `Usuario`: id, nome, email (único), senha (hash BCrypt), roles.
- `Categoria`: id, nome, tipo (RECEITA/DESPESA), usuario (dono — categorias são customizáveis por usuário, não globais).
- `Transacao`: id, descricao, valor, data, tipo (RECEITA/DESPESA), usuario, categoria.

## Endpoints principais

Prefixo de versionamento: `/api/v1`.

### Autenticação (públicos)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/auth/register` | Cria usuário, retorna 201 |
| POST | `/api/auth/login` | Valida credenciais, retorna JWT |

### Transações (protegidos)

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/v1/usuarios/{id}/transacoes` | Lista transações do usuário |
| GET | `/api/v1/usuarios/{id}/transacoes/{transacaoId}` | Detalhe de uma transação |
| POST | `/api/v1/usuarios/{id}/transacoes` | Cria transação |
| PUT | `/api/v1/usuarios/{id}/transacoes/{transacaoId}` | Atualiza transação |
| DELETE | `/api/v1/usuarios/{id}/transacoes/{transacaoId}` | Remove transação |
| GET | `/api/v1/usuarios/{id}/transacoes/saldo` | Retorna `SaldoDTO` (receitas, despesas, saldo) |
| GET | `/api/v1/usuarios/{id}/transacoes?dataInicio=...&dataFim=...` | Relatório por período |

### Categorias (protegidos)

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/v1/usuarios/{id}/categorias` | Lista categorias do usuário |
| POST | `/api/v1/usuarios/{id}/categorias` | Cria categoria customizada |
| DELETE | `/api/v1/usuarios/{id}/categorias/{categoriaId}` | Remove categoria |

Todo endpoint sob `/usuarios/{id}/...` exige, além do JWT válido, que o `{id}` da rota bata com o `id` do usuário autenticado (via `@PreAuthorize("#id == authentication.principal.id")` ou equivalente).

## Segurança

- Login gera um JWT assinado (HS256) com claim `sub` = id do usuário e expiração curta (ex: 1h) + suporte a refresh token.
- `SecurityFilterChain` bloqueia tudo por padrão, libera só `/api/auth/**` e `/swagger-ui/**`.
- Senhas nunca trafegam nem são logadas em texto plano; hash com BCrypt.
- `@PreAuthorize` em cada método do controller/service que recebe um `usuarioId` como parâmetro — a checagem de dono do recurso é a linha de defesa que faz a diferença entre "API multiusuário" e "API com um bug grave de IDOR".

## Tratamento de erros

`GlobalExceptionHandler` centraliza as respostas de erro em JSON consistente (`timestamp`, `status`, `error`, `message`, `path`):

| Exceção | Status |
|---|---|
| `ResourceNotFoundException` | 404 |
| `ValidationException` (Bean Validation em DTOs) | 400 |
| `SaldoInsuficienteException` | 409 |
| `AccessDeniedException` (Spring Security) | 403 |
| `AuthenticationException` / credenciais inválidas | 401 |
| `EmailJaCadastradoException` | 409 |

## Rodando localmente

```bash
git clone <url-do-repositorio>
cd fintrack-api

# application.properties (ou .yml):
# spring.datasource.url=jdbc:h2:file:./data/fintrack
# spring.datasource.driverClassName=org.h2.Driver
# spring.jpa.hibernate.ddl-auto=update
# spring.h2.console.enabled=true
# jwt.secret=<segredo-de-pelo-menos-256-bits>

./mvnw spring-boot:run
```

Com `spring.h2.console.enabled=true`, o console web do H2 fica em `http://localhost:8080/h2-console` — útil para conferir as tabelas sem precisar de outro programa. A URL de conexão a usar lá dentro é a mesma do `spring.datasource.url`.

Swagger UI disponível em `http://localhost:8080/swagger-ui.html` após subir a aplicação.

### Testando a API sem curl

Curl é só um jeito de fazer requisições HTTP pelo terminal — não é obrigatório. Formas mais visuais de testar:

- **Swagger UI** (`/swagger-ui.html`): já lista todos os endpoints com botão "Try it out", onde você preenche os campos e clica em executar. Pra `/api/v1/...`, tem um botão "Authorize" no topo onde você cola `Bearer <token>` recebido no login.
- **Postman ou Insomnia**: programas gratuitos com interface gráfica (montar a requisição em campos, sem escrever comando nenhum). Cria uma requisição POST para `/api/auth/register`, manda o corpo em JSON, e a resposta aparece na tela.

Se mais pra frente você quiser aprender curl mesmo assim, é simples: `curl -X POST URL -H "Content-Type: application/json" -d '{"campo":"valor"}'` — `-X` define o método, `-H` manda um header, `-d` manda o corpo da requisição. Mas pro dia a dia de testar a API, o Swagger UI já resolve.

## Migração a partir da versão desktop

| FinTrack Desktop (JavaFX/JDBC) | FinTrack API |
|---|---|
| `TransacaoDAO` (JDBC manual) | `TransacaoRepository` (Spring Data JPA) |
| Banco único, sem usuário | Entidade `Usuario`, dados isolados por dono |
| Lógica de saldo na tela | Lógica de saldo no `TransacaoService` |
| Sem contrato formal | DTOs + OpenAPI/Swagger |
| Sem autenticação | Spring Security + JWT |
