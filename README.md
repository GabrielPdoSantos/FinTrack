# FinTrack API

API RESTful multiusuário para controle de finanças pessoais, construída com Spring Boot. É a evolução do FinTrack desktop (JavaFX + JDBC): a lógica de negócio e a persistência saem do cliente e passam a viver num back-end stateless, autenticado via JWT, com isolamento total de dados entre usuários.

## O que essa versão resolve

A versão desktop tinha um problema estrutural: um único banco local, sem conceito de usuário. A API resolve isso com três garantias:

- **Multiusuário real**: toda transação e categoria pertence a um `Usuario`. A identificação do dono dos dados é feita diretamente pelo token JWT, não sendo necessário passar IDs nas rotas.
- **Segurança por Contexto**: O usuário autenticado só acessa e modifica os próprios dados, pois o e-mail de vínculo é extraído do `SecurityContextHolder` em cada requisição, eliminando riscos de manipulação de IDs na URL.
- **Documentação viva**: Swagger/OpenAPI gerado a partir do código, garantindo que o contrato da API nunca fique desatualizado em relação à implementação.

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem / Framework | Java 21 + Spring Boot 3 |
| Persistência | Spring Data JPA (Hibernate) |
| Banco de dados | H2 (arquivo local) |
| Segurança | Spring Security + JWT |
| Documentação | springdoc-openapi-starter-webmvc-ui |
| Build | Maven |

## Arquitetura

```text
Controller → Service → Repository → Entity
     ↓            ↓
    DTO     Regras de negócio
```

- **Controller**: recebe e devolve DTOs (`RequestDTO` e `ResponseDTO`), isolando a estrutura do banco de dados do contrato da API.
- **Service**: responsável pelas validações de negócio e pelo mapeamento entre DTOs e Entidades.
- **Repository**: interfaces `JpaRepository` para comunicação com o H2.

## Modelo de dados

- `Usuario`: id, nome, email (único), senha (hash BCrypt).
- `Categoria`: id, nome, tipo (RECEITA/DESPESA), usuario (dono — categorias são customizáveis por usuário).
- `Transacao`: id, descricao, valor, data, tipo (RECEITA/DESPESA), usuario, categoria.

## Endpoints principais

Prefixo de versionamento: `/api/v1`.

### Autenticação (públicos)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/auth/register` | Cria usuário |
| POST | `/api/auth/login` | Valida credenciais e retorna o token JWT |

### Transações (protegidos via JWT)

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/v1/transacoes` | Lista todas as transações do usuário autenticado |
| POST | `/api/v1/transacoes` | Cria uma nova transação |
| PUT | `/api/v1/transacoes/{id}` | Atualiza uma transação existente |
| DELETE | `/api/v1/transacoes/{id}` | Remove uma transação |

### Categorias (protegidos via JWT)

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/v1/categorias` | Lista as categorias do usuário autenticado |
| POST | `/api/v1/categorias` | Cria uma nova categoria |
| PUT | `/api/v1/categorias/{id}` | Atualiza uma categoria existente |
| DELETE | `/api/v1/categorias/{id}` | Remove uma categoria |

## Segurança

- O Login gera um JWT assinado. O e-mail do usuário fica armazenado no token, garantindo a identidade nas próximas requisições.
- O `SecurityFilterChain` bloqueia todas as rotas por padrão, liberando apenas `/api/auth/**` e as rotas de documentação (`/swagger-ui/**`, `/v3/api-docs/**`).
- Senhas são armazenadas utilizando hash BCrypt.

## Tratamento de erros

O `GlobalExceptionHandler` centraliza as respostas de erro da aplicação retornando um `ErroResponseDTO` padronizado. Exceções personalizadas implementadas:

| Exceção | Status HTTP |
|---|---|
| `UsuarioNaoEncontradoException` | 404 Not Found |
| `TransacaoNaoEncontradaException` | 404 Not Found |
| `UsuarioNaoAutorizadoException` | 403 Forbidden |
| `EmailJaCadastradoException` | 409 Conflict |

## Rodando localmente

```bash
git clone <url-do-repositorio>
cd fintrack-api

# Execute via Maven
./mvnw spring-boot:run
```

O console web do H2 pode ser acessado (se habilitado) em `http://localhost:8080/h2-console`. 

A documentação interativa da API via Swagger UI fica disponível em `http://localhost:8080/swagger-ui/index.html` após subir a aplicação. Nela, é possível testar todos os endpoints clicando em "Authorize" e inserindo o token JWT no formato `Bearer <token>`.

## Migração a partir da versão desktop

| FinTrack Desktop (JavaFX/JDBC) | FinTrack API (Spring Boot) |
|---|---|
| DAOs com JDBC manual | Repositories com Spring Data JPA |
| Banco único local | Entidade `Usuario`, isolamento total de dados |
| Lógica misturada nas telas | Separação em Controllers e Services via DTOs |
| Sem contrato formal | Swagger/OpenAPI gerado automaticamente |
| Sem sistema de login | Spring Security + Autenticação JWT |
