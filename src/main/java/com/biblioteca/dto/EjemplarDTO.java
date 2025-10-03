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
public class EjemplarDTO {
    private Long idEjemplar;
    private Long idLibro;
    private String tituloLibro; // Campo adicional para mostrar el título del libro
    private String codigoEjemplar;
    private String ubicacion;
    private String estado;
    private LocalDate fechaAdquisicion;
    private String observaciones;
    private String estadoFisico;
    private Boolean disponibleParaPrestamo; // Campo adicional calculado
}