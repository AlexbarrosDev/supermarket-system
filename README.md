# Supermarket System

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.14-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-success)

---

## 📖 Sobre o projeto

O **Supermarket System** é uma API REST desenvolvida em **Java** utilizando **Spring Boot**, inspirada em um sistema de gerenciamento de supermercado.

O projeto foi criado com foco em aprendizado prático e aplicação de boas práticas de desenvolvimento back-end, utilizando arquitetura em camadas, DTOs, mapeamento entre objetos, tratamento global de exceções, migrações de banco de dados e regras de negócio.

Além das funcionalidades de CRUD, o sistema implementa validações para garantir a integridade dos dados e refletir cenários encontrados em aplicações reais.

---

## 📌 Principais funcionalidades

- Cadastro de produtos
- Cadastro de categorias
- Cadastro de grupos
- Cadastro de clientes
- Cadastro de endereços
- Registro de vendas
- Registro de itens da venda
- Alteração de status de clientes
- Alteração de status de produtos
- Alteração de status das vendas
- Validações de regras de negócio
- Tratamento global de exceções
- Documentação automática da API

---

## 🛠 Tecnologias

- Java 21
- Spring Boot 3.5
- Spring Data JPA
- Hibernate
- MySQL
- Flyway
- MapStruct
- Lombok
- Spring Validation
- SpringDoc OpenAPI (Swagger)
- Maven

---

## 📂 Arquitetura

O projeto segue uma arquitetura em camadas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Organização dos pacotes:

```text
config/
controllers/
domain/
 ├── entities/
 └── enums/
dtos/
 ├── request/
 └── response/
exceptions/
mappers/
repositories/
services/
resources/
 └── db/migration/
```

## 💻 Front-end

Este repositório inclui uma aplicação front-end apenas para demonstrar o consumo da API.

O foco deste projeto é o desenvolvimento da API REST. 
O front-end foi desenvolvido com auxílio de Inteligência Artificial e recebeu apenas adaptações para integração com esta API. 
Seu objetivo é servir como interface de demonstração das funcionalidades do back-end.
---

## 📚 Regras de negócio

- Clientes possuem status (ACTIVE / INACTIVE).
- Produtos possuem status.
- Vendas possuem controle de status.
- Não é permitido cadastrar registros duplicados quando houver restrições de unicidade.
- Exclusões respeitam relacionamentos entre entidades.
- Todas as entradas são validadas antes do processamento.
- As respostas de erro seguem um padrão único.

---

## 🗄 Banco de dados

O projeto utiliza **Flyway** para versionamento do banco de dados.

As migrações ficam em:

```text
src/main/resources/db/migration
```

Ao iniciar a aplicação, todas as migrações são executadas automaticamente.

---

## 📖 Documentação da API

Após iniciar a aplicação:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🚀 Como executar

### Clone o projeto

```bash
git clone https://github.com/AlexbarrosDev/supermarket-system.git
```

### Entre no projeto

```bash
cd supermarket-system
```

### Configure o banco de dados

Edite o arquivo:

```text
src/main/resources/application.yaml
```

Configure:

- URL
- Usuário
- Senha

### Execute

```bash
./mvnw spring-boot:run
```

ou

```bash
mvn spring-boot:run
```

---

## 📌 Endpoints

### Clientes

- GET /clients
- GET /clients/{id}
- POST /clients
- PUT /clients/{id}
- PATCH /clients/{id}/status

### Produtos

- GET /products
- GET /products/{id}
- POST /products
- PUT /products/{id}
- PATCH /products/{id}/status

### Categorias

- GET /categories
- GET /categories/{id}
- POST /categories
- PUT /categories/{id}

### Grupos

- GET /groups
- GET /groups/{id}
- POST /groups
- PUT /groups/{id}

### Vendas

- GET /sales
- GET /sales/{id}
- POST /sales
- PATCH /sales/{id}/status

---

## 📈 Próximas melhorias

- Testes unitários
- Testes de integração
- Docker
- CI/CD com GitHub Actions
- Autenticação com Spring Security + JWT
- Paginação e filtros
- Front-end integrado

---

## 👨‍💻 Autor

**Alex Barros**

Estudante de Análise e Desenvolvimento de Sistemas

Back-end Java Developer

📍 Itapetininga - SP

---

## 📬 Contato

- LinkedIn: https://www.linkedin.com/in/alex-barros-dev
- Email: alexbarros.dev@gmail.com









































