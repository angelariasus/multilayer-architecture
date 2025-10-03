package com.biblioteca.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prestamo {
    private int idPrestamo;
    private int idUsuario;
    private int idLibro;
    private String fechaPrestamo;
    private String fechaDevolucionEsperada;
    private String fechaDevolucionReal;
    private String estado; 
    private String observaciones;
    
    // Campos adicionales
    private String nombreUsuario;
    private String tituloLibro;
    private String autorLibro;
    private boolean esVencido;
}