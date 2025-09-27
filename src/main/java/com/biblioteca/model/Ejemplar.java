package com.biblioteca.model;
import lombok.Data;

@Data    
public class Ejemplar {
    private int idEjemplar;
    private String titulo;
    private String autor;
    private String editorial;
    private int anio;
    private String categoria;
    private String estado;
}
