package com.biblioteca.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteDTO {
    private String tipoReporte;
    private String fechaGeneracion;
    
    private int totalLibros;
    private int librosDisponibles;
    private int librosPrestados;
    private int totalUsuarios;
    private int usuariosActivos;
    private int prestamosActivos;
    private int prestamosVencidos;
    private int multasPendientes;
    
    private String periodo;
    private String detalle;
}