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
public class Categoria {
    private Long idCategoria;
    private String nombre;
    private String descripcion;
    private String estado; // 'Activa', 'Inactiva'
    private LocalDate fechaCreacion;
}