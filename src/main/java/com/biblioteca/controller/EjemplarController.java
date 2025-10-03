package com.biblioteca.controller;

import com.biblioteca.dto.EjemplarDTO;
import com.biblioteca.service.EjemplarService;

import java.util.List;
import java.util.Map;

public class EjemplarController extends BaseController {
    
    private final EjemplarService ejemplarService;
    
    public EjemplarController() {
        this.ejemplarService = new EjemplarService();
    }
    
    public Map<String, Object> crearEjemplar(EjemplarDTO ejemplarDTO) {
        try {
            EjemplarDTO nuevoEjemplar = ejemplarService.save(ejemplarDTO);
            return createSuccessResponse("Ejemplar creado exitosamente", nuevoEjemplar);
        } catch (Exception e) {
            return createErrorResponse("Error al crear ejemplar", e);
        }
    }
    
    public Map<String, Object> obtenerEjemplar(Long id) {
        try {
            EjemplarDTO ejemplar = ejemplarService.findById(id);
            if (ejemplar != null) {
                return createSuccessResponse("Ejemplar encontrado", ejemplar);
            } else {
                return createErrorResponse("Ejemplar no encontrado");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al buscar ejemplar", e);
        }
    }
    
    public Map<String, Object> buscarEjemplarPorCodigo(String codigo) {
        try {
            EjemplarDTO ejemplar = ejemplarService.findByCodigo(codigo);
            if (ejemplar != null) {
                return createSuccessResponse("Ejemplar encontrado", ejemplar);
            } else {
                return createErrorResponse("Ejemplar no encontrado");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al buscar ejemplar por código", e);
        }
    }
    
    public Map<String, Object> listarEjemplares() {
        try {
            List<EjemplarDTO> ejemplares = ejemplarService.findAll();
            return createSuccessResponse("Ejemplares obtenidos exitosamente", ejemplares);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener ejemplares", e);
        }
    }
    
    public Map<String, Object> listarEjemplaresPorLibro(Long idLibro) {
        try {
            List<EjemplarDTO> ejemplares = ejemplarService.findByLibro(idLibro);
            return createSuccessResponse("Ejemplares por libro obtenidos exitosamente", ejemplares);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener ejemplares por libro", e);
        }
    }
    
    public Map<String, Object> listarEjemplaresDisponibles(Long idLibro) {
        try {
            List<EjemplarDTO> ejemplares = ejemplarService.findDisponiblesByLibro(idLibro);
            return createSuccessResponse("Ejemplares disponibles obtenidos exitosamente", ejemplares);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener ejemplares disponibles", e);
        }
    }
    
    public Map<String, Object> actualizarEjemplar(EjemplarDTO ejemplarDTO) {
        try {
            EjemplarDTO ejemplarActualizado = ejemplarService.update(ejemplarDTO);
            return createSuccessResponse("Ejemplar actualizado exitosamente", ejemplarActualizado);
        } catch (Exception e) {
            return createErrorResponse("Error al actualizar ejemplar", e);
        }
    }
    
    public Map<String, Object> cambiarEstadoEjemplar(Long idEjemplar, String nuevoEstado, String observaciones) {
        try {
            EjemplarDTO ejemplarActualizado = ejemplarService.cambiarEstado(idEjemplar, nuevoEstado, observaciones);
            return createSuccessResponse("Estado del ejemplar cambiado exitosamente", ejemplarActualizado);
        } catch (Exception e) {
            return createErrorResponse("Error al cambiar estado del ejemplar", e);
        }
    }
    
    public Map<String, Object> eliminarEjemplar(Long id) {
        try {
            boolean eliminado = ejemplarService.delete(id);
            if (eliminado) {
                return createSuccessResponse("Ejemplar eliminado exitosamente", null);
            } else {
                return createErrorResponse("No se pudo eliminar el ejemplar");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al eliminar ejemplar", e);
        }
    }
}