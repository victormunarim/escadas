package com.example.demo.localidades.controller;

import com.example.demo.localidades.service.ConsultaLocalidadesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LocalidadesRestController {

    private final ConsultaLocalidadesService consultaLocalidadesService;

    public LocalidadesRestController(ConsultaLocalidadesService consultaLocalidadesService) {
        this.consultaLocalidadesService = consultaLocalidadesService;
    }

    @GetMapping("/api/localidades/ufs")
    public List<String> getUfs() {
        return consultaLocalidadesService.buscarUfs();
    }

    @GetMapping("/api/localidades/municipios")
    public List<String> getMunicipios(
            @RequestParam(defaultValue = "") String termo,
            @RequestParam(defaultValue = "SC") String uf,
            @RequestParam(defaultValue = "500") int limite
    ) {
        return consultaLocalidadesService.buscarMunicipios(termo, uf, limite);
    }

    @GetMapping("/api/localidades/bairros")
    public List<String> getBairros(
            @RequestParam(defaultValue = "") String termo,
            @RequestParam(defaultValue = "SC") String uf,
            @RequestParam(defaultValue = "500") int limite
    ) {
        return consultaLocalidadesService.buscarBairros(termo, uf, limite);
    }
}
