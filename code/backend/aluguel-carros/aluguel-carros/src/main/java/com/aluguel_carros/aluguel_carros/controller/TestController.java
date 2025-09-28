package com.aluguel_carros.aluguel_carros.controller;

import com.aluguel_carros.aluguel_carros.model.Agente;
import com.aluguel_carros.aluguel_carros.model.Aluguel;
import com.aluguel_carros.aluguel_carros.model.Carro;
import com.aluguel_carros.aluguel_carros.model.Cliente;
import com.aluguel_carros.aluguel_carros.services.AgenteService;
import com.aluguel_carros.aluguel_carros.services.CarroService;
import com.aluguel_carros.aluguel_carros.services.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final ClienteService clienteService;
    private final AgenteService agenteService;
    private final CarroService carroService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public Map<String, Object> testUsers() {
        Map<String, Object> result = new HashMap<>();
        
        // Testar clientes
        Cliente cliente = clienteService.buscarPorEmail("ana.silva@email.com").orElse(null);
        if (cliente != null) {
            result.put("cliente_encontrado", true);
            result.put("cliente_nome", cliente.getNome());
            result.put("cliente_senha_hash", cliente.getSenha());
            result.put("senha_123456_matches", passwordEncoder.matches("123456", cliente.getSenha()));
        } else {
            result.put("cliente_encontrado", false);
        }
        
        // Testar agentes
        Agente agente = agenteService.buscarPorEmail("joao.silva@empresa.com").orElse(null);
        if (agente != null) {
            result.put("agente_encontrado", true);
            result.put("agente_nome", agente.getNome());
            result.put("agente_senha_hash", agente.getSenha());
            result.put("senha_123456_matches_agente", passwordEncoder.matches("123456", agente.getSenha()));
        } else {
            result.put("agente_encontrado", false);
        }
        
        // Contar usuários
        result.put("total_clientes", clienteService.listarTodos().size());
        result.put("total_agentes", agenteService.listarTodos().size());
        
        return result;
    }

    @GetMapping("/aluguel")
    public Map<String, Object> testAluguel() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Testar se há carros disponíveis
            List<Carro> carrosDisponiveis = carroService.buscarDisponiveis();
            result.put("carros_disponiveis", carrosDisponiveis.size());
            
            if (!carrosDisponiveis.isEmpty()) {
                Carro primeiroCarro = carrosDisponiveis.get(0);
                result.put("primeiro_carro_id", primeiroCarro.getId());
                result.put("primeiro_carro_placa", primeiroCarro.getPlaca());
                result.put("primeiro_carro_status", primeiroCarro.getStatus());
            }
            
            // Testar se há clientes
            List<Cliente> clientes = clienteService.listarTodos();
            result.put("total_clientes", clientes.size());
            
            if (!clientes.isEmpty()) {
                Cliente primeiroCliente = clientes.get(0);
                result.put("primeiro_cliente_id", primeiroCliente.getId());
                result.put("primeiro_cliente_nome", primeiroCliente.getNome());
            }
            
            result.put("status", "OK");
            
        } catch (Exception e) {
            result.put("status", "ERRO");
            result.put("erro", e.getMessage());
            log.error("Erro no teste de aluguel", e);
        }
        
        return result;
    }

    @GetMapping("/carros")
    public Map<String, Object> testCarros() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("Testando busca de carros...");
            
            // Testar listar todos os carros
            List<Carro> todosCarros = carroService.listarTodos();
            result.put("total_carros", todosCarros.size());
            
            // Testar buscar carros disponíveis
            List<Carro> carrosDisponiveis = carroService.buscarDisponiveis();
            result.put("carros_disponiveis", carrosDisponiveis.size());
            
            // Detalhes dos carros
            if (!todosCarros.isEmpty()) {
                Carro primeiroCarro = todosCarros.get(0);
                result.put("primeiro_carro_id", primeiroCarro.getId());
                result.put("primeiro_carro_placa", primeiroCarro.getPlaca());
                result.put("primeiro_carro_status", primeiroCarro.getStatus());
                result.put("primeiro_carro_marca", primeiroCarro.getMarca());
                result.put("primeiro_carro_modelo", primeiroCarro.getModelo());
            }
            
            result.put("status", "OK");
            log.info("Teste de carros concluído com sucesso");
            
        } catch (Exception e) {
            result.put("status", "ERRO");
            result.put("erro", e.getMessage());
            log.error("Erro no teste de carros", e);
        }
        
        return result;
    }

    @GetMapping("/debug-aluguel")
    public Map<String, Object> debugAluguel() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("=== DEBUG ALUGUEL INICIADO ===");
            
            // Teste 1: Verificar se o DataInitializer executou
            List<Carro> todosCarros = carroService.listarTodos();
            result.put("total_carros_no_banco", todosCarros.size());
            log.info("Total de carros no banco: {}", todosCarros.size());
            
            // Teste 2: Verificar carros disponíveis
            List<Carro> carrosDisponiveis = carroService.buscarDisponiveis();
            result.put("carros_disponiveis", carrosDisponiveis.size());
            log.info("Carros disponíveis: {}", carrosDisponiveis.size());
            
            // Teste 3: Verificar se há clientes
            List<Cliente> clientes = clienteService.listarTodos();
            result.put("total_clientes", clientes.size());
            log.info("Total de clientes: {}", clientes.size());
            
            // Teste 4: Verificar se há agentes
            List<Agente> agentes = agenteService.listarTodos();
            result.put("total_agentes", agentes.size());
            log.info("Total de agentes: {}", agentes.size());
            
            // Teste 5: Criar um aluguel básico
            try {
                Aluguel aluguel = new Aluguel();
                aluguel.setDataInicio(LocalDateTime.now());
                result.put("aluguel_criado", true);
                log.info("Aluguel básico criado com sucesso");
            } catch (Exception e) {
                result.put("aluguel_criado", false);
                result.put("erro_criar_aluguel", e.getMessage());
                log.error("Erro ao criar aluguel básico", e);
            }
            
            result.put("status", "OK");
            log.info("=== DEBUG ALUGUEL CONCLUÍDO ===");
            
        } catch (Exception e) {
            result.put("status", "ERRO");
            result.put("erro", e.getMessage());
            log.error("Erro no debug de aluguel", e);
        }
        
        return result;
    }
}
