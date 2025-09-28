# 🚗 BrawnRent - Sistema de Aluguel de Carros

<div align="center">

**Sistema completo para gestão de aluguel de veículos com controle de clientes, frota e contratos**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.0-green.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-11-orange.svg)](https://www.oracle.com/java/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.0-blue.svg)](https://www.thymeleaf.org/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple.svg)](https://getbootstrap.com/)

</div>

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Como Executar](#-como-executar)
- [Histórias de Usuário](#-histórias-de-usuário)


---

## 🎯 Sobre o Projeto

O **BrawnRent** é um sistema web para gerenciamento de aluguel de carros, oferecendo uma plataforma moderna para clientes e agentes. Permite controle completo da frota de veículos, gestão de clientes e processamento de pedidos de aluguel.

---

## ✨ Funcionalidades

### 👤 Para Clientes
- ✅ Cadastro e Login
- ✅ Visualização de Carros disponíveis
- ✅ Solicitação de Aluguéis
- ✅ Acompanhamento de Pedidos
- ✅ Gestão de Perfil
- ✅ Histórico de Aluguéis

### 🏢 Para Agentes
- ✅ Gestão de Clientes (CRUD)
- ✅ Controle da Frota de veículos
- ✅ Análise de Pedidos de aluguel
- ✅ Aprovação/Rejeição de contratos
- ✅ Dashboard Administrativo
- ✅ Controle de Status

---

## 🛠 Tecnologias

### Backend
- **Java 11** - Linguagem principal
- **Spring Boot 2.7.0** - Framework web
- **Spring Security** - Autenticação
- **Spring Data JPA** - Persistência
- **Hibernate** - ORM
- **Maven** - Dependências

### Frontend
- **Thymeleaf** - Templates
- **Bootstrap 5.3.0** - Interface responsiva
- **Font Awesome 6.0** - Ícones
- **CSS3** - Estilização customizada

### Banco de Dados
- **H2 Database** - Banco em memória

---

## 🚀 Como Executar

### Pré-requisitos
- Java 11 ou superior
- Maven 3.6 ou superior

### Instalação
```bash
# Clone o repositório
git clone https://github.com/seu-usuario/Sistema-de-Aluguel-de-Carros.git
cd Sistema-de-Aluguel-de-Carros/code/backend/aluguel-carros/aluguel-carros

# Execute o projeto
mvn spring-boot:run
```

### Acesso
- **URL**: http://localhost:8080
- **Login Cliente**: Use as credenciais do cadastro
- **Login Agente**: Use as credenciais de agente

---

## 📖 Histórias de Usuário

### 👤 Cliente
- Como cliente, quero me cadastrar no sistema para poder utilizar os serviços de aluguel
- Como cliente, quero introduzir um pedido de aluguel para alugar um automóvel
- Como cliente, quero modificar um pedido de aluguel já feito para corrigir informações
- Como cliente, quero consultar meus pedidos de aluguel para acompanhar o status
- Como cliente, quero cancelar um pedido de aluguel para desistir da solicitação

### 🏢 Agente
- Como agente, quero modificar pedidos de aluguel para ajustar informações
- Como agente, quero avaliar financeiramente um pedido para decidir aprovação
- Como agente, quero gerenciar a frota de veículos para controlar disponibilidade
- Como agente, quero visualizar estatísticas do sistema para acompanhar desempenho

---

## 👥 Contribuidores

| **Desenvolvedor** | **GitHub** |
|:-----------------:|:----------:|
| **Cauê Afonso Moraes** | [@caue-moraes](https://github.com/caue-moraes) |
| **Vitor Veiga Silva** | [@vitor-veiga](https://github.com/vitor-veiga) |

---

<div align="center">

**Desenvolvido com ❤️ para a disciplina de Laboratório de Desenvolvimento de Software**

*4° Período - Engenharia de Software*

</div>