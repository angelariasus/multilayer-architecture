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
public class Reserva {
    private Long idReserva;
    private Long idUsuario;
    private Long idLibro;
    private LocalDate fechaReserva;
    private LocalDate fechaVencimiento;
    private LocalDate fechaRecogida;
    private String estado; // 'Activa', 'Cumplida', 'Vencida', 'Cancelada'
    private Integer posicionCola;
    private String observaciones;
}