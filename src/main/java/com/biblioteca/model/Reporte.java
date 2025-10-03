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
public class Reporte {
    private Long idReporte;
    private String tipoReporte;
    private LocalDate fechaGeneracion;
    private String nombreReporte;
    
    // Estadísticas generales del sistema
    private Integer totalLibros;
    private Integer totalEjemplares;
    private Integer totalUsuarios;
    private Integer totalCategorias;
    
    // Estadísticas de préstamos
    private Integer prestamosActivos;
    private Integer prestamosVencidos;
    private Integer prestamosDelMes;
    private Integer prestamosDelAno;
    
    // Estadísticas de reservas
    private Integer reservasActivas;
    private Integer reservasVencidas;
    private Integer reservasCumplidas;
    
    // Estadísticas de multas
    private Integer multasPendientes;
    private Integer multasPagadas;
    private BigDecimal montoTotalMultas;
    private BigDecimal montoMultasPendientes;
    
    // Estadísticas de usuarios
    private Integer usuariosActivos;
    private Integer usuariosBloqueados;
    private Integer estudiantesActivos;
    private Integer docentesActivos;
    
    // Estadísticas de libros
    private Integer librosDisponibles;
    private Integer librosAgotados;
    private Integer librosMasPrestados;
    private String categoriaPopular;
    
    // Configuración del reporte
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String generadoPor;
    private String observaciones;
}