package com.biblioteca.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {
    private int idReserva;
    private int idUsuario;
    private int idLibro;
    private String fechaReserva;
    private String fechaVencimiento;
    private String fechaRecogida;
    private String estado;
    private int posicionCola;
    private String observaciones;
    
    // Campos adicionales
    private String nombreUsuario;
    private String emailUsuario;
    private String tituloLibro;
    private String autorLibro;
    private String isbnLibro;
    private boolean esUrgente;
    private int diasRestantes;
}