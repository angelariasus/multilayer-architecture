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
public class PrestamoDTO {
    private Long idPrestamo;
    private Long idUsuario;
    private String nombreUsuario; // Campo adicional
    private String tipoUsuario; // Campo adicional
    private Long idLibro;
    private String tituloLibro; // Campo adicional
    private String autorLibro; // Campo adicional
    private Long idEjemplar;
    private String codigoEjemplar; // Campo adicional
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEsperada;
    private LocalDate fechaDevolucionReal;
    private String estado;
    private String observaciones;
    private Integer diasRetraso; // Campo adicional calculado
    private Boolean tieneMulta; // Campo adicional calculado
    private Boolean estaVencido; // Campo adicional calculado
}