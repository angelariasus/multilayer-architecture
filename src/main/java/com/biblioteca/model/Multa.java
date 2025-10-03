package com.biblioteca.model;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Multa {
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