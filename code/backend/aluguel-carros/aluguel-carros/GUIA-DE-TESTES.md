# 🧪 Guia de Testes - Sistema de Aluguel de Carros BrawnRent

## 📋 Pré-requisitos para Teste

### 1. Verificar Dependências
```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar PostgreSQL
psql --version
```

### 2. Configurar Banco de Dados
```sql
-- Conectar ao PostgreSQL
psql -U postgres

-- Executar o script de criação
\i database-setup.sql
```

## 🚀 Iniciando a Aplicação

### Opção 1: Script Automático (Recomendado)
```bash
# Windows
start.bat

# Linux/Mac
./start.sh
```

### Opção 2: Manual
```bash
# Compilar
mvn clean compile

# Executar
mvn spring-boot:run
```

### Verificar se está funcionando
- Acesse: `http://localhost:8080`
- Deve aparecer o dashboard com estatísticas zeradas

## 🧪 Cenários de Teste

### Teste 1: Dashboard Inicial
**Objetivo**: Verificar se a aplicação carrega corretamente

**Passos**:
1. Acesse `http://localhost:8080`
2. Verifique se aparece:
   - ✅ Logo "BrawnRent"
   - ✅ Menu de navegação
   - ✅ Estatísticas zeradas (0 clientes, 0 carros, etc.)
   - ✅ Botões de ação funcionais

**Resultado Esperado**: Dashboard carregado com interface moderna

---

### Teste 2: Cadastro de Clientes
**Objetivo**: Testar o CRUD completo de clientes

**Passos**:
1. Clique em "Clientes" no menu
2. Clique em "Novo Cliente"
3. Preencha o formulário:
   ```
   Nome: João Silva
   Email: joao@email.com
   Senha: 123456
   Telefone: (11) 99999-9999
   Endereço: Rua das Flores, 123
   RG: 12.345.678-9
   CPF: 123.456.789-00
   Profissão: Engenheiro
   Rendimentos: 5000.00
   ```
4. Clique em "Salvar"
5. Verifique se o cliente aparece na lista
6. Teste editar o cliente
7. Teste deletar o cliente

**Validações**:
- ✅ Máscaras funcionam (CPF, telefone)
- ✅ Validação de campos obrigatórios
- ✅ Mensagens de sucesso/erro
- ✅ Lista atualiza automaticamente

---

### Teste 3: Cadastro de Carros
**Objetivo**: Testar o CRUD completo de carros

**Passos**:
1. Clique em "Carros" no menu
2. Clique em "Novo Carro"
3. Preencha o formulário:
   ```
   Placa: ABC-1234
   Marca: Toyota
   Modelo: Corolla
   Ano: 2020
   Cor: Branco
   Valor Diária: 150.00
   Status: DISPONIVEL
   ```
4. Clique em "Salvar"
5. Cadastre mais 2-3 carros com dados diferentes
6. Teste os filtros por status
7. Teste a busca por marca/modelo

**Validações**:
- ✅ Máscara de placa funciona
- ✅ Status é salvo corretamente
- ✅ Filtros funcionam
- ✅ Busca funciona

---

### Teste 4: Cadastro de Aluguéis
**Objetivo**: Testar o fluxo completo de aluguel

**Passos**:
1. Clique em "Aluguéis" no menu
2. Clique em "Novo Aluguel"
3. Preencha o formulário:
   ```
   Cliente: [Selecione um cliente cadastrado]
   Carro: [Selecione um carro disponível]
   Data Início: [Data atual]
   Data Fim: [Data futura]
   Observações: Teste de aluguel
   ```
4. Clique em "Registrar Aluguel"
5. Verifique se o aluguel aparece na lista
6. Verifique se o status do carro mudou para "ALUGADO"

**Validações**:
- ✅ Apenas carros disponíveis aparecem
- ✅ Valor é calculado automaticamente
- ✅ Status do carro muda para ALUGADO
- ✅ Datas são validadas

---

### Teste 5: Finalização de Aluguel
**Objetivo**: Testar o processo de devolução

**Passos**:
1. Na lista de aluguéis, encontre um aluguel "ATIVO"
2. Clique no botão verde (✓) para finalizar
3. Confirme a ação
4. Verifique se:
   - Status mudou para "FINALIZADO"
   - Carro voltou para "DISPONIVEL"
   - Data de devolução foi registrada

**Validações**:
- ✅ Confirmação antes de finalizar
- ✅ Status atualiza corretamente
- ✅ Carro volta a ficar disponível

---

### Teste 6: Cancelamento de Aluguel
**Objetivo**: Testar o cancelamento de aluguel

**Passos**:
1. Crie um novo aluguel
2. Na lista, clique no botão vermelho (✗) para cancelar
3. Confirme a ação
4. Verifique se:
   - Status mudou para "CANCELADO"
   - Carro voltou para "DISPONIVEL"

**Validações**:
- ✅ Confirmação antes de cancelar
- ✅ Carro fica disponível novamente

---

### Teste 7: Busca e Filtros
**Objetivo**: Testar funcionalidades de busca

**Passos**:
1. **Clientes**: Use a busca por nome
2. **Carros**: Teste busca por marca/modelo e filtro por status
3. **Aluguéis**: Teste filtro por status
4. Verifique se os resultados são corretos

**Validações**:
- ✅ Busca funciona em tempo real
- ✅ Filtros retornam resultados corretos
- ✅ Interface atualiza corretamente

---

### Teste 8: Validações e Erros
**Objetivo**: Testar tratamento de erros

**Passos**:
1. Tente cadastrar cliente com CPF duplicado
2. Tente cadastrar carro com placa duplicada
3. Tente alugar carro já alugado
4. Deixe campos obrigatórios vazios
5. Use dados inválidos (email mal formatado, etc.)

**Validações**:
- ✅ Mensagens de erro aparecem
- ✅ Formulário não é submetido com dados inválidos
- ✅ Validações funcionam corretamente

---

### Teste 9: Responsividade
**Objetivo**: Testar em diferentes tamanhos de tela

**Passos**:
1. Redimensione a janela do navegador
2. Teste em modo mobile (F12 > Device Toolbar)
3. Verifique se todos os elementos são visíveis
4. Teste navegação em telas pequenas

**Validações**:
- ✅ Interface se adapta ao tamanho da tela
- ✅ Menu funciona em mobile
- ✅ Tabelas são responsivas

---

### Teste 10: Performance
**Objetivo**: Testar com múltiplos dados

**Passos**:
1. Cadastre 10+ clientes
2. Cadastre 10+ carros
3. Crie 10+ aluguéis
4. Teste navegação e busca com muitos dados
5. Verifique tempo de resposta

**Validações**:
- ✅ Sistema responde rapidamente
- ✅ Listas carregam sem problemas
- ✅ Busca funciona com muitos dados

## 🐛 Problemas Comuns e Soluções

### Erro de Conexão com Banco
```
Solução: Verificar se PostgreSQL está rodando
- Windows: services.msc > PostgreSQL
- Linux: sudo service postgresql start
```

### Erro de Porta em Uso
```
Solução: Mudar porta no application.properties
server.port=8081
```

### Erro de Compilação
```
Solução: Limpar cache do Maven
mvn clean
mvn compile
```

### Erro de Templates
```
Solução: Verificar se Thymeleaf está configurado
- Verificar pom.xml
- Verificar application.properties
```

## ✅ Checklist de Testes

- [ ] Dashboard carrega corretamente
- [ ] Cadastro de clientes funciona
- [ ] Cadastro de carros funciona
- [ ] Cadastro de aluguéis funciona
- [ ] Finalização de aluguel funciona
- [ ] Cancelamento de aluguel funciona
- [ ] Busca e filtros funcionam
- [ ] Validações funcionam
- [ ] Interface é responsiva
- [ ] Performance está adequada

## 📊 Relatório de Testes

Após executar todos os testes, documente:

1. **Funcionalidades que funcionam perfeitamente**
2. **Problemas encontrados**
3. **Sugestões de melhoria**
4. **Performance geral**
5. **Usabilidade da interface**

---

**🎯 Objetivo**: Garantir que todas as funcionalidades do sistema estão funcionando corretamente antes do uso em produção.
