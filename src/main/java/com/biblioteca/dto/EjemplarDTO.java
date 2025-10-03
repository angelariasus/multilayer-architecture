package com.biblioteca.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EjemplarDTO {
    private int idEjemplar;
    private int idLibro;
    private String codigoEjemplar; 
    private String ubicacion;
    private String estado; 
    private String fechaAdquisicion;
    private String observaciones;
    private String estadoFisico; 
    
    // Campos adicionales
    private String tituloLibro;
    private String autorLibro;
    private String isbnLibro;
    private String categoriaLibro;
}