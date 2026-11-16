# Clínica Médica Completa

API REST para apoiar o gerenciamento de uma clínica médica. O projeto reúne cadastro e autenticação de usuários, pacientes, médicos, especialidades, convênios, consultas, pagamentos e registros clínicos.

## Índice

- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Requisitos](#requisitos)
- [Configuração do application.properties](#configuração-do-applicationproperties)
- [Execução](#execução)
- [Execução com Docker](#execução-com-docker)
- [Autenticação](#autenticação)
- [Rotas da API](#rotas-da-api)
- [Fluxo recomendado](#fluxo-recomendado)
- [Paginação e respostas](#paginação-e-respostas)
- [Testes](#testes)
- [Estrutura do projeto](#estrutura-do-projeto)

## Funcionalidades

- Cadastro, login, atualização e desativação de contas.
- Autenticação stateless com access token JWT e refresh token.
- Cadastro de médicos, pacientes, especialidades e convênios.
- Agendamento e gerenciamento de consultas.
- Registro de pagamentos e respectivos métodos e estados.
- Prontuários, exames e receitas associados às consultas.
- Endereços associados a usuários.
- Validação de entrada com Jakarta Bean Validation.

## Tecnologias

- Java 21
- Spring Boot 4.1.2
- Spring MVC, Spring Security, Spring Data JPA e Hibernate
- JWT (JJWT e Java JWT)
- Maven Wrapper
- Docker e Docker Compose

## Requisitos

- JDK 21
- Docker e Docker Compose (para executar com containers)
- Acesso às variáveis de ambiente descritas abaixo
- Git (opcional, para clonar o repositório)

## Configuração do application.properties

O arquivo `src/main/resources/application.properties` importa opcionalmente um arquivo `.env` no formato de propriedades e referencia três variáveis de ambiente:

| Variável | Uso | Obrigatória |
|---|---|---|
| `JWT_SECRET` | Segredo usado para assinar e validar os tokens JWT | Sim |
| `DB_USER` | Usuário utilizado na conexão configurada para a aplicação | Não; padrão `clinica` |
| `DB_PASSWORD` | Senha utilizada na conexão configurada para a aplicação | Sim |
| `DB_ROOT_PASSWORD` | Senha do administrador do MySQL; usada somente pelo Docker Compose | Somente com Docker |

Exemplo de `.env` na raiz do projeto:

```properties
JWT_SECRET=coloque-um-segredo-aleatorio-com-pelo-menos-32-bytes
DB_USER=clinica
DB_PASSWORD=sua-senha
DB_ROOT_PASSWORD=sua-senha-root
```

O arquivo `.env` é opcional. As mesmas variáveis podem ser fornecidas pelo ambiente de execução. `JWT_SECRET` deve ter pelo menos 32 bytes para a chave HMAC usada pelo serviço de tokens. Não compartilhe nem versione os valores reais.

O `application.properties` também define os prazos dos tokens: access token de 1 hora e refresh token de 15 dias.

## Execução

Na raiz do projeto:

```bash
./mvnw spring-boot:run
```

No Windows PowerShell:

```powershell
$env:DB_USER = "clinica"
$env:DB_PASSWORD = "sua-senha-local"
$env:JWT_SECRET = "chave-secreta-32-bytes"
.\mvnw.cmd spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

Para compilar e empacotar:

```bash
./mvnw clean package
```

## Execução com Docker

O `Dockerfile` e o `compose.yaml` sobem a API e o MySQL juntos. Com o `.env` criado na raiz.

```bash
docker compose up --build
```

A API ficará disponível em `http://localhost:8080`. Para parar:

```bash
docker compose down     
docker compose down -v  
```

## Autenticação

As rotas protegidas recebem o access token no cabeçalho:

```http
Authorization: Bearer SEU_ACCESS_TOKEN
```

### Registrar usuário

`POST /usuarios/registrar` é público e cria um usuário com papel `USER`.

```bash
curl -X POST http://localhost:8080/usuarios/registrar \
  -H 'Content-Type: application/json' \
  -d '{
    "nome": "Ana Clara Dias",
    "cpf": "12345678901",
    "email": "ana@example.com",
    "password": "uma-senha-forte"
  }'
```

### Fazer login

`POST /usuarios/logar` é público e recebe um `UsuarioRequest` com os campos `nome`, `cpf`, `email` e `password`:

```bash
curl -X POST http://localhost:8080/usuarios/logar \
  -H 'Content-Type: application/json' \
  -d '{
    "nome": "Ana Clara Dias",
    "cpf": "12345678901",
    "email": "ana@example.com",
    "password": "uma-senha-forte"
  }'
```

A resposta contém `accessToken` e `refreshToken`. Use o access token nas rotas protegidas.

### Renovar token

`POST /usuarios/refresh` recebe:

```json
{
  "refreshToken": "SEU_REFRESH_TOKEN"
}
```

O access token tem validade configurada de 1 hora; o refresh token usa a configuração de 15 dias.

## Rotas da API

Não há prefixo global de versão. `{id}` é o identificador do recurso: pode ser UUID ou número, conforme a entidade.

| Recurso | Método e rota | Acesso |
|---|---|---|
| Usuário | `POST /usuarios/registrar` | Público |
| Usuário | `POST /usuarios/logar` | Público |
| Usuário | `POST /usuarios/refresh` | Público |
| Usuário | `GET /usuarios/listar` | Autenticado; retorna o perfil atual |
| Usuário | `PATCH /usuarios/atualizar/{id}` | Autenticado; o serviço valida a conta do usuário |
| Usuário | `DELETE /usuarios/deletar/{id}` | Autenticado; o serviço valida a conta do usuário |
| Endereço | `GET /enderecos/listar` | Autenticado; lista endereços do usuário atual |
| Endereço | `POST /enderecos/criar` | Autenticado |
| Endereço | `PATCH /enderecos/atualizar/{id}` | Autenticado; o serviço verifica o proprietário |
| Endereço | `DELETE /enderecos/deletar/{id}` | Autenticado; o serviço verifica o proprietário |
| Médico | `GET /medicos/listar` | `ADMIN` |
| Médico | `POST /medicos/criar` | `ADMIN` |
| Médico | `PATCH /medicos/atualizar/{id}` | Autenticado; o serviço verifica o médico vinculado ao usuário |
| Médico | `DELETE /medicos/deletar/{id}` | `ADMIN` |
| Especialidade | `GET /especialidades/listar` | `ADMIN` |
| Especialidade | `POST /especialidades/criar` | `ADMIN` |
| Especialidade | `PATCH /especialidades/atualizar/{id}` | `ADMIN` |
| Especialidade | `DELETE /especialidades/desativar/{id}` | `ADMIN` |
| Convênio | `GET /convenios/listar` | `ADMIN` |
| Convênio | `POST /convenios/criar` | `ADMIN` |
| Convênio | `PATCH /convenios/atualizar/{id}` | `ADMIN` |
| Convênio | `DELETE /convenios/desativar/{id}` | `ADMIN` |
| Paciente (administração) | `GET /pacientes/listar` | `ADMIN` |
| Paciente (administração) | `POST /pacientes/cadastrar` | `ADMIN` |
| Paciente (administração) | `PATCH /pacientes/atualizar/{id}` | `ADMIN` |
| Paciente (administração) | `DELETE /pacientes/deletar/{id}` | `ADMIN` |
| Paciente (próprio cadastro) | `GET /pacientes/visualizar` | Autenticado |
| Paciente (próprio cadastro) | `POST /pacientes/cadastro` | Autenticado; o serviço confere o usuário do token |
| Paciente (próprio cadastro) | `PATCH /pacientes/atualizarcadastro/{id}` | Autenticado; o serviço confere o usuário do token |
| Paciente (próprio cadastro) | `DELETE /pacientes/deletarcadastro/{id}` | Autenticado; o serviço confere o usuário do token |
| Consulta | `GET /consulta/listar` | `ADMIN` |
| Consulta | `POST /consulta/adicionar` | `ADMIN` |
| Consulta | `PATCH /consulta/atualizar/{id}` | `ADMIN` |
| Consulta | `DELETE /consulta/deletar/{id}` | `ADMIN` |
| Pagamento | `GET /pagamentos/listar` | Autenticado; dados filtrados pelo paciente do usuário atual |
| Pagamento | `POST /pagamentos/adicionar` | Autenticado; o serviço verifica o paciente da consulta |
| Pagamento | `PATCH /pagamentos/atualizar/{id}` | Autenticado; o serviço verifica o proprietário |
| Pagamento | `DELETE /pagamentos/deletar/{id}` | Autenticado; o serviço verifica o proprietário |
| Exame | `GET /exames/listar` | `ADMIN` |
| Exame | `POST /exames/adicionar` | `ADMIN` |
| Exame | `PATCH /exames/atualizar/{id}` | `ADMIN` |
| Exame | `DELETE /exames/deletar/{id}` | `ADMIN` |
| Prontuário | `GET /prontuarios/listar` | `ADMIN` |
| Prontuário | `POST /prontuarios/criar` | `ADMIN` |
| Prontuário | `PATCH /prontuarios/atualizar/{id}` | `ADMIN` |
| Prontuário | `DELETE /prontuarios/deletar/{id}` | `ADMIN` |
| Receita | `GET /receitas/listar` | `ADMIN` |
| Receita | `POST /receitas/criar` | `ADMIN` |
| Receita | `PATCH /receitas/atualizar/{id}` | `ADMIN` |
| Receita | `DELETE /receitas/deletar/{id}` | `ADMIN` |

O papel `ADMIN` é exigido em várias operações, mas a API não fornece endpoint público para concedê-lo. O provisionamento inicial deve ser feito por um procedimento administrativo controlado.

## Fluxo recomendado

A criação de recursos clínicos depende de registros relacionados. Um fluxo típico é:

1. Registrar e autenticar usuários.
2. Com uma conta `ADMIN`, criar convênios e especialidades.
3. Cadastrar um paciente associado a um usuário e a um convênio.
4. Cadastrar um médico associado a um usuário e a uma especialidade.
5. Criar uma consulta com paciente, médico, data/hora, status e valor.
6. Criar o prontuário da consulta.
7. Associar exames e receitas ao prontuário.
8. Registrar pagamentos para a consulta do próprio paciente.

Exemplo de corpo para consulta:

```json
{
  "pacienteId": "UUID-DO-PACIENTE",
  "medicoId": 1,
  "dataHora": "2026-12-10T14:30:00",
  "status": "AGENDADO",
  "valor": 250.00
}
```

`dataHora` usa formato ISO-8601 de data e hora local (`LocalDateTime`), sem sufixo de fuso horário.

### Valores enum aceitos

- `StatusConsulta`: `AGENDADO`, `CONFIRMADO`, `REALIZADA`, `CANCELADA`
- `MetodoPagamento`: `PIX`, `CARTAO_CREDITO`, `CARTAO_DEBITO`, `DINHEIRO`, `CONVENIO`
- `StatusPagamento` é definido pelo sistema; o request de pagamento envia apenas `consultaId` e `metodo`.

## Paginação e respostas

As listagens paginadas aceitam os parâmetros do Spring Data, por exemplo:

```text
GET /medicos/listar?page=0&size=20&sort=id,asc
```

As operações de criação normalmente retornam `201 Created`, consultas bem-sucedidas retornam `200 OK` e exclusões retornam `204 No Content`.

## Testes

Com as variáveis exigidas pelo `application.properties` disponíveis no ambiente, execute:

```bash
./mvnw test
```

## Estrutura do projeto

```text
Dockerfile        # Imagem da API
compose.yaml      # API + MySQL

src/main/java/.../clinica/
├── config/       # Segurança HTTP e filtro JWT
├── controller/   # Endpoints REST
├── domain/       # Entidades JPA e enums
├── dto/          # Contratos de entrada e saída
├── mapper/       # Conversão entre entidades e DTOs
├── repository/   # Persistência Spring Data JPA
└── service/      # Regras de negócio

src/main/resources/
└── application.properties

src/test/java/    # Testes automatizados
```

## Autor
**João Vitor de Jesus Alves** · [GitHub](https://github.com/JoaoVitorAlves-dev).