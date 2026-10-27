# SpringWebAuthenticator

API de autenticação desenvolvida com **Spring Boot**. No momento, o projeto permite cadastrar usuários (nome, e-mail e senha) com validação dos dados, usando um banco H2 em memória.

> Projeto em desenvolvimento. A configuração do Spring Security (`WebSecurity`) e o modelo de administrador (`EntityAdmin`) ainda estão vazios.

## Tecnologias

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Security
- Spring Data JPA
- H2 Database (em memória) com console web
- Jakarta Validation
- Lombok
- Maven (wrapper incluído)

## Pré-requisitos

- JDK 21 instalado
- Git

Não é necessário instalar o Maven, pois o projeto inclui o Maven Wrapper (`mvnw`).

## Como rodar

### 1. Clonar o repositório

```bash
git clone https://github.com/guilherf/SpringWebAuthenticator.git
cd SpringWebAuthenticator
```

### 2. Executar a aplicação

Linux / Mac:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

### 3. Rodar os testes

```bash
./mvnw test
```

## Banco de dados

O projeto usa o **H2 em memória**, então os dados são apagados toda vez que a aplicação é reiniciada.

- Console web: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuário: `sa`

A senha do banco está definida no arquivo `src/main/resources/application.properties`. Por ser um banco em memória usado só em desenvolvimento, não há risco, mas **se você trocar para um banco real, não deixe usuário e senha no arquivo**. Use variáveis de ambiente, por exemplo:

```properties
spring.datasource.password=${DB_PASSWORD}
```

## Endpoints

| Método | Rota    | Descrição                          | Resposta         |
|--------|---------|------------------------------------|------------------|
| GET    | `/user` | Cadastra um usuário (corpo JSON)   | `201 Created`    |

Exemplo de corpo da requisição:

```json
{
  "nome": "Maria",
  "email": "maria@email.com",
  "password": "senha12345"
}
```

Exemplo de resposta:

```json
{
  "id": 1,
  "nome": "Maria",
  "email": "maria@email.com"
}
```

Regras de validação:

- `nome`: obrigatório
- `email`: precisa ser um e-mail válido
- `password`: entre 8 e 32 caracteres

> Observação: o cadastro está mapeado em `GET /user` com corpo na requisição. O mais comum é usar `POST` para criar recursos, então essa rota deve mudar nas próximas versões.

## Estrutura do projeto

```
src/main/java/net/bifewagyu/authenticator
├── AuthenticatorApplication.java   # Classe principal
├── controllers/UserController.java # Rotas da API
├── dtos/
│   ├── request/UserRequest.java    # Dados recebidos
│   └── response/UserResponse.java  # Dados devolvidos
├── models/
│   ├── EntityUser.java             # Entidade de usuário
│   └── EntityAdmin.java            # Entidade de administrador (em desenvolvimento)
├── repository/UserRepository.java  # Acesso ao banco
├── security/WebSecurity.java       # Configuração de segurança (em desenvolvimento)
└── service/UserService.java        # Regras de negócio
```

## Próximos passos

- Salvar a senha com hash (por exemplo, `BCryptPasswordEncoder`) em vez de texto puro
- Configurar o Spring Security (login, rotas protegidas)
- Trocar o cadastro para `POST`
- Implementar o perfil de administrador
- Migrar para um banco de dados persistente

## Autor

Feito por [guilherf](https://github.com/guilherf).
