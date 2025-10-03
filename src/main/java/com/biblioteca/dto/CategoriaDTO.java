package com.biblioteca.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO {
    private int idCategoria;
    private String nombre;
    private String descripcion;
    private String estado;
    private int cantidadLibros;
}