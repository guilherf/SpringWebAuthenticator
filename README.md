# SpringWebAuthenticator

API REST de cadastro, listagem e remoção de usuários feita com **Spring Boot**. As senhas são armazenadas com hash **BCrypt**, cada usuário tem um papel (`USER_ROLE` ou `ADMIN_ROLE`) e a base para autenticação com **JWT** (filtro, serviço de token e `UserDetails`) já está montada. Os dados ficam em um banco H2 em memória.

> **Projeto em desenvolvimento.** O cadastro, a listagem e a remoção já existem, mas o login que gera o token JWT ainda não foi implementado. Veja a seção [Estado atual e limitações](#estado-atual-e-limitações).

## Tecnologias

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Security (BCrypt, filtro de autenticação customizado, sessão stateless)
- Spring Data JPA
- H2 Database (em memória) com console web
- Jakarta Validation (Hibernate Validator)
- [java-jwt](https://github.com/auth0/java-jwt) 4.4.0 (auth0)
- [spring-dotenv](https://github.com/paulschwarz/spring-dotenv) 4.0.0 (leitura do arquivo `.env`)
- Lombok
- Maven (wrapper incluído)

## Pré-requisitos

- JDK 21
- Git

Não é preciso instalar o Maven: o projeto inclui o Maven Wrapper (`mvnw`).

## Configuração

O `application.properties` lê o segredo do JWT da variável `SECRET_TOKEN`:

```properties
api.security.token.secret=${SECRET_TOKEN}
```

Sem essa variável definida, a aplicação não sobe. Crie um arquivo `.env` na raiz do projeto (ele já está no `.gitignore`, então não vai para o repositório):

```env
SECRET_TOKEN=troque-por-um-segredo-longo-e-aleatorio
```

Também é possível definir `SECRET_TOKEN` como variável de ambiente do sistema.

## Como rodar

```bash
git clone https://github.com/guilherf/SpringWebAuthenticator.git
cd SpringWebAuthenticator
```

Depois de criar o `.env` (veja acima):

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

Tabelas criadas pelo JPA:

| Tabela        | Descrição                                           |
|---------------|-----------------------------------------------------|
| `users`       | Usuários (`id`, `nome`, `email` único, `password`)  |
| `roles`       | Papéis (`id`, `names`)                              |
| `users_roles` | Relação muitos-para-muitos entre usuários e papéis  |

Se você migrar para um banco real, não deixe usuário e senha no arquivo. Use variáveis de ambiente:

```properties
spring.datasource.password=${DB_PASSWORD}
```

## Endpoints

Todas as rotas ficam sob o prefixo `/user`.

| Método   | Rota                | Autenticação      | Descrição              |
|----------|---------------------|-------------------|------------------------|
| `POST`   | `/user/register`    | Pública           | Cadastra um usuário    |
| `GET`    | `/user/getUsers`    | Token Bearer      | Lista os usuários      |
| `DELETE` | `/user/remove/{id}` | Token Bearer      | Remove um usuário      |

As rotas `/user/register`, `/user/login` e `/h2-console/**` estão na lista de rotas sem autenticação (`WebSecurity.ENDPOINTS_SEM_AUTENTIFICACAO`). Todas as outras exigem o header `Authorization: Bearer <token>`.

### `POST /user/register`: cadastra um usuário

Corpo da requisição:

```json
{
  "nome": "Maria",
  "email": "maria@email.com",
  "password": "senha12345",
  "role": "USER_ROLE"
}
```

O campo `role` aceita `USER_ROLE` ou `ADMIN_ROLE`.

Resposta: `201 Created`

```json
{
  "id": 1,
  "nome": "Maria",
  "email": "maria@email.com",
  "roles": [
    { "id": 1, "names": "USER_ROLE" }
  ]
}
```

A senha nunca é devolvida. No banco ela é salva com hash BCrypt.

### `GET /user/getUsers`: lista os usuários

Resposta: `200 OK`, com uma lista de objetos no mesmo formato da resposta do cadastro.

### `DELETE /user/remove/{id}`: remove um usuário

Exclui o usuário com o `id` informado na URL.

Resposta: `200 OK`, com os dados do usuário removido.

```json
{
  "id": 1,
  "nome": "Maria",
  "email": "maria@email.com",
  "roles": [
    { "id": 1, "names": "USER_ROLE" }
  ]
}
```

Se o `id` não existir, a API responde `404 Not Found`.

## Validações (`/user/register`)

| Campo      | Regra                             | Mensagem                                                |
|------------|-----------------------------------|---------------------------------------------------------|
| `nome`     | Obrigatório, não pode ser vazio   | Insira o nome de usuario                                |
| `email`    | Obrigatório e em formato válido   | Insira o email / Email invalido                         |
| `password` | Obrigatória, de 8 a 32 caracteres | Insira a senha (a mensagem do `@Size` ainda está vazia) |
| `role`     | Sem validação                     | -                                                       |

Se alguma regra falhar, a API responde `400 Bad Request` e nada é salvo.

## Tratamento de erros

O `GlobalExeptionHandler` (`@RestControllerAdvice`) trata:

| Situação                                           | Status | Corpo                                                                |
|----------------------------------------------------|--------|----------------------------------------------------------------------|
| E-mail já cadastrado (violação da restrição única) | `409`  | `{"erro": "Violação de integridade de dados (registro duplicado)"}`  |
| `EmailCadastradoExeption`                          | `409`  | `{"erro ": "Email Ja cadastrado<email>"}`                            |

A `EmailCadastradoExeption` já existe e tem handler, mas o `UserService` ainda não a lança. Hoje o e-mail duplicado é barrado pela restrição `unique` do banco, que cai no primeiro caso.

## Autenticação com JWT

O que já existe no código:

- `AuthenticationFilter`: a cada requisição a uma rota não pública, lê o header `Authorization`, valida o token, busca o usuário pelo e-mail (subject do token) e preenche o `SecurityContext`. Sem token, lança `RuntimeException("Token está ausente!")`.
- `JwtTokenService`: gera tokens (`generetToken`, emissor `pizzurg-api`, validade de 4 horas) e valida tokens (`getSubjectFromToken`), usando HMAC256 com o `SECRET_TOKEN`.
- `UserDetailsimpl` e `UserDetailsServiceimpl`: adaptam o `EntityUser` para o Spring Security, transformando os papéis em authorities.
- `WebSecurity`: desativa CSRF, usa sessão stateless, libera as rotas públicas e registra o filtro antes do `UsernamePasswordAuthenticationFilter`.

## Estado atual e limitações

Pontos que o código ainda não resolve, para evitar surpresas ao testar:

- **Não há endpoint de login.** `/user/login` está na lista de rotas públicas, mas nenhum controller a implementa, e o `generetToken` ainda não é chamado em lugar nenhum. Por isso `GET /user/getUsers` e `DELETE /user/remove/{id}` ainda não podem ser usados com um token real.
- **O token gerado não leva o e-mail como subject** (falta `withSubject`), e o filtro usa o subject para buscar o usuário.
- **O segredo é lido em um campo `static`** (`@Value` em `JwtTokenService.SECRET_KEY`), e o Spring não injeta valores em campos estáticos. Isso precisa virar campo de instância para o `SECRET_TOKEN` chegar ao serviço.
- **O filtro compara a URI exatamente** com a lista de rotas públicas, então o padrão `/h2-console/**` nunca coincide e as chamadas ao console H2 também passam pela exigência de token.
- **`EntityAdmin`** é uma classe vazia, reservada para o perfil de administrador.
- O `@Size` da senha tem mensagem vazia e o campo `role` não é validado.

## Estrutura do projeto

```
src/main/java/net/bifewagyu/authenticator
├── AuthenticatorApplication.java            # Classe principal
├── controllers/
│   └── UserController.java                  # Rotas da API (/user)
├── details/
│   ├── UserDetailsimpl.java                 # EntityUser adaptado para o Spring Security
│   └── service/UserDetailsServiceimpl.java  # Carrega o usuário pelo e-mail
├── dtos/
│   ├── request/UserRequest.java             # Dados recebidos (com validação)
│   └── response/
│       ├── UserResponse.java                # Dados devolvidos
│       └── RecoveryJwtTokenDto.java         # Resposta com o token (ainda não utilizada)
├── globalhandler/
│   ├── EmailCadastradoExeption.java         # Exceção de e-mail duplicado
│   └── exeptionHandler/
│       └── GlobalExeptionHandler.java       # Respostas de erro (409)
├── models/
│   ├── EntityUser.java                      # Entidade de usuário
│   ├── RoleModels.java                      # Entidade de papel
│   └── EntityAdmin.java                     # Entidade de administrador (em desenvolvimento)
├── repository/
│   └── UserRepository.java                  # Acesso ao banco (JPA)
├── roles/
│   └── RolesNames.java                      # Enum: USER_ROLE, ADMIN_ROLE
├── security/
│   ├── AuthenticationFilter.java            # Filtro que valida o token JWT
│   └── WebSecurity.java                     # Regras de acesso e BCryptPasswordEncoder
└── service/
    ├── JwtTokenService.java                 # Geração e validação de tokens
    └── UserService.java                     # Regras de negócio
```

## Próximos passos

- Criar o endpoint de login que valida e-mail e senha e devolve o token (`RecoveryJwtTokenDto`)
- Incluir o e-mail como subject do token e corrigir a leitura do segredo (campo não estático)
- Lançar a `EmailCadastradoExeption` no cadastro e devolver as mensagens de validação por campo
- Preencher a mensagem do `@Size` da senha e validar o `role`
- Restringir rotas por papel (`USER_ROLE` / `ADMIN_ROLE`) e implementar o perfil de administrador
- Migrar para um banco de dados persistente

## Autor

Feito por [guilherf](https://github.com/guilherf).
