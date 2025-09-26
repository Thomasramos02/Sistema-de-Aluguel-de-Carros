#!/bin/bash

# Script de inicialização do Sistema de Aluguel de Carros - BrawnRent
# Autor: Sistema de Aluguel de Carros
# Data: 2024

echo "🚗 BrawnRent - Sistema de Aluguel de Carros"
echo "=========================================="
echo ""

# Verificar se o Java está instalado
if ! command -v java &> /dev/null; then
    echo "❌ Java não encontrado. Por favor, instale o Java 21+"
    exit 1
fi

# Verificar se o Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven não encontrado. Por favor, instale o Maven 3.6+"
    exit 1
fi

# Verificar se o PostgreSQL está rodando
if ! pg_isready -h localhost -p 5432 &> /dev/null; then
    echo "⚠️  PostgreSQL não está rodando. Por favor, inicie o PostgreSQL"
    echo "   Comando sugerido: sudo service postgresql start"
    exit 1
fi

echo "✅ Pré-requisitos verificados!"
echo ""

# Compilar o projeto
echo "🔨 Compilando o projeto..."
mvn clean compile

if [ $? -eq 0 ]; then
    echo "✅ Compilação concluída com sucesso!"
    echo ""
    
    # Executar a aplicação
    echo "🚀 Iniciando a aplicação..."
    echo "   Acesse: http://localhost:8080"
    echo "   Para parar: Ctrl+C"
    echo ""
    
    mvn spring-boot:run
else
    echo "❌ Erro na compilação. Verifique os logs acima."
    exit 1
fi
