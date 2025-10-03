package com.biblioteca.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteDTO {
    private Long idReporte;
    private String tipoReporte;
    private LocalDateTime fechaGeneracion;
    private String nombreReporte;
    
    // Estadísticas generales
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
    
    // Campos adicionales para el DTO
    private List<LibroDTO> topLibrosPrestados; // Top 10 libros más prestados
    private List<CategoriaDTO> categoriasConEstadisticas; // Categorías con sus estadísticas
    private Map<String, Integer> prestamosPorMes; // Gráfico de préstamos por mes
    private Map<String, Integer> usuariosPorTipo; // Distribución de usuarios por tipo
    private List<UsuarioDTO> usuariosConMasMultas; // Usuarios con más multas
    private BigDecimal promedioPrestamosPorDia; // Promedio de préstamos por día
    private Double tasaDevolucionPuntual; // Porcentaje de devoluciones a tiempo
}