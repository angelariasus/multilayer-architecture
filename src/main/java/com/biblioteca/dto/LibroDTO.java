package com.biblioteca.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroDTO {
    private int idLibro;
    private String titulo;
    private String autor;
    private String isbn;
    private String categoria;
    private int cantidadDisponible;
    private int cantidadTotal;
    private String estado;
    private String fechaPublicacion;
    private String editorial;
}