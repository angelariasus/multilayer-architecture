package com.biblioteca.service;

import com.biblioteca.dao.*;
import com.biblioteca.dto.ReporteDTO;
import com.biblioteca.dto.LibroDTO;
import com.biblioteca.dto.CategoriaDTO;
import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ReporteService {
    
    private final UsuarioDAO usuarioDAO;
    private final LibroDAO libroDAO;
    private final CategoriaDAO categoriaDAO;
    private final PrestamoDAO prestamoDAO;
    private final ReservaDAO reservaDAO;
    private final MultaDAO multaDAO;
    private final EjemplarDAO ejemplarDAO;
    
    public ReporteService() {
        this.usuarioDAO = new UsuarioDAO();
        this.libroDAO = new LibroDAO();
        this.categoriaDAO = new CategoriaDAO();
        this.prestamoDAO = new PrestamoDAO();
        this.reservaDAO = new ReservaDAO();
        this.multaDAO = new MultaDAO();
        this.ejemplarDAO = new EjemplarDAO();
    }
    
    public ReporteDTO generarReporteGeneral() {
        try {
            LocalDate fechaInicio = LocalDate.now().minusMonths(1);
            LocalDate fechaFin = LocalDate.now();
            
            return ReporteDTO.builder()
                    .tipoReporte("GENERAL")
                    .fechaGeneracion(LocalDateTime.now())
                    .nombreReporte("Reporte General del Sistema")
                    .fechaInicio(fechaInicio)
                    .fechaFin(fechaFin)
                    
                    // Estadísticas generales
                    .totalLibros(libroDAO.findAll().size())
                    .totalEjemplares(ejemplarDAO.findAll().size())
                    .totalUsuarios(usuarioDAO.findAll().size())
                    .totalCategorias(categoriaDAO.findAll().size())
                    
                    // Estadísticas de préstamos
                    .prestamosActivos(getPrestamosActivos())
                    .prestamosVencidos(prestamoDAO.findVencidos().size())
                    .prestamosDelMes(getPrestamosDelPeriodo(fechaInicio, fechaFin))
                    .prestamosDelAno(getPrestamosDelAno())
                    
                    // Estadísticas de reservas
                    .reservasActivas(getReservasActivas())
                    .reservasVencidas(reservaDAO.findVencidas().size())
                    .reservasCumplidas(getReservasCumplidas())
                    
                    // Estadísticas de multas
                    .multasPendientes(multaDAO.findPendientes().size())
                    .multasPagadas(getMultasPagadas())
                    .montoTotalMultas(getMontoTotalMultas())
                    .montoMultasPendientes(getMontoMultasPendientes())
                    
                    // Estadísticas de usuarios
                    .usuariosActivos(getUsuariosActivos())
                    .usuariosBloqueados(getUsuariosBloqueados())
                    .estudiantesActivos(getUsuariosPorTipo("Estudiante"))
                    .docentesActivos(getUsuariosPorTipo("Docente"))
                    
                    // Estadísticas de libros
                    .librosDisponibles(getLibrosDisponibles())
                    .librosAgotados(getLibrosAgotados())
                    .categoriaPopular(getCategoriaPopular())
                    
                    // Datos adicionales para gráficos
                    .prestamosPorMes(getPrestamosPorMes())
                    .usuariosPorTipo(getUsuariosPorTipoMap())
                    .promedioPrestamosPorDia(getPromedioPrestamosPorDia())
                    .tasaDevolucionPuntual(getTasaDevolucionPuntual())
                    
                    .generadoPor("Sistema")
                    .observaciones("Reporte generado automáticamente")
                    .build();
                    
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte general: " + e.getMessage(), e);
        }
    }
    
    public ReporteDTO generarReportePrestamos(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            List<Prestamo> prestamosPeriodo = prestamoDAO.findAll().stream()
                    .filter(p -> !p.getFechaPrestamo().isBefore(fechaInicio) && 
                               !p.getFechaPrestamo().isAfter(fechaFin))
                    .collect(Collectors.toList());
            
            return ReporteDTO.builder()
                    .tipoReporte("PRESTAMOS")
                    .fechaGeneracion(LocalDateTime.now())
                    .nombreReporte("Reporte de Préstamos")
                    .fechaInicio(fechaInicio)
                    .fechaFin(fechaFin)
                    .prestamosDelMes(prestamosPeriodo.size())
                    .prestamosActivos((int) prestamosPeriodo.stream().filter(p -> "Activo".equals(p.getEstado())).count())
                    .prestamosVencidos((int) prestamosPeriodo.stream()
                            .filter(p -> "Activo".equals(p.getEstado()) && 
                                       LocalDate.now().isAfter(p.getFechaDevolucionEsperada())).count())
                    .generadoPor("Sistema")
                    .build();
                    
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte de préstamos: " + e.getMessage(), e);
        }
    }
    
    public ReporteDTO generarReporteUsuarios() {
        try {
            List<Usuario> usuarios = usuarioDAO.findAll();
            
            return ReporteDTO.builder()
                    .tipoReporte("USUARIOS")
                    .fechaGeneracion(LocalDateTime.now())
                    .nombreReporte("Reporte de Usuarios")
                    .totalUsuarios(usuarios.size())
                    .usuariosActivos((int) usuarios.stream().filter(u -> "Activo".equals(u.getEstado())).count())
                    .usuariosBloqueados((int) usuarios.stream().filter(u -> "Bloqueado".equals(u.getEstado())).count())
                    .estudiantesActivos((int) usuarios.stream()
                            .filter(u -> "Estudiante".equals(u.getTipo()) && "Activo".equals(u.getEstado())).count())
                    .docentesActivos((int) usuarios.stream()
                            .filter(u -> "Docente".equals(u.getTipo()) && "Activo".equals(u.getEstado())).count())
                    .usuariosPorTipo(getUsuariosPorTipoMap())
                    .generadoPor("Sistema")
                    .build();
                    
        } catch (Exception e) {
            throw new RuntimeException("Error al generar reporte de usuarios: " + e.getMessage(), e);
        }
    }
    
    // Métodos auxiliares para cálculos
    private int getPrestamosActivos() {
        return (int) prestamoDAO.findAll().stream()
                .filter(p -> "Activo".equals(p.getEstado()))
                .count();
    }
    
    private int getPrestamosDelPeriodo(LocalDate inicio, LocalDate fin) {
        return (int) prestamoDAO.findAll().stream()
                .filter(p -> !p.getFechaPrestamo().isBefore(inicio) && 
                           !p.getFechaPrestamo().isAfter(fin))
                .count();
    }
    
    private int getPrestamosDelAno() {
        LocalDate inicioAno = LocalDate.now().withDayOfYear(1);
        return getPrestamosDelPeriodo(inicioAno, LocalDate.now());
    }
    
    private int getReservasActivas() {
        return (int) reservaDAO.findAll().stream()
                .filter(r -> "Activa".equals(r.getEstado()))
                .count();
    }
    
    private int getReservasCumplidas() {
        return (int) reservaDAO.findAll().stream()
                .filter(r -> "Cumplida".equals(r.getEstado()))
                .count();
    }
    
    private int getMultasPagadas() {
        return (int) multaDAO.findAll().stream()
                .filter(m -> "Pagada".equals(m.getEstado()))
                .count();
    }
    
    private BigDecimal getMontoTotalMultas() {
        return multaDAO.findAll().stream()
                .map(Multa::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private BigDecimal getMontoMultasPendientes() {
        return multaDAO.findPendientes().stream()
                .map(Multa::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private int getUsuariosActivos() {
        return (int) usuarioDAO.findAll().stream()
                .filter(u -> "Activo".equals(u.getEstado()))
                .count();
    }
    
    private int getUsuariosBloqueados() {
        return (int) usuarioDAO.findAll().stream()
                .filter(u -> "Bloqueado".equals(u.getEstado()))
                .count();
    }
    
    private int getUsuariosPorTipo(String tipo) {
        return (int) usuarioDAO.findAll().stream()
                .filter(u -> tipo.equals(u.getTipo()) && "Activo".equals(u.getEstado()))
                .count();
    }
    
    private int getLibrosDisponibles() {
        return (int) libroDAO.findAll().stream()
                .filter(l -> "Disponible".equals(l.getEstado()) && l.getCantidadDisponible() > 0)
                .count();
    }
    
    private int getLibrosAgotados() {
        return (int) libroDAO.findAll().stream()
                .filter(l -> "Agotado".equals(l.getEstado()) || l.getCantidadDisponible() == 0)
                .count();
    }
    
    private String getCategoriaPopular() {
        // Simplificado - retorna la primera categoría
        List<Categoria> categorias = categoriaDAO.findAll();
        return !categorias.isEmpty() ? categorias.get(0).getNombre() : "Sin datos";
    }
    
    private Map<String, Integer> getPrestamosPorMes() {
        Map<String, Integer> prestamosPorMes = new LinkedHashMap<>();
        LocalDate fecha = LocalDate.now().minusMonths(11);
        
        for (int i = 0; i < 12; i++) {
            String mes = fecha.getMonth().name();
            int prestamos = getPrestamosDelPeriodo(
                fecha.withDayOfMonth(1), 
                fecha.withDayOfMonth(fecha.lengthOfMonth())
            );
            prestamosPorMes.put(mes, prestamos);
            fecha = fecha.plusMonths(1);
        }
        
        return prestamosPorMes;
    }
    
    private Map<String, Integer> getUsuariosPorTipoMap() {
        Map<String, Integer> usuariosPorTipo = new HashMap<>();
        usuariosPorTipo.put("Estudiante", getUsuariosPorTipo("Estudiante"));
        usuariosPorTipo.put("Docente", getUsuariosPorTipo("Docente"));
        usuariosPorTipo.put("Administrativo", getUsuariosPorTipo("Administrativo"));
        usuariosPorTipo.put("Bibliotecario", getUsuariosPorTipo("Bibliotecario"));
        return usuariosPorTipo;
    }
    
    private BigDecimal getPromedioPrestamosPorDia() {
        int totalPrestamos = prestamoDAO.findAll().size();
        int diasDelAno = LocalDate.now().getDayOfYear();
        return diasDelAno > 0 ? 
            BigDecimal.valueOf(totalPrestamos).divide(BigDecimal.valueOf(diasDelAno), 2, RoundingMode.HALF_UP) : 
            BigDecimal.ZERO;
    }
    
    private Double getTasaDevolucionPuntual() {
        List<Prestamo> prestamosTerminados = prestamoDAO.findAll().stream()
                .filter(p -> "Devuelto".equals(p.getEstado()) && p.getFechaDevolucionReal() != null)
                .collect(Collectors.toList());
        
        if (prestamosTerminados.isEmpty()) return 0.0;
        
        long devolucionesPuntuales = prestamosTerminados.stream()
                .filter(p -> !p.getFechaDevolucionReal().isAfter(p.getFechaDevolucionEsperada()))
                .count();
        
        return (devolucionesPuntuales * 100.0) / prestamosTerminados.size();
    }
}