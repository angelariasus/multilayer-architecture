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
public class ReservaDTO {
    private Long idReserva;
    private Long idUsuario;
    private String nombreUsuario; // Campo adicional
    private Long idLibro;
    private String tituloLibro; // Campo adicional
    private String autorLibro; // Campo adicional
    private LocalDate fechaReserva;
    private LocalDate fechaVencimiento;
    private LocalDate fechaRecogida;
    private String estado;
    private Integer posicionCola;
    private String observaciones;
    private Integer diasRestantes; // Campo adicional calculado
    private Boolean puedeRecoger; // Campo adicional calculado
}