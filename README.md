# SpringWebAuthenticator

API REST de cadastro e remoção de usuários feita com **Spring Boot**. Os dados de entrada são validados, a senha é armazenada com hash **BCrypt** e os dados ficam em um banco H2 em memória.

> Projeto em desenvolvimento. O login com autenticação real e o perfil de administrador (`EntityAdmin`) ainda não foram implementados.

## Tecnologias

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Security (BCrypt)
- Spring Data JPA
- H2 Database (em memória) com console web
- Jakarta Validation (Hibernate Validator)
- Lombok
- Maven (wrapper incluído)

## Pré-requisitos

- JDK 21
- Git

Não é preciso instalar o Maven: o projeto inclui o Maven Wrapper (`mvnw`).

## Como rodar

```bash
git clone https://github.com/guilherf/SpringWebAuthenticator.git
cd SpringWebAuthenticator
```

Linux / Mac:

```bash
./mvnw spring-boot:run
```

Windows:

```bash
mvnw.cmd spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

Para rodar os testes:

```bash
./mvnw test
```

## Banco de dados

O banco é um **H2 em memória**, então os dados são apagados a cada reinicialização.

| Item        | Valor                                |
|-------------|--------------------------------------|
| Console web | `http://localhost:8080/h2-console`   |
| JDBC URL    | `jdbc:h2:mem:testdb`                 |
| Usuário     | `sa`                                 |
| Senha       | definida em `application.properties` |

Se você migrar para um banco real, não deixe usuário e senha no arquivo. Use variáveis de ambiente:

```properties
spring.datasource.password=${DB_PASSWORD}
```

## Endpoints

As rotas `/user/**` e `/h2-console/**` são públicas. Qualquer outra rota exige autenticação.

### `POST /user/login`: cadastra um usuário

Corpo da requisição:

```json
{
  "nome": "Maria",
  "email": "maria@email.com",
  "password": "senha12345"
}
```

Resposta: `201 Created`

```json
{
  "id": 1,
  "nome": "Maria",
  "email": "maria@email.com"
}
```

A senha nunca é devolvida. No banco ela é salva com hash BCrypt.

### `POST /user/remove`: remove um usuário

Recebe o objeto do usuário (`EntityUser`) no corpo e o exclui do banco.

```json
{
  "id": 1,
  "nome": "Maria",
  "email": "maria@email.com",
  "password": "senha12345"
}
```

Resposta: `201 Created`, com o e-mail seguido de `REMOVED`.

```json
{
  "id": 1,
  "nome": "Maria",
  "email": "maria@email.comREMOVED"
}
```

## Validações (`/user/login`)

| Campo      | Regra                          | Mensagem            |
|------------|--------------------------------|---------------------|
| `nome`     | Obrigatório, não pode ser vazio | Insira o nome de usuario |
| `email`    | Obrigatório e em formato válido | Insira o email / Email invalido |
| `password` | Obrigatória, de 8 a 32 caracteres | Insira a senha (a mensagem do `@Size` ainda está vazia) |

Se alguma regra falhar, a API responde `400 Bad Request` e nada é salvo.

## Estrutura do projeto

```
src/main/java/net/bifewagyu/authenticator
├── AuthenticatorApplication.java    # Classe principal
├── controllers/UserController.java  # Rotas da API
├── dtos/
│   ├── request/UserRequest.java     # Dados recebidos (com validação)
│   └── response/UserResponse.java   # Dados devolvidos
├── models/
│   ├── EntityUser.java              # Entidade de usuário
│   └── EntityAdmin.java             # Entidade de administrador (em desenvolvimento)
├── repository/UserRepository.java   # Acesso ao banco (JPA)
├── security/WebSecurity.java        # Regras de acesso e BCryptPasswordEncoder
└── service/UserService.java         # Regras de negócio
```

## Próximos passos

- Impedir cadastro de e-mails duplicados
- Devolver as mensagens de validação por campo (por exemplo, com `@RestControllerAdvice`) e preencher a mensagem do `@Size`
- Trocar a rota de cadastro `POST /user/login` por algo como `POST /user` e o remover por `DELETE /user/{id}`
- Usar `201 Created` só na criação (a remoção devolve `201` hoje)
- Implementar login real com autenticação (por exemplo, JWT)
- Implementar o perfil de administrador
- Migrar para um banco de dados persistente

## Autor

Feito por [guilherf](https://github.com/guilherf).
