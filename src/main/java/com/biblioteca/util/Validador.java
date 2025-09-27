package com.biblioteca.util;

public class Validador {
    public static boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public static boolean esNumero(String texto) {
        if (esVacio(texto)) return false;
        try {
            Double.parseDouble(texto);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean esTexto(String texto) {
        return texto != null && texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }

    public static boolean validarTipoUsuario(String tipo) {
        return tipo.equalsIgnoreCase("Alumno") ||
               tipo.equalsIgnoreCase("Docente") ||
               tipo.equalsIgnoreCase("Administrativo");
    }
}