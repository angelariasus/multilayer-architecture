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
public class Ejemplar {
    private Long idEjemplar;
    private Long idLibro;
    private String codigoEjemplar;
    private String ubicacion;
    private String estado; // 'Disponible', 'Prestado', 'Reservado', 'En reparación', 'Perdido', 'Dado de baja'
    private LocalDate fechaAdquisicion;
    private String observaciones;
    private String estadoFisico; // 'Excelente', 'Bueno', 'Regular', 'Malo'
}