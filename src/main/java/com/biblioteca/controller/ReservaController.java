package com.biblioteca.controller;

import com.biblioteca.dto.ReservaDTO;
import com.biblioteca.service.ReservaService;

import java.util.List;
import java.util.Map;

public class ReservaController extends BaseController {
    
    private final ReservaService reservaService;
    
    public ReservaController() {
        this.reservaService = new ReservaService();
    }
    
    public Map<String, Object> crearReserva(ReservaDTO reservaDTO) {
        try {
            ReservaDTO nuevaReserva = reservaService.save(reservaDTO);
            return createSuccessResponse("Reserva creada exitosamente", nuevaReserva);
        } catch (Exception e) {
            return createErrorResponse("Error al crear reserva", e);
        }
    }
    
    public Map<String, Object> obtenerReserva(Long id) {
        try {
            ReservaDTO reserva = reservaService.findById(id);
            if (reserva != null) {
                return createSuccessResponse("Reserva encontrada", reserva);
            } else {
                return createErrorResponse("Reserva no encontrada");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al buscar reserva", e);
        }
    }
    
    public Map<String, Object> listarReservas() {
        try {
            List<ReservaDTO> reservas = reservaService.findAll();
            return createSuccessResponse("Reservas obtenidas exitosamente", reservas);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener reservas", e);
        }
    }
    
    public Map<String, Object> listarReservasPorUsuario(Long idUsuario) {
        try {
            List<ReservaDTO> reservas = reservaService.findByUsuario(idUsuario);
            return createSuccessResponse("Reservas por usuario obtenidas exitosamente", reservas);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener reservas por usuario", e);
        }
    }
    
    public Map<String, Object> listarReservasActivasPorLibro(Long idLibro) {
        try {
            List<ReservaDTO> reservas = reservaService.findActivasByLibro(idLibro);
            return createSuccessResponse("Reservas activas por libro obtenidas exitosamente", reservas);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener reservas activas por libro", e);
        }
    }
    
    public Map<String, Object> listarReservasVencidas() {
        try {
            List<ReservaDTO> reservas = reservaService.findVencidas();
            return createSuccessResponse("Reservas vencidas obtenidas exitosamente", reservas);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener reservas vencidas", e);
        }
    }
    
    public Map<String, Object> actualizarReserva(ReservaDTO reservaDTO) {
        try {
            ReservaDTO reservaActualizada = reservaService.update(reservaDTO);
            return createSuccessResponse("Reserva actualizada exitosamente", reservaActualizada);
        } catch (Exception e) {
            return createErrorResponse("Error al actualizar reserva", e);
        }
    }
    
    public Map<String, Object> cumplirReserva(Long idReserva) {
        try {
            ReservaDTO reservaCumplida = reservaService.cumplirReserva(idReserva);
            return createSuccessResponse("Reserva cumplida exitosamente", reservaCumplida);
        } catch (Exception e) {
            return createErrorResponse("Error al cumplir reserva", e);
        }
    }
    
    public Map<String, Object> cancelarReserva(Long idReserva, String motivo) {
        try {
            ReservaDTO reservaCancelada = reservaService.cancelarReserva(idReserva, motivo);
            return createSuccessResponse("Reserva cancelada exitosamente", reservaCancelada);
        } catch (Exception e) {
            return createErrorResponse("Error al cancelar reserva", e);
        }
    }
    
    public Map<String, Object> procesarReservasVencidas() {
        try {
            reservaService.procesarReservasVencidas();
            return createSuccessResponse("Reservas vencidas procesadas exitosamente", null);
        } catch (Exception e) {
            return createErrorResponse("Error al procesar reservas vencidas", e);
        }
    }
    
    public Map<String, Object> eliminarReserva(Long id) {
        try {
            boolean eliminado = reservaService.delete(id);
            if (eliminado) {
                return createSuccessResponse("Reserva eliminada exitosamente", null);
            } else {
                return createErrorResponse("No se pudo eliminar la reserva");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al eliminar reserva", e);
        }
    }
}