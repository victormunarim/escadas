package com.example.demo.controller;

import com.example.demo.crud.CrudModuleProvider;
import com.example.demo.crud.CrudModuleRegistry;
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
public class CrudController {

    private final CrudModuleRegistry registry;

    public CrudController(CrudModuleRegistry registry) {
        this.registry = registry;
    }

    @GetMapping("/{key}")
    public String crud(
        @PathVariable String key,
        @RequestParam Map<String, String> params,
        Model model
    ) {
        CrudModuleProvider provider = registry.getRequired(key);

        List<Map<String, Object>> rows = provider.rows(params);

        model.addAttribute("module", provider.module());
        model.addAttribute("params", params);
        model.addAttribute("rows", rows);

        return "index";
    }
}