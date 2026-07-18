package com.example.demo.shared.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping(value = {
        "/",
        "/login",
        "/pedidos",
        "/pedidos/novo",
        "/pedidos/{id}/editar",
        "/pedidos/{id}/visualizar",
        "/orcamentos",
        "/orcamentos/novo",
        "/orcamentos/{id}/editar",
        "/orcamentos/{id}/visualizar",
        "/token"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
