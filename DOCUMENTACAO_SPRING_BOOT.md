# Documentacao do Spring Boot no projeto Escadas

Esta documentacao explica como o Spring Boot funciona dentro do projeto Escadas, cobrindo a arquitetura, o fluxo de requisicoes, a persistencia, a seguranca e as integracoes. O objetivo e que um estudante de Sistemas de Informacao com 2 anos de curso consiga entender e navegar pelo codigo com seguranca.

## 1. Visao geral

O projeto e uma aplicacao web baseada em Spring Boot, com:
1. Camada Web usando Spring MVC e Thymeleaf (paginas HTML).
2. Camada de Servicos com regras de negocio.
3. Persistencia com Spring Data JPA (MySQL).
4. Seguranca com Spring Security (login por formulario).
5. Integracao com Google Drive para upload de arquivos.
6. Geracao de PDF para pedidos.

O ponto de entrada e a classe `AplicacaoEscadas`, que inicia o Spring Boot e configura os beans.

## 2. Tecnologias e dependencias principais

Definidas em `build.gradle`:
1. `spring-boot-starter-web`: MVC, controllers, request/response, view resolver.
2. `spring-boot-starter-thymeleaf`: templates HTML em `src/main/resources/templates`.
3. `spring-boot-starter-security`: login, sessao e filtro de seguranca.
4. `spring-boot-starter-data-jpa`: ORM com entidades e repositorios.
5. `mysql-connector-j`: driver do MySQL.
6. Bibliotecas Google Drive OAuth e API.
7. `openhtmltopdf`: geracao de PDF a partir de HTML.
8. `logstash-logback-encoder`: logging estruturado.

## 3. Estrutura de pastas (resumo)

1. `src/main/java/com/example/demo`
   - `AplicacaoEscadas.java`: classe principal do Spring Boot.
   - `web/`: controllers Spring MVC.
   - `pedidos/`: dominio de pedidos (entidades, repositorios, servicos, CRUD).
   - `auth/`: autenticacao e seguranca.
   - `drive/`: integracao com Google Drive.
   - `shared/`: componentes reutilizaveis (CRUD, navegacao).
   - `util/`: utilitarios de formatacao e numeros.
2. `src/main/resources`
   - `application.properties` e `application-dev.properties`: configuracoes.
   - `templates/`: paginas Thymeleaf.
   - `static/`: CSS, JS e imagens.

## 4. Bootstrapping: como o Spring Boot inicia

Arquivo: `src/main/java/com/example/demo/AplicacaoEscadas.java`

1. `@SpringBootApplication` ativa:
   - Auto-configuracao do Spring Boot.
   - Component scan (acha `@Controller`, `@Service`, `@Component`, etc.).
   - Configuracoes padrao.
2. `SpringApplication.run(...)` sobe o contexto e o servidor web embutido.
3. Existe um `CommandLineRunner` que lista os beans no console (util para debug).

## 5. Camada Web (Controllers + Thymeleaf)

Principais controllers:
1. `ControladorInicio`:
   - `GET /` redireciona para `"/crud/pedidos"`.
2. `ControladorLogin`:
   - `GET /login` mostra pagina de login.
   - Se o usuario ja estiver autenticado, redireciona para `"/"`.
3. `ControladorCrud`:
   - `GET /crud/{chave}` carrega um modulo de CRUD generico.
   - O modulo e escolhido pelo `RegistroModuloCrud` com base na chave.
4. `ControladorPedidos`:
   - `GET /pedidos/novo`: pagina de cadastro.
   - `POST /pedidos`: salva novo pedido.
   - `GET /pedidos/{id}/editar`: edita pedido.
   - `POST /pedidos/{id}/editar`: atualiza pedido.
   - `GET /pedidos/{id}/visualizar`: visualiza detalhes.
   - `POST /pedidos/{id}/arquivos`: envia arquivo para Google Drive.
5. `ControladorTokenDrive`:
   - `GET /token`: pagina para configurar credenciais do Drive.
   - `POST /token/credenciais`: salva credenciais no banco.
   - `GET /oauth/google/start` e `GET /oauth/google/callback`: fluxo OAuth.
6. `ControladorLog`:
   - `GET /dev/logs`: retorna as ultimas linhas do log.

Os controllers retornam nomes de templates, por exemplo `"index"` ou `"login"`. O Thymeleaf resolve para arquivos em `src/main/resources/templates`.

## 6. Camada de Templates (Thymeleaf)

1. Templates principais:
   - `templates/index.html`: tela de listagem.
   - `templates/formulario.html`: formulario de pedidos.
   - `templates/login.html`: login.
   - `templates/pedido-visualizacao.html`: detalhes do pedido.
   - `templates/token.html`: configuracao do Google Drive.
2. Fragmentos reutilizaveis:
   - `templates/fragments/*.html`.
   - Exemplo: header, footer, filtros, CRUD.
3. Arquivos estaticos:
   - `static/css/style.css`
   - `static/js/select-busca.js`
   - `static/logo.png`

O Spring Boot serve os arquivos estaticos automaticamente de `static/`.

## 7. Camada de Dominio (Pedidos)

### 7.1 Entidade principal
Arquivo: `pedidos/model/Pedido.java`
1. Mapeada com `@Entity` para tabela `pedidos`.
2. Campos representam dados do pedido, cliente e obra.
3. Usa `@Column` para mapear colunas.

### 7.2 Repositorio
Arquivo: `pedidos/repository/RepositorioPedido.java`
1. Extende `JpaRepository` e `JpaSpecificationExecutor`.
2. Permite CRUD basico e consultas com `Specification`.

### 7.3 Filtro dinamico
Arquivo: `pedidos/spec/EspecificacaoPedido.java`
1. Monta filtros por:
   - Texto livre.
   - Numero do pedido.
   - Intervalo de datas.
2. Tambem oculta pedidos marcados com `oculto = true`.

### 7.4 Servicos
1. `FormularioPedidoService`:
   - Prepara campos do formulario e preenche opcoes.
   - Converte entre `FormularioPedido` (dados da tela) e `Pedido` (entidade).
2. `GeradorPdfPedido`:
   - Gera HTML e converte para PDF.
   - Usa logo e dados do pedido.

### 7.5 Upload de arquivos
1. `ServicoArquivoPedido`:
   - Envia arquivos ao Google Drive.
   - Cria pasta por pedido.
   - Salva link no banco.
2. `ArquivoPedido` + `ArquivoPedidoId`:
   - Representam arquivo vinculado ao pedido.
3. `RepositorioArquivoPedido`:
   - Busca arquivos por pedido e ordena por nome.

## 8. CRUD generico (Listagem e filtros)

O projeto tem um mecanismo generico de CRUD:
1. `ModuloCrud` representa um modulo com filtros e colunas.
2. `ProvedorModuloCrud` define como carregar dados do modulo.
3. `RegistroModuloCrud` injeta todos os provedores e permite buscar pelo nome.
4. `ModuloCrudPedidos` implementa o CRUD de pedidos.

Fluxo:
1. Usuario acessa `/crud/pedidos`.
2. `ControladorCrud` resolve o modulo pelo `RegistroModuloCrud`.
3. `ModuloCrudPedidos` monta filtros e colunas, consulta o banco e devolve as linhas.
4. O template `index.html` renderiza os dados.

## 9. Localidades (UF, Municipio, Bairro)

1. Entidades:
   - `Estado` (tabela `Estado`).
   - `Municipio` (tabela `Municipio`).
   - `Bairro` (tabela `Bairro`).
2. Repositorios usam queries derivadas do Spring Data.
3. `ConsultaLocalidades`:
   - Normaliza termos.
   - Filtra e limita resultados.
   - Retorna listas para formulario de pedidos.

## 10. Seguranca (Spring Security)

### 10.1 Configuracao principal
Arquivo: `auth/security/ConfiguracaoSeguranca.java`
1. `SecurityFilterChain` define:
   - Rotas publicas: `/login`, `/css/**`, `/js/**`, `/logo.png`.
   - Outras rotas exigem autenticacao.
2. Login por formulario:
   - Pagina: `/login`.
   - Parametros: `email` e `senha`.
   - Sucesso: `/crud/pedidos`.
3. Logout:
   - `POST /logout`.
   - Limpa sessao e cookie `JSESSIONID`.

### 10.2 Autenticacao customizada
Arquivo: `auth/security/ProvedorAutenticacaoUsuario.java`
1. Implementa `AuthenticationProvider`.
2. Busca usuario por email ou nome.
3. Valida senha:
   - Se for hash bcrypt, usa `PasswordEncoder`.
   - Se for texto puro, valida e migra para bcrypt automaticamente.
4. Cria `UsuarioAutenticado` como principal.

### 10.3 Usuario
Arquivo: `auth/model/Usuario.java`
1. Entidade `usuarios`.
2. Campos principais: `id`, `nome`, `email`, `senha`.

## 11. Google Drive (OAuth e arquivos)

### 11.1 Credenciais
1. Entidade: `CredenciaisDrive` (tabela `google_drive_credentials`).
2. `ServicoCredenciaisDrive`:
   - Salva client_id, project_id e client_secret.
3. `ServicoTokenDrive`:
   - Salva refresh_token do OAuth.

### 11.2 Fluxo OAuth (resumo)
1. Usuario acessa `/token` e preenche credenciais.
2. Clica para iniciar OAuth: `/oauth/google/start`.
3. Google redireciona para `/oauth/google/callback`.
4. `ServicoOAuthDrive` troca o `code` por `refresh_token`.
5. Token e salvo no banco.

### 11.3 Upload
1. `ServicoArquivoPedido` cria pasta por pedido no Drive.
2. Envia arquivo e cria permissao publica.
3. Salva link no banco (`ArquivoPedido`).

## 12. Configuracoes e perfis

### 12.1 application.properties
1. Nome da aplicacao.
2. Configuracao do Thymeleaf (UTF-8).
3. Configuracao de erros.
4. Configuracao de logs em arquivo `logs/app.log`.
5. Profile ativo: `dev`.

### 12.2 application-dev.properties
1. `spring.main.lazy-initialization=true` para iniciar beans sob demanda.
2. Configuracao do datasource MySQL.
3. Configuracao do Google Drive:
   - `google.drive.oauth-redirect-uri`

## 13. Fluxos principais de uso

### 13.1 Acesso e login
1. Usuario acessa `/`.
2. Se nao estiver autenticado, vai para `/login`.
3. Apos login, vai para `/crud/pedidos`.

### 13.2 Criar pedido
1. Acessa `/pedidos/novo`.
2. Preenche formulario.
3. Envia `POST /pedidos`.
4. Pedido e salvo no banco.

### 13.3 Consultar pedidos
1. Acessa `/crud/pedidos`.
2. Aplica filtros e pagina.
3. Os dados sao carregados pelo `ModuloCrudPedidos`.

### 13.4 Visualizar e anexar arquivos
1. Acessa `/pedidos/{id}/visualizar`.
2. Envia arquivo via `POST /pedidos/{id}/arquivos`.
3. Arquivo vai ao Google Drive e o link e salvo no banco.

### 13.5 Configurar Google Drive
1. Acessa `/token`.
2. Salva credenciais (client_id, project_id, client_secret, parent_folder).
3. Inicia OAuth e conclui para salvar refresh_token.

## 14. Como o Spring Boot "liga tudo"

1. O component scan encontra classes anotadas (`@Controller`, `@Service`, `@Component`).
2. O Spring cria instancias e injeta dependencias via construtor.
3. Controllers respondem a rotas.
4. Servicos encapsulam regras de negocio.
5. Repositorios fazem acesso ao banco usando JPA.
6. O view resolver do Thymeleaf renderiza as paginas HTML.
7. O Spring Security intercepta requisicoes antes do controller.

## 15. Pontos de extensao (se quiser evoluir)

1. Novo modulo CRUD:
   - Criar classe que implementa `ProvedorModuloCrud`.
   - Registrar automaticamente pelo Spring.
2. Novas paginas:
   - Adicionar template em `templates/`.
   - Criar controller com `@GetMapping`.
3. Novas entidades:
   - Criar classe com `@Entity`.
   - Criar repositorio `JpaRepository`.
4. Novas regras de seguranca:
   - Ajustar `ConfiguracaoSeguranca`.

## 16. Como executar localmente (resumo)

1. Configure o MySQL e ajuste `application-dev.properties` se necessario.
2. Execute:
   - `./gradlew bootRun`
3. Acesse:
   - `http://localhost:8080`

## 17. Arquivos chave (referencia rapida)

1. Classe principal: `src/main/java/com/example/demo/AplicacaoEscadas.java`
2. Configuracao de seguranca: `src/main/java/com/example/demo/auth/security/ConfiguracaoSeguranca.java`
3. CRUD de pedidos: `src/main/java/com/example/demo/pedidos/crud/ModuloCrudPedidos.java`
4. Controller de pedidos: `src/main/java/com/example/demo/web/ControladorPedidos.java`
5. Integracao Drive: `src/main/java/com/example/demo/drive/service/ServicoOAuthDrive.java`
6. Configuracoes: `src/main/resources/application.properties`
