package com.example.demo.shared.navigation;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class AtributosGlobaisModelo {

    @ModelAttribute("modulos")
    public List<ModuloNavegacao> modulos() {
        return List.of(
                new ModuloNavegacao("Pedidos", "/pedidos"),
                new ModuloNavegacao("Novo Pedido", "/pedidos/novo"),
                new ModuloNavegacao("Google Drive", "/token")
        );
    }
}