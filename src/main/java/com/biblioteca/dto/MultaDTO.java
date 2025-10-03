package com.biblioteca.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultaDTO {
    private int idMulta;
    private int idPrestamo;
    private BigDecimal monto;
    private String fechaGeneracion;
    private String fechaPago;
    private String estado; 
    private String motivo;
    private int diasRetraso;
    
    // Campos adicionales
    private String nombreUsuario;
    private String tituloLibro;
}