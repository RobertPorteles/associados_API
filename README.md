# Gerenciamento de Associados

Este é um projeto Spring Boot para o gerenciamento de associados, equipes e outras entidades relacionadas. Ele fornece uma API RESTful para realizar operações CRUD e outras lógicas de negócio.

## Pré-requisitos

-   Java 21
-   Maven 3.x
-   PostgreSQL

## Como Executar

1.  **Clone o repositório:**
    ```bash
    git clone <url-do-repositorio>
    cd gerenciamento
    ```

2.  **Configure o banco de dados:**
    -   Certifique-se de ter o PostgreSQL em execução.
    -   Abra o arquivo `src/main/resources/application.properties` e configure as propriedades do banco de dados:
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/seu-banco-de-dados
        spring.datasource.username=seu-usuario
        spring.datasource.password=sua-senha
        spring.jpa.hibernate.ddl-auto=update
        ```

3.  **Execute a aplicação:**
    ```bash
    ./mvnw spring-boot:run
    ```
    A aplicação estará disponível em `http://localhost:8080`.

## Documentação da API

A documentação da API está disponível via Swagger UI. Após iniciar a aplicação, você pode acessá-la em:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Endpoints Principais

-   `/api/v1/associados`: Gerenciamento de associados.
-   `/api/v1/equipes`: Gerenciamento de equipes.
-   `/api/v1/cargos-lideranca`: Gerenciamento de cargos de liderança.
-   `/api/v1/auth`: Autenticação e registro de usuários.

---
*Este README foi gerado pela IA.*
