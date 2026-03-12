package com.example.demo.web;

import com.example.demo.shared.crud.ProvedorModuloCrud;
import com.example.demo.shared.crud.RegistroModuloCrud;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/crud")
public class ControladorCrud {

    private final RegistroModuloCrud registro;

    public ControladorCrud(RegistroModuloCrud registro) {
        this.registro = registro;
    }

    @GetMapping("/{chave}")
    public String crud(
        @PathVariable String chave,
        @RequestParam Map<String, String> parametros,
        Model model
    ) {
        ProvedorModuloCrud provedor = registro.obterObrigatorio(chave);
        Map<String, String> parametrosEfetivos = new java.util.HashMap<>(parametros);
        parametrosEfetivos.putIfAbsent("size", "50");

        List<Map<String, Object>> linhas = provedor.linhas(parametrosEfetivos);

        model.addAttribute("modulo", provedor.modulo());
        model.addAttribute("parametros", parametrosEfetivos);
        model.addAttribute("linhas", linhas);

        return "index";
    }
}
