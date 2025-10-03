package com.biblioteca.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Libro {
    private Long idLibro;
    private String titulo;
    private String autor;
    private String isbn;
    private Long idCategoria;
    private Integer cantidadDisponible;
    private Integer cantidadTotal;
    private String estado; // 'Disponible', 'Agotado', 'Fuera de circulación'
    private LocalDate fechaPublicacion;
    private String editorial;
    private String descripcion;
    private String ubicacion;
    private LocalDate fechaRegistro;
}