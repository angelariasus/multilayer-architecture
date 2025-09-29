package com.biblioteca.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Fechas {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static String formatear(Date fecha) {
        if (fecha == null) return "";
        return sdf.format(fecha);
    }

    public static Date sumarDias(Date fecha, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.DAY_OF_YEAR, dias);
        return cal.getTime();
    }

    public static long diferenciaDias(Date inicio, Date fin) {
        long diff = fin.getTime() - inicio.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    public static boolean estaVencida(Date fechaDevolucion) {
        Date hoy = new Date();
        return hoy.after(fechaDevolucion);
    }

    public static boolean validarFecha(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            return false;
        }
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // fuerza validación estricta
            sdf.parse(fecha.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
