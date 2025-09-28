package com.biblioteca.controller;

import com.biblioteca.model.Ejemplar;
import com.biblioteca.service.EjemplarService;

import java.util.List;

public class EjemplarController {
    private EjemplarService ejemplarService = new EjemplarService();

    public void registrarEjemplar(String titulo, String autor, String editorial, int anio, String categoria) {
        Ejemplar ejemplar = new Ejemplar(0, titulo, autor, editorial, anio, categoria, "Disponible");
        ejemplarService.registrarEjemplar(ejemplar);
        System.out.println("Ejemplar registrado.");
    }

    public List<Ejemplar> listarEjemplares() {
        return ejemplarService.listarEjemplares();
    }

    public Ejemplar buscarEjemplar(int id) {
        return ejemplarService.buscarEjemplar(id);
    }

    public void cambiarEstado(int id, String estado) {
        ejemplarService.cambiarEstadoEjemplar(id, estado);
        System.out.println("Estado actualizado.");
    }

    public void eliminarEjemplar(int id) {
        ejemplarService.eliminarEjemplar(id);
        System.out.println("Ejemplar eliminado.");
    }
}
