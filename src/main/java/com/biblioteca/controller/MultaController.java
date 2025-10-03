package com.biblioteca.controller;

import com.biblioteca.dto.MultaDTO;
import com.biblioteca.service.MultaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class MultaController extends BaseController {
    
    private final MultaService multaService;
    
    public MultaController() {
        this.multaService = new MultaService();
    }
    
    public Map<String, Object> crearMulta(MultaDTO multaDTO) {
        try {
            MultaDTO nuevaMulta = multaService.save(multaDTO);
            return createSuccessResponse("Multa creada exitosamente", nuevaMulta);
        } catch (Exception e) {
            return createErrorResponse("Error al crear multa", e);
        }
    }
    
    public Map<String, Object> obtenerMulta(Long id) {
        try {
            MultaDTO multa = multaService.findById(id);
            if (multa != null) {
                return createSuccessResponse("Multa encontrada", multa);
            } else {
                return createErrorResponse("Multa no encontrada");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al buscar multa", e);
        }
    }
    
    public Map<String, Object> listarMultas() {
        try {
            List<MultaDTO> multas = multaService.findAll();
            return createSuccessResponse("Multas obtenidas exitosamente", multas);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener multas", e);
        }
    }
    
    public Map<String, Object> listarMultasPorUsuario(Long idUsuario) {
        try {
            List<MultaDTO> multas = multaService.findByUsuario(idUsuario);
            return createSuccessResponse("Multas por usuario obtenidas exitosamente", multas);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener multas por usuario", e);
        }
    }
    
    public Map<String, Object> listarMultasPendientes() {
        try {
            List<MultaDTO> multas = multaService.findPendientes();
            return createSuccessResponse("Multas pendientes obtenidas exitosamente", multas);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener multas pendientes", e);
        }
    }
    
    public Map<String, Object> listarMultasPorPrestamo(Long idPrestamo) {
        try {
            List<MultaDTO> multas = multaService.findByPrestamo(idPrestamo);
            return createSuccessResponse("Multas por préstamo obtenidas exitosamente", multas);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener multas por préstamo", e);
        }
    }
    
    public Map<String, Object> actualizarMulta(MultaDTO multaDTO) {
        try {
            MultaDTO multaActualizada = multaService.update(multaDTO);
            return createSuccessResponse("Multa actualizada exitosamente", multaActualizada);
        } catch (Exception e) {
            return createErrorResponse("Error al actualizar multa", e);
        }
    }
    
    public Map<String, Object> pagarMulta(Long idMulta) {
        try {
            MultaDTO multaPagada = multaService.pagarMulta(idMulta);
            return createSuccessResponse("Multa pagada exitosamente", multaPagada);
        } catch (Exception e) {
            return createErrorResponse("Error al pagar multa", e);
        }
    }
    
    public Map<String, Object> cancelarMulta(Long idMulta, String motivo) {
        try {
            MultaDTO multaCancelada = multaService.cancelarMulta(idMulta, motivo);
            return createSuccessResponse("Multa cancelada exitosamente", multaCancelada);
        } catch (Exception e) {
            return createErrorResponse("Error al cancelar multa", e);
        }
    }
    
    public Map<String, Object> generarMultaPorRetraso(Long idPrestamo) {
        try {
            MultaDTO multaGenerada = multaService.generarMultaPorRetraso(idPrestamo);
            return createSuccessResponse("Multa por retraso generada exitosamente", multaGenerada);
        } catch (Exception e) {
            return createErrorResponse("Error al generar multa por retraso", e);
        }
    }
    
    public Map<String, Object> calcularMontoConInteres(Long idMulta) {
        try {
            BigDecimal montoConInteres = multaService.calcularMontoConInteres(idMulta);
            return createSuccessResponse("Monto con interés calculado exitosamente", montoConInteres);
        } catch (Exception e) {
            return createErrorResponse("Error al calcular monto con interés", e);
        }
    }
    
    public Map<String, Object> eliminarMulta(Long id) {
        try {
            boolean eliminado = multaService.delete(id);
            if (eliminado) {
                return createSuccessResponse("Multa eliminada exitosamente", null);
            } else {
                return createErrorResponse("No se pudo eliminar la multa");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al eliminar multa", e);
        }
    }
}