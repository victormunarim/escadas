package com.example.demo.modulos;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("modulos")
    public List<ModuloNav> modulos() {
        return List.of(
                new ModuloNav("Dashboard", "/"),
                new ModuloNav("Pedidos", "/crud/pedidos")
        );
    }
}