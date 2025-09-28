-- Script para criar as novas tabelas do sistema de aluguel de carros
-- Execute este script após as tabelas existentes

-- Tabela de Agentes
CREATE TABLE IF NOT EXISTS agentes (
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

-- Tabela de Pedidos de Aluguel
CREATE TABLE IF NOT EXISTS pedidos_aluguel (
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

-- Inserir alguns agentes de exemplo
INSERT INTO agentes (nome, email, senha, codigo_agente, departamento, data_admissao) VALUES
('João Silva', 'joao.silva@empresa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'AGT001', 'Vendas', NOW()),
('Maria Santos', 'maria.santos@empresa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'AGT002', 'Atendimento', NOW()),
('Pedro Costa', 'pedro.costa@empresa.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'AGT003', 'Gerência', NOW());

-- Atualizar tabela de usuários_auth para incluir role FUNCIONARIO
-- (Se necessário, adicione a role FUNCIONARIO ao enum Role)
