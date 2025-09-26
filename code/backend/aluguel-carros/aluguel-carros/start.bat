@echo off
title BrawnRent - Sistema de Aluguel de Carros

echo.
echo 🚗 BrawnRent - Sistema de Aluguel de Carros
echo ==========================================
echo.

REM Verificar se o Java está instalado
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java não encontrado. Por favor, instale o Java 21+
    pause
    exit /b 1
)

REM Verificar se o Maven está instalado
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Maven não encontrado. Por favor, instale o Maven 3.6+
    pause
    exit /b 1
)

echo ✅ Pré-requisitos verificados!
echo.

REM Compilar o projeto
echo 🔨 Compilando o projeto...
call mvn clean compile

if %errorlevel% equ 0 (
    echo ✅ Compilação concluída com sucesso!
    echo.
    
    REM Executar a aplicação
    echo 🚀 Iniciando a aplicação...
    echo    Acesse: http://localhost:8080
    echo    Para parar: Ctrl+C
    echo.
    
    call mvn spring-boot:run
) else (
    echo ❌ Erro na compilação. Verifique os logs acima.
    pause
    exit /b 1
)

pause
