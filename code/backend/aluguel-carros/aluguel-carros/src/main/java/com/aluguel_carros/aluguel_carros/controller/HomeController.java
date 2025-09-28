package com.aluguel_carros.aluguel_carros.controller;

import com.aluguel_carros.aluguel_carros.model.Cliente;
import com.aluguel_carros.aluguel_carros.model.Agente;
import com.aluguel_carros.aluguel_carros.services.AluguelService;
import com.aluguel_carros.aluguel_carros.services.CarroService;
import com.aluguel_carros.aluguel_carros.services.ClienteService;
import com.aluguel_carros.aluguel_carros.services.AgenteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    
    private final ClienteService clienteService;
    private final AgenteService agenteService;
    private final CarroService carroService;
    private final AluguelService aluguelService;
    
    @GetMapping("/")
    public String home(Model model) {
        try {
            log.info("Iniciando HomeController.home()");
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            log.info("Authentication: {}", auth);
            
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                String email = auth.getName();
                log.info("Email do usuário logado: {}", email);
                
                // Verificar se é cliente
                if (clienteService.buscarPorEmail(email).isPresent()) {
                    log.info("Usuário é cliente");
                    Cliente cliente = clienteService.buscarPorEmail(email).get();
                    log.info("Cliente encontrado: {}", cliente.getNome());
                    
                    // Estatísticas específicas para clientes (com tratamento de erro)
                    try {
                        long carrosDisponiveis = carroService.buscarDisponiveis().size();
                        log.info("Carros disponíveis: {}", carrosDisponiveis);
                        model.addAttribute("carrosDisponiveis", carrosDisponiveis);
                    } catch (Exception e) {
                        log.error("Erro ao buscar carros disponíveis", e);
                        model.addAttribute("carrosDisponiveis", 0);
                    }
                    
                    try {
                        long meusAlugueis = aluguelService.buscarPorCliente(cliente.getId()).size();
                        log.info("Aluguéis do cliente: {}", meusAlugueis);
                        model.addAttribute("meusAlugueis", meusAlugueis);
                    } catch (Exception e) {
                        log.error("Erro ao buscar aluguéis do cliente", e);
                        model.addAttribute("meusAlugueis", 0);
                    }
                    
                    model.addAttribute("cliente", cliente);
                    
                    log.info("Retornando index-cliente");
                    return "index-cliente";
                }
                
                // Verificar se é agente
                if (agenteService.buscarPorEmail(email).isPresent()) {
                    log.info("Usuário é agente");
                    // Estatísticas para agentes (página completa)
                    long totalClientes = clienteService.listarTodos().size();
                    long totalCarros = carroService.listarTodos().size();
                    long carrosDisponiveis = carroService.buscarDisponiveis().size();
                    long totalAlugueis = aluguelService.listarTodos().size();
                    long alugueisAtivos = aluguelService.buscarPorStatus(com.aluguel_carros.aluguel_carros.model.Aluguel.StatusAluguel.ATIVO).size();
                    
                    model.addAttribute("totalClientes", totalClientes);
                    model.addAttribute("totalCarros", totalCarros);
                    model.addAttribute("carrosDisponiveis", carrosDisponiveis);
                    model.addAttribute("totalAlugueis", totalAlugueis);
                    model.addAttribute("alugueisAtivos", alugueisAtivos);
                    
                    log.info("Retornando index");
                    return "index";
                }
                
                log.warn("Usuário logado mas não é cliente nem agente: {}", email);
            }
            
            log.info("Usuário não logado, redirecionando para login");
            return "redirect:/login";
            
        } catch (Exception e) {
            log.error("Erro no HomeController.home()", e);
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/test-cliente")
    public String testCliente(Model model) {
        try {
            log.info("Teste de cliente iniciado");
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            log.info("Authentication: {}", auth);
            
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                String email = auth.getName();
                log.info("Email: {}", email);
                
                if (clienteService.buscarPorEmail(email).isPresent()) {
                    Cliente cliente = clienteService.buscarPorEmail(email).get();
                    log.info("Cliente: {}", cliente.getNome());
                    
                    model.addAttribute("cliente", cliente);
                    model.addAttribute("carrosDisponiveis", 5);
                    model.addAttribute("meusAlugueis", 0);
                    
                    return "index-cliente";
                }
            }
            
            return "redirect:/login";
            
        } catch (Exception e) {
            log.error("Erro no teste de cliente", e);
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    
    @GetMapping("/test-simple")
    public String testSimple(Model model) {
        log.info("Teste simples iniciado");
        model.addAttribute("carrosDisponiveis", 3);
        model.addAttribute("meusAlugueis", 1);
        return "index-cliente";
    }

}
