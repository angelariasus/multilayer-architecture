package com.biblioteca.controller;

import com.biblioteca.dto.ReporteDTO;
import com.biblioteca.service.ReporteService;

import java.time.LocalDate;
import java.util.Map;

public class ReporteController extends BaseController {
    
    private final ReporteService reporteService;
    
    public ReporteController() {
        this.reporteService = new ReporteService();
    }
    
    public Map<String, Object> generarReporteGeneral() {
        try {
            ReporteDTO reporte = reporteService.generarReporteGeneral();
            return createSuccessResponse("Reporte general generado exitosamente", reporte);
        } catch (Exception e) {
            return createErrorResponse("Error al generar reporte general", e);
        }
    }
    
    public Map<String, Object> generarReportePrestamos(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            ReporteDTO reporte = reporteService.generarReportePrestamos(fechaInicio, fechaFin);
            return createSuccessResponse("Reporte de préstamos generado exitosamente", reporte);
        } catch (Exception e) {
            return createErrorResponse("Error al generar reporte de préstamos", e);
        }
    }
    
    public Map<String, Object> generarReporteUsuarios() {
        try {
            ReporteDTO reporte = reporteService.generarReporteUsuarios();
            return createSuccessResponse("Reporte de usuarios generado exitosamente", reporte);
        } catch (Exception e) {
            return createErrorResponse("Error al generar reporte de usuarios", e);
        }
    }
}