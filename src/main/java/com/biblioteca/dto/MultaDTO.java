package com.biblioteca.dto;

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
public class MultaDTO {
    private Long idMulta;
    private Long idPrestamo;
    private String nombreUsuario; // Campo adicional
    private String tituloLibro; // Campo adicional
    private BigDecimal monto;
    private LocalDate fechaGeneracion;
    private LocalDate fechaPago;
    private String estado;
    private String motivo;
    private Integer diasRetraso;
    private Integer diasVencimiento; // Campo adicional calculado
    private BigDecimal montoConInteres; // Campo adicional calculado
}