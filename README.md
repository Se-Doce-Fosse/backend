# Backend do projeto Se Doce Fosse

Backend da aplicação Se Doce Fosse desenvolvido com Spring Boot.

## Pré-requisitos

- Java 17+
- Maven 3.6+
- Git

## Como rodar o projeto

Opção 1 — Rodar com Docker (desenvolvimento)

Clone o repositório:
```bash
git clone https://github.com/Se-Doce-Fosse/backend.git
cd backend
```
Suba o backend em modo desenvolvimento com:
```bash
docker-compose up --build backend-dev
```
O Docker vai baixar as dependências e iniciar o backend.

Acesse a aplicação em `http://localhost:8080/api/hello`

Opção 2 — Rodar sem Docker (manual)

Clone o repositório:
```bash
git clone https://github.com/Se-Doce-Fosse/backend.git
cd backend
```
Compilar o projeto:
```bash
mvn clean compile
```
Executar o projeto:
```bash
mvn spring-boot:run
```
Acesse a aplicação em `http://localhost:8080/api/hello`

## Arquitetura

O projeto utiliza o padrão de arquitetura em **Camadas (Layered Architecture)** no Spring Boot, seguindo as melhores práticas de separação de responsabilidades:

### Camadas da Aplicação

- **Controller Layer** - Responsável por receber requisições HTTP e retornar respostas
- **Service Layer** - Contém a lógica de negócio da aplicação
- **Repository Layer** - Responsável pelo acesso e manipulação de dados
- **Model Layer** - Representa os modelos de dados

### Benefícios da Arquitetura em Camadas

- **Separação de Responsabilidades** - Cada camada tem uma função específica
- **Manutenibilidade** - Facilita a manutenção e evolução do código
- **Testabilidade** - Permite testar cada camada independentemente
- **Reutilização** - Componentes podem ser reutilizados entre diferentes partes da aplicação
- **Escalabilidade** - Facilita a expansão e modificação da aplicação

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/sedocefosse/backend/
│   │       ├── BackendApplication.java     # Classe principal
│   │       ├── controller/                 # Controllers REST (Camada de Apresentação)
│   │       │   └── HelloController.java
│   │       ├── service/                    # Lógica de negócio (Camada de Serviço)
│   │       │   └── ExampleService.java
│   │       ├── repository/                 # Acesso a dados (Camada de Repositório)
│   │       │   └── ExampleRepository.java
│   │       └── model/                      # Modelos de dados (Camada de Modelo)
│   │           └── ExampleModel.java
│   └── resources/
│       └── application.properties          # Configurações da aplicação
└── test/
    └── java/
        └── com/sedocefosse/backend/
```

## API Endpoints

- **GET** `/api/hello` - Endpoint de teste

## Dependências Principais

- **Spring Boot 3.5.4** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança e autenticação
- **H2 Database** - Banco de dados para desenvolvimento
- **Lombok** - Redução de boilerplate
