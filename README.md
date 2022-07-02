# Sistema de crítica de filmes

## Coding Challenge Itaú Bootcamp Devs

Este repositório contém o desafio proposto no Coding Challenge de criar 2 API's:
  - API para críticas de filmes (Principal)
  - API para a segurança (Segurança)

Neste projeto, toda chamada da API principal precisa passar pela API de segurança para ser aceita ou ser rejeitada, caso o usuário não tenha acesso àquela informação.

## Requisitos para teste

  - Postman para as requisições
  - MySql para armazenamento dos dados
  - Uma IDE que execute o projeto, como o Intellij ou Eclipse

## Alterações a serem feitas

Como o projeto roda somente localmente, é necessário que, após a clonagem do repositório, sejam adicionados o usuário e a senha do seu MySql no application.properties,
presente na pasta "resources". Não é necessário criar nenhum schema pois o projeto o cria automaticamente caso ele não exista.

## Regras de negócio

Todo usuário possui um perfil que vai de:
  - Leitor: Após o cadastro, esse usuário poderá logar e buscar por um filme. Ele poderá ver as informações de um filme, 
  comentários e dar uma nota para o filme. A cada filme que o usuário avaliar, ele ganha 1 ponto em seu perfil.
  - Básico:  O usuário leitor poderá se tornar BÁSICO quando adquirir 20 pontos. Nesse perfil será possível postar 
  comentários, notas e responder comentários. Cada resposta que o usuário enviar, ele ganha 1 ponto.
  - Avançado: O usuário básico poderá se tornar AVANÇADO quando adquirir 100 pontos. 
  Esse perfil tem as capacidades do BÁSICO, e mais citar outros comentários (comentários feitos por outros usuários) e marcar comentários como “gostei” ou "não gostei”.
  - Moderador: Um usuário poderá se tornar MODERADOR de 2 formas: um moderador torna outro usuário moderador ou por pontuação,
  para se tornar MODERADOR o usuário deverá ter 1000 pontos. O moderador tem as capacidades do AVANÇADO, e mais excluir um comentário ou marcar como repetida.

### API Principal

A função desta API é receber comentários e notas de usuários para filmes que eles tenham assistido. Para isso, ela conta com os seguintes endpoints:

### Endpoints dos filmes 

  - /movie/{id} Método GET:
    Este endpoint realiza uma busca na api externa (OmdApi)[https://www.omdbapi.com/] e retorna os dados dos filmes a partir do **imdbID** enviado pela URL
    encontrado no próprio [imdb](imdb.com). Com o filme encontrado, o mesmo é adicionado no banco de dados local.
    
  - /movie/title Método GET:
    Este endpoint realiza uma busca na api externa (OmdApi)[https://www.omdbapi.com/] e retorna os dados dos filmes a partir do seu título e, caso seja escolhido, 
    pelo seu ano de lançamento, ambos devem ser enviados como parâmetros como **title** para o título e **year** para o ano de lançamento. 
    Com o filme encontrado, o mesmo é adicionado no banco de dados local.
    
  - /movie/comments Método GET:
    Este endpoint realiza uma busca no banco de dados local e retorna uma lista de todos os comentários feitos para aquele filme
    
 Os filmes são configurados a partir de seu:
 
   -  [MovieController](src/main/java/jonathan_coutinho/CodingChallenge/controller/MovieController.java)
   -  [MovieService](src/main/java/jonathan_coutinho/CodingChallenge/service/MovieService.java)
   -  [MovieRepository](src/main/java/jonathan_coutinho/CodingChallenge/repository/MovieRepository.java)
   -  [Movie](src/main/java/jonathan_coutinho/CodingChallenge/domain/Movie.java)
    
### Endpoints dos comentários 

  - /comment/ Método POST:
    Este endpoint cria um comentário a partir do corpo enviado em Json informando os atributos necessários e o adiciona no banco de dados local
    .É retornado o comentário criado, com todas as suas informações.
    
      * "userID" id do usuário
      * "ImdbId" ImdbID do filme
      * "comment" Texto enviado pelo usuário
      * "timeNDate" Horário em que o comentário foi enviado, em Date (ex: 2022-07-01T20:15:43)
        
  - /comment/reply Método POST:
    Este endpoint cria a resposta para um comentário já feito por outro usuário e o adiciona no banco de dados local. O corpo é semelhante ao método acima, 
    acrescentando o ID do comentário a ser respondido como parâmetro o **id** do comentário.
        
  - /comment/quote Método POST:
    Este endpoint cria um comentário que possuí citações a outros comentários e o adiciona no banco de dados local. Este método requer
    os mesmos dados que o primeiro método da lista. Ele também é acionado caso, ao criar um comentário comum, o texto do comentário possua uma tag de citação
    ("{comment-ID}").
    
  - /comment/moderation/flagDuplicate Método PUT:
    Este endpoint, a partir do parâmetro **id** do comentário, adiciona uma flag de duplicado ao comentário.
    
  - /comment/{id} Método GET
    Este endpoint busca um comentário específico, a partr da sua **id** fornecida na URL
    
  - /comment/putReaction MÉTODO PUT
    Este endpoint cria uma reação de "gostei" ou "não gostei" a um comentário existente. É requerido como parâmetros o **id** do 
    comentárrio a ser reagido e a **reaction** a ele ("like" ou "dislike"). O comentário é atualizado no banco de dados local e é retornado
    
  - /comment/ Método DELETE:
    Este endpoint deleta um comentário, pedindo como parâmetros o **id** do comentário e o **username** do usuário que está fazendo esta solicitação.
    Usuários comuns só podem deletar seus próprios comentários enquanto usuários Moderadores podem deletar qualquer comentário.
    Quando o comentário é excluído, o usuário responsável por ele perde 1 ponto.
    
 Os comentários são configurados a partir de seu:
 
   -  [CommentController](src/main/java/jonathan_coutinho/CodingChallenge/controller/CommentController.java)
   -  [CommentService](src/main/java/jonathan_coutinho/CodingChallenge/service/CommentService.java)
   -  [CommentRepository](src/main/java/jonathan_coutinho/CodingChallenge/repository/CommentRepository.java)
   -  [Comment](src/main/java/jonathan_coutinho/CodingChallenge/domain/Comment.java)
   -  [CommentDTO](src/main/java/jonathan_coutinho/CodingChallenge/dto/CommentDTO.java)
   -  [NewCommentDTO](src/main/java/jonathan_coutinho/CodingChallenge/dto/NewCommentDTO.java)
   -  [ReplyCommentDTO](src/main/java/jonathan_coutinho/CodingChallenge/dto/ReplyCommentDTO.java)
   
### Endpoints das notas

  - /score Método POST
    Este endpoint aplica uma nota a um filme, a partir do corpo enviado em Json informando os atributos necessários e a salva no
    banco de dados local. São necessários os dados:
    
      * "score" Nota a ser dada em float
      * "username" Nome de usuário dando a nota
      * "movieId" ImdbID do filme a ser avaliado
      
  - /score Método PUT
    Este endpoint atualiza uma nota já aplicada a um filme anteriormente e a atualiza no banco de dados local. Requer os mesmos dados informados acima
    
 As notas são configuradas a partir de seu:
 
   -  [ScoreController](src/main/java/jonathan_coutinho/CodingChallenge/controller/ScoreController.java)
   -  [ScoreService](src/main/java/jonathan_coutinho/CodingChallenge/service/ScoreService.java)
   -  [ScoreRepository](src/main/java/jonathan_coutinho/CodingChallenge/repository/ScoreRepository.java)
   -  [Score](src/main/java/jonathan_coutinho/CodingChallenge/domain/Score.java)
   -  [ScoreDTO](src/main/java/jonathan_coutinho/CodingChallenge/dto/ScoreDTO.java)
   
### Endpoints dos usuários

  - /user/{username} Método GET
    Este endpoint retorna um usuário cadastrado na base dados local e o retorna a partir do seu **username** informado na URL.
    
  - /user/comments Método GET
    Este endpoint retorna uma lista com todos os comentários postados por um usuário específico a partir do parâmetro **username** informado.
    
  - /user/moderation/upgrade Método PUT
    Este endpoint da o cargo de Moderador a um usuário, a partir do seu nome de usuário, fornecido como o parãmetro **receiver**. Este método só pode
    ser realizado por moderadores.
    
  - /user/{username} Método PUT
    Este endpoint aumenta os pontos de um usuário específico em 21. Pode ser usado para testar as autorizações de um usuário. É necessário fornecer o **username** 
    como parâmetro.
    
  Os usuários são configurados a partir de seu:
 
   -  [UserController](src/main/java/jonathan_coutinho/CodingChallenge/controller/UserController.java)
   -  [UserService](src/main/java/jonathan_coutinho/CodingChallenge/service/UserService.java)
   -  [UserRepository](src/main/java/jonathan_coutinho/CodingChallenge/repository/UserRepository.java)
   -  [User](src/main/java/jonathan_coutinho/CodingChallenge/domain/User.java)
   -  [UserDTO](src/main/java/jonathan_coutinho/CodingChallenge/dto/UserDTO.java)
   
## API de Segurança

A API de Segurança foi feita acoplada a API principal e toda, e **qualquer** requisição feita pela API Principal requer um token JWT no 
cabeçalho da mesma, a partir do atributo **Authorization**. 
Este token JWT é único por usuário, tem duração de 1 dia e contém as informações deste usuário, inclusive seu perfil e suas autorizações
e é informado a partir de alguns métodos.

### Endpoints  [AccountController]((src/main/java/jonathan_coutinho/CodingChallenge/controller/AccountController.java))

  - /signup Método POST
    Este endpoint realiza o cadastro de um novo usuário a partir do corpo informado em Json com os seguintes atributos:
      * "username" Nome de usuário, que deve ser único
      * "email" Email do usuário, que só pode ser cadastrado a 1 usuário
      * "password" Senha do usuário. A senha precisa ser de no mínimo 10 caracteres, 1 caixa alta, 1 caixa baixa, 1 número e 1 caractere especial
    Caso o método seja bem sucedido, é retornado os dados do novo usuário e no corpo da resposta é providenciado o seu token JWT.
    
  - /login Método POST
    Este endpoint realiza o login de um usuário existente no banco de dados local a partir do corpo informado em Json com os seguintes atributos:
      * "username" Nome do usuário
      * "password" Senha do usuário
    Caso o método seja bem sucedido, é retornado os dados do usuário logado e no corpo da resposta é providenciado o seu token JWT.
    Logins por senha errada são salvos em cache, através do Guava e, caso ocorram 4 falhas de login durante 5 minutos, a conta do usuário é bloqueada
    e só é desbloqueada após 5 minutos do bloqueio, a partir de uma nova tentativa de login.
    
  - /refresh_token
    Este endpoint realiza a atualização do token JWT de um usuário a partir do token JWT antigo dele enviado no cabeçalho.
    
Os métodos se utilizam do [AccountService](src/main/java/jonathan_coutinho/CodingChallenge/security/AccountService.java) para realizar
suas configurações de acordo com a resposta dada pela autenticação/autorização.

As configurações de segurança se encontram no pacote [security](src/main/java/jonathan_coutinho/CodingChallenge/security), onde costam
suas configurações de:

  * [HTTP e permissões](src/main/java/jonathan_coutinho/CodingChallenge/security/SecurityConfiguration.java)
  * [Filtro de autenticação incial](src/main/java/jonathan_coutinho/CodingChallenge/security/JWTAuthorizationFilter.java) e 
  [Filtro de autenticação secundário](src/main/java/jonathan_coutinho/CodingChallenge/security/JwtCredentialsAuthenticationFilter.java)
  * [Beans de Segurança](src/main/java/jonathan_coutinho/CodingChallenge/security/SecurityBeans.java) para Cors e Encoder.
  * [Handler para falta de autorização do usuário](src/main/java/jonathan_coutinho/CodingChallenge/security/CustomAccessDeniedHandler.java)
  * [Modelo de credenciais a ser fornecido](src/main/java/jonathan_coutinho/CodingChallenge/security/CredentialsRequest.java)

Em relação às requisições, os únicos caminhos que **não** necessitam obrigatoriamente do token JWT são "/login" e "/signup". Todos os restantes necessitam 
do token enviado como parâmetro "Authorization" no seu cabeçalho.

### Cache

As configurações de cache se encontram em [AccountCacheConfig](src/main/java/jonathan_coutinho/CodingChallenge/service/AccountCacheConfig.java), o 
sistema funciona localmente, isto é, ele é resetado sempre que a aplicação é terminada. Os caches têm duração máxima de 5 minutos e são chamados quando 
o login de um usuário falha por conta de senha errada.


### Informações adicionais 

Este projeto foi feito como um modelo para um sistema de críticas de filmes. Outros dados como imagens, trailers, premiações, etc dos filmes não foram 
armazenadas por questões de otimização e necessidade. Dito isto, qualquer feedback é mais do que bem vindo para melhorar a qualdade das API's.

*Happy Coding Everyone :D*

      
