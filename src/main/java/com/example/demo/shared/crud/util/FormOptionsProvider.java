package com.example.demo.shared.crud.util;

import com.example.demo.shared.crud.OpcaoCrud;
import java.util.ArrayList;
import java.util.List;

public class FormOptionsProvider {

    public static List<OpcaoCrud> criarOpcoesDias() {
        List<OpcaoCrud> opcoes = new ArrayList<>();
        opcoes.add(new OpcaoCrud("", "Selecione"));
        for (int i = 1; i <= 31; i++) {
            String val = String.valueOf(i);
            opcoes.add(new OpcaoCrud(val, val));
        }
        return opcoes;
    }

    public static List<OpcaoCrud> criarOpcoesMeses() {
        return List.of(
                new OpcaoCrud("", "Selecione"),
                new OpcaoCrud("1", "Janeiro"),
                new OpcaoCrud("2", "Fevereiro"),
                new OpcaoCrud("3", "Março"),
                new OpcaoCrud("4", "Abril"),
                new OpcaoCrud("5", "Maio"),
                new OpcaoCrud("6", "Junho"),
                new OpcaoCrud("7", "Julho"),
                new OpcaoCrud("8", "Agosto"),
                new OpcaoCrud("9", "Setembro"),
                new OpcaoCrud("10", "Outubro"),
                new OpcaoCrud("11", "Novembro"),
                new OpcaoCrud("12", "Dezembro")
        );
    }

    public static List<OpcaoCrud> criarOpcoesAnos() {
        List<OpcaoCrud> opcoes = new ArrayList<>();
        opcoes.add(new OpcaoCrud("", "Selecione"));
        for (int i = 2020; i <= 2030; i++) {
            String val = String.valueOf(i);
            opcoes.add(new OpcaoCrud(val, val));
        }
        return opcoes;
    }

    public static List<OpcaoCrud> criarOpcoesTamanhoPagina() {
        return List.of(
                new OpcaoCrud("50", "50"),
                new OpcaoCrud("100", "100"),
                new OpcaoCrud("200", "200"),
                new OpcaoCrud("500", "500")
        );
    }

    public static int tamanhoPagina(String valor) {
        if (valor == null || valor.isBlank()) {
            return 50;
        }
        return switch (valor.trim()) {
            case "50" -> 50;
            case "100" -> 100;
            case "200" -> 200;
            case "500" -> 500;
            default -> 50;
        };
    }
}
