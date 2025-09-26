package com.biblioteca.controller;

import com.biblioteca.model.Prestamo;
import com.biblioteca.service.PrestamoService;

import java.util.Date;
import java.util.List;

public class PrestamoController {
    private PrestamoService prestamoService = new PrestamoService();

    public void prestarEjemplar(int idUsuario, int idEjemplar, Date fechaDevolucion) {
        Prestamo p = new Prestamo(0, idUsuario, idEjemplar, new Date(), fechaDevolucion, "N");
        if (prestamoService.prestarEjemplar(p)) {
            System.out.println("Préstamo realizado.");
        } else {
            System.out.println("No se pudo prestar el ejemplar.");
        }
    }

    public void devolverEjemplar(int idPrestamo, int idEjemplar) {
        prestamoService.devolverEjemplar(idPrestamo, idEjemplar);
        System.out.println("Ejemplar devuelto.");
    }

    public List<Prestamo> listarPrestamos() {
        return prestamoService.listarPrestamos();
    }
}
