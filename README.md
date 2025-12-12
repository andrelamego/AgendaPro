<h1 align="center">AgendaPro</h1>
<h3 align="center">Sistema completo de agendamento online para profissionais e clientes</h3>


<p align="center">
  <!-- Stack -->
  <img src="https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Gradle-7+-02303A?logo=gradle&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-16-316192?logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/JWT-Security-black?logo=jsonwebtokens" />
  <img src="https://img.shields.io/badge/Swagger-OpenAPI%203-85EA2D?logo=swagger&logoColor=white" />
  <img src="https://img.shields.io/badge/Architecture-Clean%20%26%20Layered-8A2BE2" />
</p>


## 📌 Sobre o Projeto

O **AgendaPro** é uma API completa para gerenciamento de agendamentos entre profissionais e clientes.  
Foi desenvolvida com foco em **arquitetura limpa, segurança sólida, boas práticas e extensibilidade** — servindo de base para um sistema real de agenda online.


## 🚀 Funcionalidades

- ✅ Cadastro de clientes e profissionais
- ✅ Autenticação e autorização via JWT (stateless)
- ✅ Gestão de perfil do profissional (bio, telefone, aceitação de novos clientes, ativo)
- ✅ Cadastro e gestão de serviços (nome, descrição, duração, preço)
- ✅ Busca de profissionais ativos e por disponibilidade
- ✅ Agendamento, confirmação e cancelamento
- ✅ Listagens específicas para cliente e profissional
- ✅ CORS habilitado para integração com frontends em localhost
- ✅ Timezone configurado para `America/Sao_Paulo`

## 🛠️ Tecnologias Utilizadas

- Java 17+
- Spring Boot 3.x
  - Spring Web, Validation, Security, Data JPA
- PostgreSQL (produção/desenvolvimento)
- JJWT (io.jsonwebtoken) para tokens JWT
- BCrypt para hash de senha
- Lombok
- Gradle (Kotlin DSL)

## 📋 Pré‑requisitos

- Java 17+ instalado
- PostgreSQL 12+ instalado e em execução
- Gradle Wrapper (já incluso no projeto)

## ⚙️ Configuração

Edite `src/main/resources/application.properties` (recomenda‑se usar variáveis de ambiente em produção):

```
spring.datasource.url=jdbc:postgresql://localhost:5432/{DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.jackson.time-zone=America/Sao_Paulo

# JWT
security.jwt.secret=${JWT_SECRET}
security.jwt.expiration=${JWT_EXPIRATION_MS}
```

- Altere `JWT_SECRET` por uma chave forte (32–64 caracteres aleatórios).
- Ajuste o `datasource` conforme sua instalação do PostgreSQL.

### Banco de dados

- Crie o banco de dados `agendapro` (ou outro nome e ajuste a URL):
  - Database: `agendapro`
  - User: `agendapro_admin` (ou outro)
  - Password: defina uma senha segura

## ▶️ Como executar

1. Clone o repositório e entre na pasta do projeto.
2. Ajuste o `application.properties` conforme descrito acima.
3. Execute a aplicação:

```
./gradlew bootRun   # Linux/macOS
gradlew.bat bootRun # Windows
```

A aplicação inicia por padrão em `http://localhost:8080`.

## 🔐 Autenticação

O fluxo de autenticação utiliza JWT. Após o login, use o token no header `Authorization: Bearer <TOKEN>` em todas as rotas protegidas.

### Login

- Endpoint: `POST /api/auth/login`
- Body:

```
{
  "email": "usuario@exemplo.com",
  "senha": "123456"
}
```

- Resposta:

```
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

## 👤 Usuários

- Registrar cliente
  - `POST /api/users/register-cliente` (público)
  - Body:
    ```
    {
      "nome": "Fulano",
      "email": "fulano@exemplo.com",
      "senha": "123456",
      "telefone": "+55 11 99999-0000"
    }
    ```
  - Resposta: dados básicos do usuário criado.

- Dados do usuário autenticado
  - `GET /api/users/me` (requer JWT)

## 🧑‍⚕️ Profissionais

- Criar profissional (cadastro)
  - `POST /api/profissionais` (público)
  - Body:
    ```
    {
      "nome": "Dra. Ana",
      "email": "ana@clinica.com",
      "senha": "123456",
      "telefone": "+55 11 90000-0000",
      "bio": "Dermatologista",
      "aceitaNovosClientes": true
    }
    ```

- Atualizar meu perfil
  - `PUT /api/profissionais/me` (JWT)
  - Body (campos opcionais):
    ```
    {
      "nome": "Dra. Ana",
      "telefone": "+55 11 90000-0000",
      "bio": "Dermatologista e tricologista",
      "aceitaNovosClientes": true,
      "ativo": true
    }
    ```

- Meu perfil
  - `GET /api/profissionais/me` (JWT)

- Listar profissionais
  - `GET /api/profissionais?apenasAceitandoNovos=false` (público)

- Buscar por ID
  - `GET /api/profissionais/{id}` (público)

## 💼 Serviços

Rotas de criação/gestão exigem papel de profissional (vide regras de segurança).

- Criar serviço
  - `POST /api/servicos` (JWT: PROFISSIONAL)
  - Body:
    ```
    {
      "nome": "Consulta",
      "descricao": "Consulta inicial",
      "duracaoMinutos": 60,
      "preco": 200.0
    }
    ```

- Atualizar serviço
  - `PUT /api/servicos/{id}` (JWT: PROFISSIONAL)

- Inativar serviço
  - `DELETE /api/servicos/{id}` (JWT: PROFISSIONAL)

- Meus serviços
  - `GET /api/servicos/me` (JWT: PROFISSIONAL) — filtro opcional `?nome=`

- Listar serviços públicos de um profissional
  - `GET /api/servicos/profissional/{profissionalId}` (público)

- Detalhe de um serviço específico de um profissional
  - `GET /api/servicos/profissional/{profissionalId}/{servicoId}` (público)

## 🗓️ Agendamentos

- Criar agendamento (cliente autenticado)
  - `POST /api/agendamentos` (JWT)
  - Body:
    ```
    {
      "profissionalId": 1,
      "servicoId": 10,
      "dataHoraInicio": "2025-12-11T15:00:00",
      "observacoes": "Trazer exames"
    }
    ```

- Cancelar agendamento (cliente ou profissional)
  - `POST /api/agendamentos/{id}/cancelar` (JWT)
  - Body:
    ```
    { "motivo": "Imprevisto" }
    ```

- Confirmar agendamento (profissional)
  - `POST /api/agendamentos/{id}/confirmar` (JWT)

- Agenda do dia do profissional (autenticado como profissional)
  - `GET /api/agendamentos/me/profissional/dia/{data}` (JWT) — `data` no formato ISO `YYYY-MM-DD`

- Agendamentos futuros do cliente autenticado
  - `GET /api/agendamentos/me/cliente/futuros` (JWT)

- Horários disponíveis para um profissional/serviço em um dia
  - `GET /api/agendamentos/profissional/{id}/horarios-disponiveis?servicoId={sid}&data=YYYY-MM-DD` (público)

## 🕒 Disponibilidades (Profissional)

- Adicionar disponibilidade
  - `POST /api/disponibilidades` (JWT: PROFISSIONAL)
  - Body:
    ```
    {
      "diaSemana": "MONDAY",
      "horaInicio": "09:00",
      "horaFim": "18:00"
    }
    ```

- Remover disponibilidade
  - `DELETE /api/disponibilidades/{id}` (JWT: PROFISSIONAL)

- Minhas disponibilidades
  - `GET /api/disponibilidades/me` (JWT: PROFISSIONAL)

## 🔒 Segurança e CORS

- Stateless JWT via `Authorization: Bearer <token>`
- Endpoints públicos: login, registro de cliente, listagens públicas de profissionais e GET de serviços
- Demais rotas exigem autenticação
- CORS liberado por padrão para:
  - `http://localhost:5173`
  - `http://localhost:3000`
  - `http://localhost:8080`

## 📄 Documentação (Swagger/OpenAPI)

Se configurado o SpringDoc, acesse após subir a aplicação:

- JSON: `GET /v3/api-docs`
- UI: `GET /swagger-ui.html` ou `/swagger-ui/`

## 🧭 Estrutura e referências

- Controladores (endpoints): `src/main/java/com/lamego/agendapro/controller/`
- Segurança/JWT: `src/main/java/com/lamego/agendapro/security/`
- Serviços (regras de negócio): `src/main/java/com/lamego/agendapro/service/`
- Modelos e Repositórios: `src/main/java/com/lamego/agendapro/domain` e `.../repository`
- Propriedades: `src/main/resources/application.properties`
- Diagramas UML: `docs/uml/*.puml`

## ❗ Dicas e erros comuns

- 401 Unauthorized: verifique o header `Authorization: Bearer <token>`
- 403 Forbidden: usuário sem permissão (ex.: endpoint exclusivo de profissional)
- 400 Bad Request: valide formatos de data (`YYYY-MM-DD` / `YYYY-MM-DDTHH:MM:SS`), campos obrigatórios e tamanhos
- Conexão com DB: confira URL, usuário e senha do PostgreSQL
- Não versionar segredos: use variáveis de ambiente para `DB_PASSWORD` e `JWT_SECRET`
