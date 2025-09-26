package com.aluguel_carros.aluguel_carros.controller;

import com.aluguel_carros.aluguel_carros.model.UsuarioAuth;
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
        
        UsuarioAuth usuario = usuarioAuthService.buscarPorEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        model.addAttribute("usuario", usuario);
        return "auth/profile";
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
}
