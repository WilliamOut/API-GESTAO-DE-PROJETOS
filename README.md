# 📋 Gerenciador de Projetos e Tarefas API

API REST desenvolvida com **Spring Boot** para o gerenciamento de projetos e suas respectivas tarefas. O sistema permite criar projetos, associar tarefas a eles, filtrar listagens e gerenciar o ciclo de vida das atividades.

---

### 🚀 Tecnologias Utilizadas

* **Java 21**
* **Spring Boot 3**
* **Spring Data JPA** (Persistência de dados)
* **H2 Database** (Banco de dados em memória para desenvolvimento)
* **MapStruct** (Mapeamento inteligente entre DTOs e Entidades)
* **Bean Validation** (Validação de dados de entrada)
* **JUnit 5 & Mockito** (Testes Unitários)
* **Maven** (Gerenciamento de dependências)

---

### ⚙️ Pré-requisitos

* **JDK 21** instalado.
* **Maven** instalado (ou utilizar o wrapper `./mvnw` do projeto).

---

### 🛠️ Como Rodar a Aplicação

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/WilliamOut/API-GESTAO-DE-PROJETOS.git
    cd API-GESTAO-DE-PROJETOS
    ```

2.  **Compile o projeto e gere os Mappers (MapStruct):**
    É essencial rodar este comando para que o MapStruct gere as implementações das interfaces.
    ```bash
    mvn clean install
    ```

3.  **Execute a aplicação:**
    ```bash
    mvn spring-boot:run
    ```

A aplicação estará rodando em: `http://localhost:8080`\
A documentação Swagger estará rodando em: `http://localhost:8080/swagger-ui/index.html`
---

### 📚 Documentação da API (Endpoints)

#### 📂 Projetos (`/projects`)

**1. Criar Projeto**
* **URL:** `POST /projects`
* **Body:**
    ```json
    {
      "name": "Desenvolvimento API",
      "description": "Projeto para criar uma API Rest completa",
      "startDate": "2024-01-01",
      "endDate": "2024-12-31"
    }
    ```

**2. Listar Projetos**
* **URL:** `GET /projects`
* **Resposta:** Lista de todos os projetos cadastrados.

---

#### 📝 Tarefas (`/tasks`)

**1. Criar Tarefa**
* **URL:** `POST /tasks`
* **Body:** (O campo `idProject` deve ser o ID de um projeto existente)
    ```json
    {
      "title": "Implementar Controller",
      "description": "Criar endpoints REST",
      "status": "TODO",
      "priority": "HIGH",
      "dueDate": "2024-02-15",
      "idProject": 1
    }
    ```

**2. Listar Tarefas (Com Filtros Opcionais)**
* **URL:** `GET /tasks`
* **Parâmetros (Query Params):**
    * `?status=TODO` (Opcional)
    * `?priority=HIGH` (Opcional)
    * `?idProject=1` (Opcional)
* **Exemplo:** `GET /tasks?status=DOING&idProject=1`

**3. Atualizar Status da Tarefa**
* **URL:** `PATCH /tasks/{id}/status`
* **Body:**
    ```json
    {
      "status": "DONE"
    }
    ```

**4. Deletar Tarefa**
* **URL:** `DELETE /tasks/{id}`
* **Resposta:** `204 No Content`

---

### 💾 Banco de Dados (H2 Console)

Como a aplicação utiliza o H2 (banco em memória), você pode acessar o console do banco de dados pelo navegador enquanto a aplicação estiver rodando.

* **URL:** `http://localhost:8080/h2-console`
* **Driver Class:** `org.h2.Driver`
* **JDBC URL:** `jdbc:h2:mem:testdb` (ou verifique o `application.properties` se alterou o nome)
* **User Name:** `sa`
* **Password:** (Geralmente vazio ou `password`, verifique o `application.properties`)

---

### ✅ Como Rodar os Testes

Para executar os testes unitários (Service e Mappers) e verificar a integridade da aplicação:

```bash
mvn test
```

### 🛠️ Tags
`#Java` `#SpringBoot` `#Backend` `#DesafioTecnico`  
`#API` `#RestAPI` `#H2` `#Oracle` `#JPA` `#Swagger`  
`#CleanCode` `#SoftwareEngineering`