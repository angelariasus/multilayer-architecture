package com.biblioteca.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDTO {
    private Long idCategoria;
    private String nombre;
    private String descripcion;
    private String estado;
    private LocalDate fechaCreacion;
    private Integer totalLibros; // Campo adicional para mostrar cantidad de libros
}