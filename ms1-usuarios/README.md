# MS1 – Usuários API

Microserviço de CRUD completo de usuários construído com **Spring Boot 3** e **Arquitetura Hexagonal (Ports & Adapters)**.

---

## 📐 Arquitetura Hexagonal

```
┌──────────────────────────────────────────────────────────────────┐
│                        ADAPTADORES PRIMÁRIOS                     │
│   UserController  ·  AuthController  ·  CsvImportController      │
└────────────────────────────┬─────────────────────────────────────┘
                             │  usa porta primária (in)
┌────────────────────────────▼─────────────────────────────────────┐
│                        HEXÁGONO (CORE)                            │
│                                                                   │
│   Port In:   UserServicePort · AuthServicePort · CsvImportPort    │
│   Service:   UserService · AuthService · CsvImportService         │
│   Domain:    User                                                 │
│   Port Out:  UserRepositoryPort                                   │
└────────────────────────────┬─────────────────────────────────────┘
                             │  implementa porta secundária (out)
┌────────────────────────────▼─────────────────────────────────────┐
│                      ADAPTADORES SECUNDÁRIOS                      │
│   UserRepositoryAdapter  ·  UserJpaRepository (Spring Data)       │
│   PostgreSQL                                                      │
└──────────────────────────────────────────────────────────────────┘
```

### Estrutura de Pacotes

```
src/main/java/com/seupacote/ms1/
├── adapter/
│   ├── in/controller/          ← REST (adaptadores primários)
│   │   ├── dto/                ← DTOs de entrada e saída
│   │   ├── UserController.java
│   │   ├── AuthController.java
│   │   └── CsvImportController.java
│   └── out/persistence/        ← JPA (adaptadores secundários)
│       ├── UserJpaRepository.java
│       └── UserRepositoryAdapter.java
├── core/
│   ├── domain/User.java        ← Entidade de domínio
│   ├── port/
│   │   ├── in/                 ← Portas primárias (contratos de caso de uso)
│   │   └── out/                ← Portas secundárias (contrato de persistência)
│   └── service/                ← Implementações dos casos de uso
└── configuration/
    ├── security/               ← JwtUtil + JwtFilter
    ├── SecurityConfig.java
    ├── OpenApiConfig.java
    ├── GlobalExceptionHandler.java
    └── exception/
```

---

## 🚀 Como Rodar

### Pré-requisitos

- Java 17+
- Maven 3.9+
- Docker + Docker Compose

### 1. Configurar variáveis de ambiente

```bash
cp .env.example .env
# Edite o .env com suas credenciais
```

### 2. Subir o banco com Docker Compose

```bash
# Apenas o PostgreSQL (desenvolvimento local)
docker compose up postgres -d
```

### 3. Rodar a aplicação

```bash
# Com Maven (lê o .env automaticamente via application.properties)
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8081`

### 4. Subir tudo com Docker (aplicação + banco)

```bash
# Build e subida completa
docker compose up --build -d

# Verificar logs
docker compose logs -f ms1-usuarios
```

---

## 📋 Endpoints

| Método | Endpoint       | Auth | Descrição                      |
|--------|----------------|------|--------------------------------|
| POST   | /auth/login    | ❌   | Login — retorna token JWT      |
| POST   | /users         | ❌   | Cria novo usuário              |
| GET    | /users         | ✅   | Lista todos os usuários        |
| GET    | /users/{id}    | ✅   | Busca usuário por ID           |
| PUT    | /users/{id}    | ✅   | Atualiza dados do usuário      |
| DELETE | /users/{id}    | ✅   | Remove usuário                 |
| POST   | /users/csv     | ✅   | Importa usuários via CSV       |

### Fluxo de autenticação

```bash
# 1. Criar usuário
curl -X POST http://localhost:8081/users \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva","email":"joao@email.com","senha":"minhasenha"}'

# 2. Fazer login e pegar token
TOKEN=$(curl -s -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@email.com","senha":"minhasenha"}' | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

# 3. Usar o token nos demais endpoints
curl -H "Authorization: Bearer $TOKEN" http://localhost:8081/users
```

### Importar CSV

```bash
curl -X POST http://localhost:8081/users/csv \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@src/main/resources/csv-exemplo.csv"
```

---

## 📖 Documentação Swagger

Acesse: **http://localhost:8081/swagger-ui.html**

1. Crie um usuário via `POST /users`
2. Faça login via `POST /auth/login` e copie o token
3. Clique em **Authorize** → Cole `Bearer <token>`
4. Explore todos os endpoints

---

## 🧪 Testes

```bash
# Rodar todos os testes
mvn test

# Com relatório de cobertura (opcional, adicionar JaCoCo ao pom.xml)
mvn verify
```

Testes incluídos:
- `UserServiceTest` — 8 cenários (criar, listar, buscar, atualizar, deletar)
- `AuthServiceTest` — 3 cenários (login válido, e-mail inválido, senha errada)

---

## 🔒 Segurança

- Senhas armazenadas com **BCrypt** (fator 10)
- Autenticação via **JWT** (HS256, expiração configurável)
- API stateless — sem sessão no servidor
- `.env` no `.gitignore` — credenciais nunca vão ao repositório

---

## ⚙️ Variáveis de Ambiente

| Variável       | Padrão             | Descrição                          |
|----------------|--------------------|------------------------------------|
| `DB_HOST`      | `localhost`        | Host do PostgreSQL                 |
| `DB_PORT`      | `5432`             | Porta do PostgreSQL                |
| `DB_NAME`      | `ms1db`            | Nome do banco                      |
| `DB_USER`      | `postgres`         | Usuário do banco                   |
| `DB_PASSWORD`  | —                  | Senha do banco (**obrigatório**)   |
| `JWT_SECRET`   | —                  | Chave JWT, mín. 32 chars (**obrigatório**) |
| `JWT_EXPIRATION_MS` | `86400000`    | Expiração do token (24h em ms)     |

---

## 🛠️ Tecnologias

| Tecnologia               | Versão   | Uso                              |
|--------------------------|----------|----------------------------------|
| Spring Boot              | 3.2.5    | Framework principal              |
| Spring Data JPA          | —        | Persistência                     |
| Spring Security          | —        | Autenticação + autorização       |
| PostgreSQL               | 15       | Banco de dados                   |
| jjwt                     | 0.12.3   | Geração/validação de JWT         |
| SpringDoc OpenAPI        | 2.3.0    | Swagger UI                       |
| Apache Commons CSV       | 1.10.0   | Parsing de CSV                   |
| JUnit 5 + Mockito        | —        | Testes unitários                 |
| Docker + Compose         | —        | Containerização                  |
| Java                     | 17       | Linguagem                        |

