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
public class Prestamo {
    private Long idPrestamo;
    private Long idUsuario;
    private Long idLibro;
    private Long idEjemplar;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEsperada;
    private LocalDate fechaDevolucionReal;
    private String estado; // 'Activo', 'Devuelto', 'Vencido'
    private String observaciones;
}