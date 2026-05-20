# BarberFlow

API REST para gerenciamento de barbearia com landing page integrada.

## Nome e descricao do projeto

- **Nome:** BarberFlow
- **Descricao:** plataforma academica para cadastro de clientes e barbeiros, gestao de horarios disponiveis e ciclo completo de agendamentos (criar, cancelar e concluir), com sistema de pontos para priorizacao de horarios.

## Integrantes do grupo

- Vinicius de Sousa Silva - CRUD de Cliente
- Matheus Guilherme de Paula - CRUD de Barbeiro
- Joao Felipe e Tainon Coutinho - feature de Agendamento
- Igor Gustavo - feature de HorariosDisponiveis
- Emanuel Vinicius e Pedro Henrique - Frontend

## Problema (dor), contexto e motivacao

Barbearias pequenas normalmente controlam agenda por mensagens, caderno ou planilhas, o que gera conflitos de horario, retrabalho e pouca visibilidade da rotina dos profissionais.  
No contexto da disciplina de Programacao II, o projeto foi motivado pela necessidade de aplicar modelagem orientada a objetos, persistencia relacional e API REST em um dominio real de servicos.

## Solucao proposta

O BarberFlow centraliza o fluxo de agendamento em uma API REST com regras de negocio para:

- cadastro e consulta de clientes
- cadastro e gerenciamento de barbeiros
- cadastro de horarios disponiveis por barbeiro
- agendamento com validacao de disponibilidade
- cancelamento por cliente ou barbeiro
- conclusao de servico com acumulacao de pontos do cliente
- priorizacao de exibicao de horarios baseada em `agendamentoPoints`

## Features entregues ate o momento da submissao

- CRUD completo de clientes
- CRUD de barbeiros com remocao permanente
- cadastro e listagem de horarios disponiveis
- criacao, cancelamento e conclusao de agendamentos
- validacoes de telefone/senha e regras de conflito de horario
- ranking de clientes por pontos na landing page
- migracoes Flyway em schema final (versionamento de banco)
- documentacao de endpoints via Swagger/OpenAPI

## Tecnologias utilizadas (com versoes)

- Java 21
- Spring Boot `4.0.6`
- Spring Web MVC `4.0.6` (starter gerenciado pelo Spring Boot)
- Spring Data JPA `4.0.6` (starter gerenciado pelo Spring Boot)
- Flyway starter `4.0.6` + `flyway-database-postgresql 11.14.1`
- PostgreSQL JDBC Driver `42.7.10`
- Lombok `1.18.46`
- OpenAPI/Swagger (`springdoc-openapi-starter-webmvc-ui 2.7.0`)
- Maven Wrapper (`mvnw`, `mvnw.cmd`)

## Quadro Kanban

- Trello do projeto (atividades descritas e atribuidas): [Projeto Programacao II](https://trello.com/b/HT9QjLoH/projeto-programacao-ii)

## Requisitos para executar localmente

- JDK 21 instalado
- PostgreSQL ativo

## Variaveis de ambiente necessarias

Nao ha variavel obrigatoria se voce optar por configurar diretamente em `src/main/resources/application.properties`.  
Se preferir, pode usar variaveis de ambiente padrao do Spring Boot:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

Exemplo equivalente no `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/BarberFlow
spring.datasource.username=postgres
spring.datasource.password=SuaSenhaDoPostgres
```

## Como executar o projeto localmente (passo a passo)

1. Abra o terminal na raiz do projeto.
2. Crie o banco no PostgreSQL:
   - nome do banco: `BarberFlow`
3. Configure credenciais de banco:
   - via `application.properties` **ou** via variaveis de ambiente.
4. Execute a aplicacao:
   - Windows:
     ```powershell
     .\mvnw.cmd clean spring-boot:run
     ```
   - Linux/macOS:
     ```bash
     ./mvnw clean spring-boot:run
     ```
5. Acesse:
   - landing page: `http://localhost:8080/`
   - API base: `http://localhost:8080`
6. As migrations Flyway em `src/main/resources/db/migration` rodam automaticamente na inicializacao.

## Testes

```powershell
.\mvnw.cmd test
```

## Swagger / OpenAPI

Com a aplicacao rodando:

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/swagger-ui/index.html`
- `http://localhost:8080/v3/api-docs`

## Colecao Postman

- `docs/BarberFlow-Funcionalidades.postman_collection.json`
- Variavel principal configurada: `{{base_url}} = http://localhost:8080`
- Requests organizadas por recurso: `Barbeiros`, `Clientes`, `Horarios` e `Agendamentos`

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
- `DELETE /barbeiros/{id}` (remocao permanente)

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
  controller/       # endpoints REST
  service/          # regras de negocio
  repository/       # acesso a dados (JPA)
  entity/           # entidades
  dto/              # contratos de entrada e saida

src/main/resources
  db/migration/     # scripts Flyway
  static/           # landing page (HTML/CSS/JS)
```
