package com.biblioteca.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Multa {
    private Long idMulta;
    private Long idPrestamo;
    private BigDecimal monto;
    private LocalDate fechaGeneracion;
    private LocalDate fechaPago;
    private String estado; // 'Pendiente', 'Pagada', 'Cancelada'
    private String motivo;
    private Integer diasRetraso;
}