package com.biblioteca.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrestamoDTO {
    private int idPrestamo;
    private int idUsuario;
    private int idLibro;
    private String fechaPrestamo;
    private String fechaDevolucionEsperada;
    private String fechaDevolucionReal;
    private String estado; // "Activo", "Devuelto", "Vencido"
    private String observaciones;
    
    // Campos adicionales para mostrar información relacionada
    private String nombreUsuario;
    private String tituloLibro;
    private String autorLibro;
    private boolean esVencido;
}