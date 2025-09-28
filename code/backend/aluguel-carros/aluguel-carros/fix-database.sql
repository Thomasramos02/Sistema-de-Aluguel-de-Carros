-- Script para corrigir problemas no banco de dados

-- 1. Remover chave estrangeira problemática da tabela agentes
ALTER TABLE agentes DROP CONSTRAINT IF EXISTS fkl5p21bxuuannthjtopb8f4hye;

-- 2. Recriar tabela agentes sem chave estrangeira
DROP TABLE IF EXISTS agentes CASCADE;

CREATE TABLE agentes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    codigo_agente VARCHAR(50) UNIQUE NOT NULL,
    departamento VARCHAR(255) NOT NULL,
    data_admissao TIMESTAMP,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- 3. Recriar tabela pedidos_aluguel
DROP TABLE IF EXISTS pedidos_aluguel CASCADE;

CREATE TABLE pedidos_aluguel (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    carro_id BIGINT NOT NULL,
    agente_id BIGINT,
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP NOT NULL,
    valor_total DECIMAL(10,2),
    status ENUM('PENDENTE', 'APROVADO', 'NEGADO') NOT NULL DEFAULT 'PENDENTE',
    observacoes_cliente TEXT,
    observacoes_agente TEXT,
    data_solicitacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_analise TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (carro_id) REFERENCES carros(id),
    FOREIGN KEY (agente_id) REFERENCES agentes(id)
);

-- 4. Inserir alguns agentes de exemplo
INSERT INTO agentes (nome, email, senha, codigo_agente, departamento, data_admissao) VALUES
('João Silva', 'joao.silva@empresa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'AGT001', 'Vendas', NOW()),
('Maria Santos', 'maria.santos@empresa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'AGT002', 'Atendimento', NOW()),
('Pedro Costa', 'pedro.costa@empresa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'AGT003', 'Gerência', NOW());

-- 5. Inserir alguns clientes de exemplo
INSERT INTO clientes (nome, email, senha, telefone, endereco, rg, cpf, profissao, rendimentos_auferidos, data_cadastro) VALUES
('Ana Silva', 'ana.silva@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '(11) 99999-9999', 'Rua das Flores, 123 - São Paulo/SP', '12.345.678-9', '123.456.789-00', 'Engenheira', 5000.00, NOW()),
('Carlos Santos', 'carlos.santos@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', '(11) 88888-8888', 'Av. Paulista, 456 - São Paulo/SP', '98.765.432-1', '987.654.321-00', 'Advogado', 8000.00, NOW());

-- 6. Inserir alguns carros de exemplo
INSERT INTO carros (placa, modelo, marca, cor, ano, valor_diaria, status, data_cadastro) VALUES
('ABC-1234', 'Corolla', 'Toyota', 'Prata', 2023, 150.00, 'DISPONIVEL', NOW()),
('DEF-5678', 'Civic', 'Honda', 'Branco', 2022, 140.00, 'DISPONIVEL', NOW()),
('GHI-9012', 'HB20', 'Hyundai', 'Preto', 2023, 120.00, 'DISPONIVEL', NOW());
