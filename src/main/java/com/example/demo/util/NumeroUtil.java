package com.example.demo.util;

public final class NumeroUtil {

    private NumeroUtil() {
    }

    public static String somenteDigitos(String valor) {
        if (valor == null || valor.isBlank()) {
            return "";
        }
        return valor.replaceAll("\\D", "");
    }

    public static String somenteDigitosLimitado(String valor, int limite) {
        if (limite <= 0) {
            return "";
        }
        String digitos = somenteDigitos(valor);
        if (digitos.length() > limite) {
            return digitos.substring(0, limite);
        }
        return digitos;
    }

    public static Integer paraInteiro(String valor, int limite) {
        String digitos = somenteDigitosLimitado(valor, limite);
        if (digitos.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(digitos);
        } catch (NumberFormatException erro) {
            return 0;
        }
    }

    public static Long paraLong(String valor, int limite) {
        String digitos = somenteDigitosLimitado(valor, limite);
        if (digitos.isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(digitos);
        } catch (NumberFormatException erro) {
            return 0L;
        }
    }

    public static String inteiroParaTexto(Integer valor) {
        if (valor == null || valor == 0) {
            return "";
        }
        return String.valueOf(valor);
    }

    public static String longParaTexto(Long valor) {
        if (valor == null || valor == 0L) {
            return "";
        }
        return String.valueOf(valor);
    }
}
