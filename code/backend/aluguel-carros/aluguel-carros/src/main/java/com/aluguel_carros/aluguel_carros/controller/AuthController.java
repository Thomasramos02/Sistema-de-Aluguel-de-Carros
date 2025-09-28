package com.aluguel_carros.aluguel_carros.controller;

import com.aluguel_carros.aluguel_carros.model.Agente;
import com.aluguel_carros.aluguel_carros.model.Cliente;
import com.aluguel_carros.aluguel_carros.model.UsuarioAuth;
import com.aluguel_carros.aluguel_carros.services.AgenteService;
import com.aluguel_carros.aluguel_carros.services.ClienteService;
import com.aluguel_carros.aluguel_carros.services.UsuarioAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final UsuarioAuthService usuarioAuthService;
    private final ClienteService clienteService;
    private final AgenteService agenteService;
    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                       @RequestParam(required = false) String logout,
                       Model model) {
        if (error != null) {
            model.addAttribute("error", "Email ou senha inválidos!");
        }
        if (logout != null) {
            model.addAttribute("success", "Logout realizado com sucesso!");
        }
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("usuario", new UsuarioAuth());
        return "auth/register";
    }
    
    @GetMapping("/register/cliente")
    public String registerCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "auth/register-cliente";
    }
    
    @PostMapping("/register/cliente")
    public String registerCliente(@Valid @ModelAttribute Cliente cliente,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register-cliente";
        }
        
        try {
            clienteService.salvar(cliente);
            redirectAttributes.addFlashAttribute("success", "Conta de cliente criada com sucesso! Faça login para continuar.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register/cliente";
        }
    }
    
    @GetMapping("/register/agente")
    public String registerAgente(Model model) {
        model.addAttribute("agente", new Agente());
        return "auth/register-agente";
    }
    
    @PostMapping("/register/agente")
    public String registerAgente(@Valid @ModelAttribute Agente agente,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register-agente";
        }
        
        try {
            agenteService.salvar(agente);
            redirectAttributes.addFlashAttribute("success", "Conta de agente criada com sucesso! Faça login para continuar.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register/agente";
        }
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UsuarioAuth usuario,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        try {
            usuarioAuthService.salvar(usuario);
            redirectAttributes.addFlashAttribute("success", "Conta criada com sucesso! Faça login para continuar.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
    
    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        // Tentar buscar como cliente primeiro
        Cliente cliente = clienteService.buscarPorEmail(email).orElse(null);
        if (cliente != null) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("tipoUsuario", "cliente");
            return "auth/profile-cliente";
        }
        
        // Se não for cliente, tentar como agente
        Agente agente = agenteService.buscarPorEmail(email).orElse(null);
        if (agente != null) {
            model.addAttribute("agente", agente);
            model.addAttribute("tipoUsuario", "agente");
            return "auth/profile-agente";
        }
        
        // Se não encontrar nenhum, redirecionar para login
        return "redirect:/login?error=usuario_nao_encontrado";
    }
    
    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute UsuarioAuth usuario,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/profile";
        }
        
        try {
            usuarioAuthService.atualizar(usuario);
            redirectAttributes.addFlashAttribute("success", "Perfil atualizado com sucesso!");
            return "redirect:/profile";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/profile";
        }
    }

    @GetMapping("/profile/cliente/editar")
    public String editarPerfilCliente(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        Cliente cliente = clienteService.buscarPorEmail(email)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        model.addAttribute("cliente", cliente);
        return "auth/editar-perfil-cliente";
    }

    @PostMapping("/profile/cliente/editar")
    public String atualizarPerfilCliente(@Valid @ModelAttribute Cliente cliente,
                                        BindingResult result,
                                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/editar-perfil-cliente";
        }

        try {
            // Verificar se o cliente está editando sua própria conta
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Cliente clienteAtual = clienteService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            
            // Manter o ID original
            cliente.setId(clienteAtual.getId());
            clienteService.atualizar(cliente);
            
            redirectAttributes.addFlashAttribute("success", "Perfil atualizado com sucesso!");
            return "redirect:/profile";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/profile/cliente/editar";
        }
    }
}
