-- Script para criar usuário administrador padrão
-- Execute este script após criar o banco de dados

-- Inserir usuário administrador padrão
INSERT INTO usuarios_auth (nome, email, senha, role, data_cadastro, ativo) 
VALUES (
    'Administrador', 
    'admin@brawnrent.com', 
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', -- senha: admin123
    'ADMIN', 
    NOW(), 
    true
);

-- Inserir usuário funcionário padrão
INSERT INTO usuarios_auth (nome, email, senha, role, data_cadastro, ativo) 
VALUES (
    'Funcionário', 
    'funcionario@brawnrent.com', 
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', -- senha: admin123
    'FUNCIONARIO', 
    NOW(), 
    true
);

-- Verificar se os usuários foram criados
SELECT id, nome, email, role, ativo, data_cadastro 
FROM usuarios_auth 
ORDER BY data_cadastro;
