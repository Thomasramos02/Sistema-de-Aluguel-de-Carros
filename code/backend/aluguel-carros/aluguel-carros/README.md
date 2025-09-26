# Sistema de Aluguel de Carros - BrawnRent

Sistema completo para gestão de aluguel de carros desenvolvido com Spring Boot e Thymeleaf.

## 🚀 Funcionalidades

### ✅ Implementadas
- **Gestão de Clientes**: Cadastro, edição, visualização e exclusão de clientes
- **Gestão de Carros**: Controle da frota com status (Disponível, Alugado, Manutenção, Indisponível)
- **Gestão de Aluguéis**: Registro de aluguéis com cálculo automático de valores
- **Interface Moderna**: Design responsivo com Bootstrap 5 e Font Awesome
- **Validação de Dados**: Validação completa de formulários
- **Relatórios**: Dashboard com estatísticas do sistema

### 🎯 Principais Recursos
- **Dashboard Interativo**: Visão geral do sistema com estatísticas
- **Busca e Filtros**: Pesquisa avançada em todas as entidades
- **Máscaras de Input**: Formatação automática de CPF, telefone e placa
- **Status Dinâmico**: Controle de status de carros e aluguéis
- **Cálculo Automático**: Valor total calculado automaticamente baseado na diária

## 🛠️ Tecnologias Utilizadas

- **Backend**: Spring Boot 3.5.5, Java 21
- **Frontend**: Thymeleaf, Bootstrap 5, Font Awesome
- **Banco de Dados**: PostgreSQL
- **ORM**: JPA/Hibernate
- **Build**: Maven
- **Validação**: Bean Validation

## 📋 Pré-requisitos

- Java 21+
- Maven 3.6+
- PostgreSQL 12+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## 🚀 Como Executar

### 1. Configurar Banco de Dados
```sql
-- Criar banco de dados
CREATE DATABASE "AluguelCarrosDB";

-- Criar usuário (opcional)
CREATE USER postgres WITH PASSWORD 'Veiga@3321';
GRANT ALL PRIVILEGES ON DATABASE "AluguelCarrosDB" TO postgres;
```

### 2. Criar Usuários Padrão (Após primeira execução)
```sql
-- Execute o arquivo create-admin-users.sql
-- Ou execute os comandos SQL diretamente no PostgreSQL
```

### 3. Configurar Application Properties
Edite o arquivo `src/main/resources/application.properties`:
```properties
# Ajuste conforme sua configuração do PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/AluguelCarrosDB
spring.datasource.username=postgres
spring.datasource.password=Veiga@3321
```

### 4. Executar a Aplicação
```bash
# Via Maven
mvn spring-boot:run

# Ou compilar e executar
mvn clean package
java -jar target/aluguel-carros-0.0.1-SNAPSHOT.jar
```

### 5. Acessar a Aplicação
Abra o navegador e acesse: `http://localhost:8080`

**🔐 Credenciais Padrão:**
- **Admin:** admin@brawnrent.com / admin123
- **Funcionário:** funcionario@brawnrent.com / admin123

## 📱 Interface do Sistema

### Dashboard Principal
- Estatísticas em tempo real
- Acesso rápido às principais funcionalidades
- Design moderno e responsivo

### Gestão de Clientes
- **Lista de Clientes**: Visualização completa com busca
- **Cadastro**: Formulário completo com validação
- **Edição**: Atualização de dados existentes
- **Exclusão**: Remoção segura com confirmação

### Gestão de Carros
- **Lista de Carros**: Frota completa com filtros
- **Cadastro**: Informações detalhadas do veículo
- **Controle de Status**: Disponibilidade em tempo real
- **Busca Avançada**: Por marca, modelo ou placa

### Gestão de Aluguéis
- **Novo Aluguel**: Seleção de cliente e carro disponível
- **Controle de Datas**: Validação de períodos
- **Cálculo Automático**: Valor baseado na diária
- **Status**: Ativo, Finalizado, Cancelado

## 🗂️ Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/aluguel_carros/aluguel_carros/
│   │   ├── config/          # Configurações
│   │   ├── controller/      # Controllers MVC
│   │   ├── model/           # Entidades JPA
│   │   ├── repositories/     # Repositories JPA
│   │   └── services/        # Lógica de negócio
│   └── resources/
│       ├── templates/        # Templates Thymeleaf
│       └── application.properties
└── test/                    # Testes unitários
```

## 🔧 Configurações Avançadas

### Logs
O sistema está configurado para mostrar logs detalhados:
- SQL queries executadas
- Debug da aplicação
- Rastreamento de requisições

### Validação
- Validação de formulários com Bean Validation
- Mensagens de erro personalizadas
- Validação de unicidade (CPF, RG, Email, Placa)

### Segurança
- Configuração básica de segurança
- CSRF desabilitado para desenvolvimento
- Todas as rotas públicas (configurável)

## 📊 Banco de Dados

### Entidades Principais
- **Usuario**: Classe base abstrata
- **Cliente**: Herda de Usuario com dados específicos
- **Carro**: Informações do veículo e status
- **Aluguel**: Relacionamento entre Cliente e Carro

### Relacionamentos
- Cliente 1:N Aluguel
- Carro 1:N Aluguel
- Aluguel N:1 Cliente
- Aluguel N:1 Carro

## 🚀 Próximos Passos

### Funcionalidades Futuras
- [ ] Sistema de autenticação e autorização
- [ ] Relatórios em PDF
- [ ] Notificações por email
- [ ] API REST completa
- [ ] Testes automatizados
- [ ] Deploy em nuvem

### Melhorias Sugeridas
- [ ] Cache com Redis
- [ ] Logs estruturados
- [ ] Monitoramento com Actuator
- [ ] Documentação com Swagger
- [ ] CI/CD pipeline

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 👨‍💻 Desenvolvedor

Desenvolvido como parte do Laboratório 02 - Sistema de Aluguel de Carros.

---

**BrawnRent** - Sistema completo para gestão de aluguel de carros 🚗
