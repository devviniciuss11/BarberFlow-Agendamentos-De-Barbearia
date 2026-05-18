# BarberFlow

API REST para gerenciamento de barbearia com landing page integrada.

## Contexto academico e equipe

O BarberFlow e um projeto academico da disciplina de Programacao 2 e foi desenvolvido em grupo.

Integrantes do grupo e responsabilidades:
- Vinicius de Sousa Silva: responsavel pelo CRUD de Cliente.
- Matheus Guilherme de Paula: responsavel pelo CRUD de Barbeiro.
- Joao Felipe e Tainon Coutinho: responsaveis pela feature de Agendamento.
- Igor Gustavo: responsavel pela feature de HorariosDisponiveis.
- Emanuel Vinicius e Pedro Henrique: responsaveis pelo Frontend.

O projeto permite:
- cadastro e gestao de clientes
- cadastro e gestao de barbeiros
- cadastro e consulta de horarios disponiveis
- criacao, cancelamento e conclusao de agendamentos
- sistema de pontos (`agendamentoPoints`) para ranking e prioridade de exibicao de horarios

## Tecnologias

- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- Flyway
- PostgreSQL
- Lombok
- OpenAPI/Swagger (springdoc)
- Maven Wrapper (`mvnw`, `mvnw.cmd`)

## Requisitos

- JDK 21
- PostgreSQL ativo

## Configuracao do banco

O projeto usa as propriedades em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/BarberFlow
spring.datasource.username=postgres
spring.datasource.password=2006
```

Antes de subir a API:
1. Crie o banco `BarberFlow` no PostgreSQL.
2. Ajuste `username` e `password` para seu ambiente, se necessario.

As migrations Flyway em `src/main/resources/db/migration` sao executadas automaticamente na inicializacao.

## Como executar

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

Aplicacao (landing page): `http://localhost:8080/`  
API base: `http://localhost:8080`

## Testes

```powershell
.\mvnw.cmd test
```

## Swagger / OpenAPI

Com a aplicacao rodando, acesse:

- `http://localhost:8080/swagger-ui/index.html`

## Colecao Postman

Colecao disponivel em:

- `docs/BarberFlow.postman_collection.json`

## Endpoints principais

### Clientes (`/clientes`)

- `POST /clientes`
- `GET /clientes`
- `GET /clientes/{id}`
- `PUT /clientes/{id}`
- `DELETE /clientes/{id}`

### Barbeiros (`/barbeiros`)

- `POST /barbeiros`
- `GET /barbeiros`
- `GET /barbeiros/{id}`
- `PUT /barbeiros/{id}`
- `DELETE /barbeiros/{id}` (inativacao logica)

### Horarios (`/Horarios`)

- `POST /Horarios`
- `GET /Horarios/disponiveis?barbeiroId={id}&clienteId={id}&data=yyyy-MM-dd`

### Agendamentos (`/agendamentos`)

- `POST /agendamentos`
- `GET /agendamentos/{id}`
- `GET /agendamentos/barbeiro/buscar?telefoneBarbeiro={telefone}`
- `GET /agendamentos/barbeiro/listar?telefoneBarbeiro={telefone}`
- `PATCH /agendamentos/{id}/cancelar/cliente?senhaCliente={senha}`
- `PATCH /agendamentos/{id}/cancelar/barbeiro?senhaBarbeiro={senha}`
- `PATCH /agendamentos/{id}/concluir?senhaBarbeiro={senha}`

## Regras de negocio importantes

- Telefone de cliente e barbeiro deve conter apenas numeros.
- Ao concluir servico, o cliente recebe `+1` em `agendamentoPoints`.
- A listagem de agendamentos ativos do barbeiro (`/agendamentos/barbeiro/listar`) nao mostra agendamentos concluidos.
- A listagem de horarios disponiveis depende do `agendamentoPoints` do cliente:
  - `0 pontos`: mostra ate `3` horarios
  - `1 a 4 pontos`: mostra ate `5` horarios
  - `5 a 9 pontos`: mostra ate `8` horarios
  - `10+ pontos`: mostra todos os horarios disponiveis

## Estrutura do projeto

```text
src/main/java/com/devflows/barberflow
  controlers/      # endpoints REST
  service/         # regras de negocio
  repositorys/     # acesso a dados (JPA)
  entity/          # entidades
  dto/             # contratos de entrada e saida

src/main/resources
  db/migration/    # scripts Flyway
  static/          # landing page (HTML/CSS/JS)
```

## Status

Projeto funcional para uso local e estudos, com API Rest + landing page.
