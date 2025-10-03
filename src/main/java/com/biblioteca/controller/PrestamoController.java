package com.biblioteca.controller;

import com.biblioteca.dto.PrestamoDTO;
import com.biblioteca.service.PrestamoService;

import java.util.List;
import java.util.Map;

public class PrestamoController extends BaseController {
    
    private final PrestamoService prestamoService;
    
    public PrestamoController() {
        this.prestamoService = new PrestamoService();
    }
    
    public Map<String, Object> crearPrestamo(PrestamoDTO prestamoDTO) {
        try {
            PrestamoDTO nuevoPrestamo = prestamoService.save(prestamoDTO);
            return createSuccessResponse("Préstamo creado exitosamente", nuevoPrestamo);
        } catch (Exception e) {
            return createErrorResponse("Error al crear préstamo", e);
        }
    }
    
    public Map<String, Object> obtenerPrestamo(Long id) {
        try {
            PrestamoDTO prestamo = prestamoService.findById(id);
            if (prestamo != null) {
                return createSuccessResponse("Préstamo encontrado", prestamo);
            } else {
                return createErrorResponse("Préstamo no encontrado");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al buscar préstamo", e);
        }
    }
    
    public Map<String, Object> listarPrestamos() {
        try {
            List<PrestamoDTO> prestamos = prestamoService.findAll();
            return createSuccessResponse("Préstamos obtenidos exitosamente", prestamos);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener préstamos", e);
        }
    }
    
    public Map<String, Object> listarPrestamosPorUsuario(Long idUsuario) {
        try {
            List<PrestamoDTO> prestamos = prestamoService.findByUsuario(idUsuario);
            return createSuccessResponse("Préstamos por usuario obtenidos exitosamente", prestamos);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener préstamos por usuario", e);
        }
    }
    
    public Map<String, Object> listarPrestamosVencidos() {
        try {
            List<PrestamoDTO> prestamos = prestamoService.findVencidos();
            return createSuccessResponse("Préstamos vencidos obtenidos exitosamente", prestamos);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener préstamos vencidos", e);
        }
    }
    
    public Map<String, Object> actualizarPrestamo(PrestamoDTO prestamoDTO) {
        try {
            PrestamoDTO prestamoActualizado = prestamoService.update(prestamoDTO);
            return createSuccessResponse("Préstamo actualizado exitosamente", prestamoActualizado);
        } catch (Exception e) {
            return createErrorResponse("Error al actualizar préstamo", e);
        }
    }
    
    public Map<String, Object> devolverLibro(Long idPrestamo, String observaciones) {
        try {
            PrestamoDTO prestamoDevuelto = prestamoService.devolver(idPrestamo, observaciones);
            return createSuccessResponse("Libro devuelto exitosamente", prestamoDevuelto);
        } catch (Exception e) {
            return createErrorResponse("Error al devolver libro", e);
        }
    }
    
    public Map<String, Object> eliminarPrestamo(Long id) {
        try {
            boolean eliminado = prestamoService.delete(id);
            if (eliminado) {
                return createSuccessResponse("Préstamo eliminado exitosamente", null);
            } else {
                return createErrorResponse("No se pudo eliminar el préstamo");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al eliminar préstamo", e);
        }
    }
}