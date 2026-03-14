package com.example.demo.shared.crud.filtros;

import com.example.demo.shared.crud.OpcaoCrud;

import java.util.ArrayList;
import java.util.List;

public final class OpcoesDataCrud {

    public static final List<OpcaoCrud> DIAS;
    public static final List<OpcaoCrud> MESES;
    public static final List<OpcaoCrud> ANOS;

    private static final int ANO_INICIAL = 2022;
    private static final int ANO_FINAL = 2030;

    private static final String[] NOMES_MESES = {
            "Janeiro",
            "Fevereiro",
            "Março",
            "Abril",
            "Maio",
            "Junho",
            "Julho",
            "Agosto",
            "Setembro",
            "Outubro",
            "Novembro",
            "Dezembro"
    };

    static {
        List<OpcaoCrud> dias = new ArrayList<>();
        for (int dia = 1; dia <= 31; dia++) {
            String valor = String.valueOf(dia);
            dias.add(new OpcaoCrud(valor, valor));
        }
        DIAS = List.copyOf(dias);

        List<OpcaoCrud> meses = new ArrayList<>();
        for (int mes = 1; mes <= 12; mes++) {
            meses.add(new OpcaoCrud(String.valueOf(mes), NOMES_MESES[mes - 1]));
        }
        MESES = List.copyOf(meses);

        List<OpcaoCrud> anos = new ArrayList<>();
        for (int ano = ANO_INICIAL; ano <= ANO_FINAL; ano++) {
            String valor = String.valueOf(ano);
            anos.add(new OpcaoCrud(valor, valor));
        }
        ANOS = List.copyOf(anos);
    }

    private OpcoesDataCrud() {}
}
