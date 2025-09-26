-- Script de criação do banco de dados para o Sistema de Aluguel de Carros - BrawnRent
-- Execute este script no PostgreSQL antes de iniciar a aplicação

-- Criar banco de dados
CREATE DATABASE "AluguelCarrosDB"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Portuguese_Brazil.1252'
    LC_CTYPE = 'Portuguese_Brazil.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Conectar ao banco criado
\c "AluguelCarrosDB";

-- Criar extensões necessárias (se não existirem)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Comentários sobre o banco
COMMENT ON DATABASE "AluguelCarrosDB" IS 'Banco de dados para o Sistema de Aluguel de Carros - BrawnRent';

-- Verificar se o banco foi criado corretamente
SELECT 'Banco de dados AluguelCarrosDB criado com sucesso!' as status;

-- Mostrar informações do banco
SELECT 
    datname as "Nome do Banco",
    pg_size_pretty(pg_database_size(datname)) as "Tamanho",
    datcollate as "Collate",
    datctype as "Ctype"
FROM pg_database 
WHERE datname = 'AluguelCarrosDB';
