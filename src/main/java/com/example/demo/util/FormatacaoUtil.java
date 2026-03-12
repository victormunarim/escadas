package com.example.demo.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.Normalizer;
import java.util.Locale;

public final class FormatacaoUtil {
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private FormatacaoUtil() {
    }

    public static String formatarCep(Integer cep) {
        if (cep == null || cep == 0) {
            return "";
        }
        String oitoDigitos = String.format("%08d", cep);
        return oitoDigitos.substring(0, 5) + "-" + oitoDigitos.substring(5);
    }

    public static String formatarRg(Integer rg) {
        if (rg == null || rg == 0) {
            return "";
        }
        String noveDigitos = String.format("%09d", rg);
        return noveDigitos.substring(0, 2) + "."
                + noveDigitos.substring(2, 5) + "."
                + noveDigitos.substring(5, 8) + "-"
                + noveDigitos.substring(8);
    }

    public static String formatarCpf(Long cpf) {
        if (cpf == null || cpf == 0L) {
            return "";
        }
        String onzeDigitos = String.format("%011d", cpf);
        return onzeDigitos.substring(0, 3) + "."
                + onzeDigitos.substring(3, 6) + "."
                + onzeDigitos.substring(6, 9) + "-"
                + onzeDigitos.substring(9);
    }

    public static String formatarCnpj(String cnpj) {
        String digitos = NumeroUtil.somenteDigitos(cnpj);
        if (digitos.isEmpty()) {
            return "";
        }
        if (digitos.length() < 14) {
            digitos = String.format("%14s", digitos).replace(' ', '0');
        } else if (digitos.length() > 14) {
            digitos = digitos.substring(digitos.length() - 14);
        }
        return digitos.substring(0, 2) + "."
                + digitos.substring(2, 5) + "."
                + digitos.substring(5, 8) + "/"
                + digitos.substring(8, 12) + "-"
                + digitos.substring(12);
    }

    public static String formatarTelefoneCelular(String telefone) {
        String digitos = NumeroUtil.somenteDigitos(telefone);
        if (digitos.isEmpty()) {
            return "";
        }
        if (digitos.length() > 11) {
            digitos = digitos.substring(digitos.length() - 11);
        }
        if (digitos.length() < 11) {
            digitos = String.format("%11s", digitos).replace(' ', '0');
        }
        return "(" + digitos.substring(0, 2) + ") "
                + digitos.substring(2, 7) + "-"
                + digitos.substring(7);
    }

    public static String formatarTelefoneFixo(String telefone) {
        String digitos = NumeroUtil.somenteDigitos(telefone);
        if (digitos.isEmpty()) {
            return "";
        }
        if (digitos.length() > 10) {
            digitos = digitos.substring(digitos.length() - 10);
        }
        if (digitos.length() < 10) {
            digitos = String.format("%10s", digitos).replace(' ', '0');
        }
        return "(" + digitos.substring(0, 2) + ") "
                + digitos.substring(2, 6) + "-"
                + digitos.substring(6);
    }

    public static String formatarValor(BigDecimal valor) {
        if (valor == null) {
            return "";
        }
        return criarFormatoMoeda().format(valor);
    }

    public static String formatarTexto(Object valor) {
        if (valor == null) {
            return "-";
        }

        String texto = valor.toString().trim();
        if (texto.isEmpty()) {
            return "-";
        }

        return texto;
    }

    public static String formatarSimNao(Boolean valor) {
        if (Boolean.TRUE.equals(valor)) {
            return "Sim";
        }
        if (Boolean.FALSE.equals(valor)) {
            return "Não";
        }
        return "-";
    }

    public static String formatarDataHora(LocalDateTime dataHora) {
        if (dataHora == null) {
            return "-";
        }
        return dataHora.format(FORMATO_DATA_HORA);
    }

    public static String nomePastaPedido(String nomeCliente, Integer numeroPedido) {
        String cliente = formatarTexto(nomeCliente);
        if ("-".equals(cliente)) {
            cliente = "Cliente";
        }

        cliente = Normalizer.normalize(cliente, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .replaceAll("[^a-zA-Z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s+", " ");

        if (cliente.isBlank()) {
            cliente = "Cliente";
        }

        String numero = numeroPedido == null ? "SemNumero" : String.valueOf(numeroPedido);
        return cliente + " - Pedido " + numero;
    }

    private static DecimalFormat criarFormatoMoeda() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("pt-BR"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        return new DecimalFormat("#,##0.00", symbols);
    }
}
