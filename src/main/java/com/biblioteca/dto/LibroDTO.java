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
public class LibroDTO {
    private Long idLibro;
    private String titulo;
    private String autor;
    private String isbn;
    private Long idCategoria;
    private String nombreCategoria; // Campo adicional para mostrar el nombre de la categoría
    private Integer cantidadDisponible;
    private Integer cantidadTotal;
    private String estado;
    private LocalDate fechaPublicacion;
    private String editorial;
    private String descripcion;
    private String ubicacion;
    private LocalDate fechaRegistro;
    private Integer ejemplaresDisponibles; // Campo adicional
    private Integer vecesPrestado; // Campo adicional para estadísticas
}