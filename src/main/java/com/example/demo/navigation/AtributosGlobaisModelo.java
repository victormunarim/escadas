package com.example.demo.navigation;

import com.example.demo.security.UsuarioAutenticado;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class AtributosGlobaisModelo {

    @ModelAttribute("modulos")
    public List<ModuloNavegacao> modulos() {
        return List.of(
                new ModuloNavegacao("Pedidos", "/crud/pedidos"),
                new ModuloNavegacao("Novo Pedido", "/pedidos/novo"),
                new ModuloNavegacao("Google Drive", "/token")
        );
    }

    @ModelAttribute("usuarioLogadoNome")
    public String usuarioLogadoNome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UsuarioAutenticado usuarioAutenticado) {
            return usuarioAutenticado.nome();
        }

        return authentication.getName();
    }
}
