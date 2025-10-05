# CODE REVIEW - Thomás Ramos para aluguel de carro de Vitor Veiga

1- Agente.java
Arquivos: Agente.java, Cliente.java, Usuario.java
Sugestão de melhoria: A classe Agente repete campos como nome, email e senha que já existem em Usuario. O ideal é que Agente herde de Usuario.
Benefícios:
- Menos código repetido, manutenção mais fácil.
- Todos os usuários ficam consistentes.
Implementação: Fazer Agente herdar de Usuario e remover os campos duplicados.

------------------------------------------------------------------------------------------------------------------------

2- Cliente e Agente
Arquivos: UsuarioAuth.java, Cliente.java, Agente.java
Sugestão de melhoria: Separar dados de login (email, senha) dos dados de perfil.
Benefícios:
- Mais segurança.
- Código mais organizado.
Implementação: Remover email e senha de Cliente e Agente e criar ligação @OneToOne com UsuarioAuth.

------------------------------------------------------------------------------------------------------------------------

3- HomeController e PedidoAluguelController
Arquivos: HomeController.java, PedidoAluguelController.java
Sugestão de melhoria: Mover lógica de pegar usuário logado para Service.
Benefícios:
- Controller mais limpo.
- Código reutilizável.
Implementação: Criar método no Service, ex: buscarPedidosDoUsuarioLogado(), e chamar no controller.

------------------------------------------------------------------------------------------------------------------------

4- Tratamento de erros
Arquivos: Todos os Serviços e Controllers
Sugestão de melhoria: Evitar RuntimeException genérica, criar exceções específicas.
Benefícios:
- Feedback mais claro para o usuário.
- Código mais limpo e centralizado.
Implementação: Criar exceções como RecursoNaoEncontradoException e usar @ControllerAdvice.

------------------------------------------------------------------------------------------------------------------------

5- PedidoAluguel e Aluguel
Arquivos: PedidoAluguel.java, Aluguel.java
Sugestão de melhoria: Juntar as duas classes em uma só.
Benefícios:
- Modelo de dados mais simples.
- Lógica centralizada.
Implementação: Criar Aluguel com enum StatusAluguel (PENDENTE, APROVADO, ATIVO).

------------------------------------------------------------------------------------------------------------------------

6- Carro.java e UsuarioAuth.java
Sugestão de melhoria: Não colocar regra de negócio no @PrePersist.
Benefícios:
- Separação de responsabilidades.
- Flexibilidade.
Implementação: Mover lógica de status para CarroService antes do save().

------------------------------------------------------------------------------------------------------------------------

7- Enums
Arquivos: Carro.java, PedidoAluguel.java, Aluguel.java
Sugestão de melhoria: Colocar cada enum em arquivo próprio.
Benefícios:
- Projeto mais organizado.
- Facilita reutilização.
Implementação: Criar arquivos separados para enums, ex: StatusCarro.java.

------------------------------------------------------------------------------------------------------------------------

8- Serviços de usuário
Arquivos: ClienteService.java, AgenteService.java, UsuarioAuthService.java
Sugestão de melhoria: Criar classe base UsuarioService com métodos comuns.
Benefícios:
- Menos código repetido.
- Manutenção mais fácil.
Implementação: Fazer os serviços específicos herdarem de UsuarioService.

------------------------------------------------------------------------------------------------------------------------

9- CarroRepository
Arquivo: CarroRepository.java
Sugestão de melhoria: Substituir concatenação de strings por métodos do Spring Data JPA.
Benefícios:
- Código mais legível.
- Mais seguro.
Implementação: Usar findByStatusAndMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(...).

------------------------------------------------------------------------------------------------------------------------

10- Cliente.java
Sugestão de melhoria: Trocar Double por BigDecimal para valores monetários.
Benefícios:
- Precisão em cálculos financeiros.
- Mantém padrão do projeto.
Implementação: Alterar rendimentosAuferidos para BigDecimal.

------------------------------------------------------------------------------------------------------------------------

11- TestController.java
Sugestão de melhoria: Controller de teste não deve ir para produção.
Benefícios:
- Mais segurança.
- Melhor organização.
Implementação: Adicionar @Profile("dev") ou remover do build de produção.

------------------------------------------------------------------------------------------------------------------------

12- Cliente.java (herança)
Sugestão de melhoria: Remover onCreate() e dataCadastro que já existem em Usuario.
Benefícios:
- Menos código repetido.
- Evita bugs.
Implementação: Apagar onCreate() e dataCadastro de Cliente.java.

------------------------------------------------------------------------------------------------------------------------

13- Boas práticas de nomenclatura
Benefícios:
- Variáveis seguem padrão Java (camelCase, constantes em UPPER_CASE).
- Código mais legível e consistente.
Exemplo: private static final String CEP_ORIGEM = "35650000"; (FreteService.java)

------------------------------------------------------------------------------------------------------------------------

14- Padrão PRG
Sugestão de melhoria: Revisar se todos os POST usam Post/Redirect/Get.
Benefícios:
- Evita reenvio de formulário.
- Padroniza comportamento.
Implementação: Verificar @PostMapping e usar redirect: com RedirectAttributes.

------------------------------------------------------------------------------------------------------------------------

15- Validação de dados
Sugestão de melhoria: Usar validações específicas para CPF, CEP, telefone.
Benefícios:
- Dados mais confiáveis.
- Mais seguro.
Implementação: Usar @Pattern, @CPF ou outras anotações específicas.

------------------------------------------------------------------------------------------------------------------------

16- Guia de estilo
Sugestão de melhoria: Criar convenção de nomes de variáveis e métodos.
Benefícios:
- Código padronizado e fácil de ler.
Implementação: Criar CONTRIBUTING.md com regras de estilo (camelCase, PascalCase, etc).

------------------------------------------------------------------------------------------------------------------------

17- Verificação de código limpo
Sugestão de melhoria: Integrar análise estática no build.
Benefícios:
- Código sempre limpo.
- Padronização.
Implementação: Usar Checkstyle ou PMD no Maven ou Gradle.

------------------------------------------------------------------------------------------------------------------------

18- Facade
Arquivos: PedidoAluguelController.java, AluguelService.java, ClienteService.java, CarroService.java
Sugestão de melhoria: Criar um Facade para centralizar operações de aprovação de aluguel, envolvendo vários serviços.
Benefícios:
- Controller mais simples e limpo.
- Reduz duplicação de código em múltiplos serviços.
- Facilita manutenção futura.
Implementação: Criar AprovarAluguelFacade com método executar(Aluguel aluguel) que chama CarroService, ClienteService e AluguelService de forma coordenada.

------------------------------------------------------------------------------------------------------------------------

19- Código morto
Arquivo: Cliente.java
Sugestão de melhoria: Remover constantes não utilizadas.
Benefícios:
- Código mais limpo.
Implementação: Apagar public static final int MAX_PROFISSAO = 3.

------------------------------------------------------------------------------------------------------------------------

20- README.md
Benefícios:
- Projeto já possui um README.
- Facilita entendimento inicial para novos devs.
- Sugestão de melhoria: detalhar como iniciar Spring Boot, dependências e pré-requisitos.
