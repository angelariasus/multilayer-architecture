package com.biblioteca.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    private int idCategoria;
    private String nombre;
    private String descripcion;
    private String estado;
    private int cantidadLibros;
}