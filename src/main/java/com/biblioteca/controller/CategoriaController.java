package com.biblioteca.controller;

import com.biblioteca.dto.CategoriaDTO;
import com.biblioteca.service.CategoriaService;

import java.util.List;
import java.util.Map;

public class CategoriaController extends BaseController {
    
    private final CategoriaService categoriaService;
    
    public CategoriaController() {
        this.categoriaService = new CategoriaService();
    }
    
    public Map<String, Object> crearCategoria(CategoriaDTO categoriaDTO) {
        try {
            CategoriaDTO nuevaCategoria = categoriaService.save(categoriaDTO);
            return createSuccessResponse("Categoría creada exitosamente", nuevaCategoria);
        } catch (Exception e) {
            return createErrorResponse("Error al crear categoría", e);
        }
    }
    
    public Map<String, Object> obtenerCategoria(Long id) {
        try {
            CategoriaDTO categoria = categoriaService.findById(id);
            if (categoria != null) {
                return createSuccessResponse("Categoría encontrada", categoria);
            } else {
                return createErrorResponse("Categoría no encontrada");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al buscar categoría", e);
        }
    }
    
    public Map<String, Object> listarCategorias() {
        try {
            List<CategoriaDTO> categorias = categoriaService.findAll();
            return createSuccessResponse("Categorías obtenidas exitosamente", categorias);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener categorías", e);
        }
    }
    
    public Map<String, Object> listarCategoriasActivas() {
        try {
            List<CategoriaDTO> categorias = categoriaService.findActivas();
            return createSuccessResponse("Categorías activas obtenidas exitosamente", categorias);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener categorías activas", e);
        }
    }
    
    public Map<String, Object> actualizarCategoria(CategoriaDTO categoriaDTO) {
        try {
            CategoriaDTO categoriaActualizada = categoriaService.update(categoriaDTO);
            return createSuccessResponse("Categoría actualizada exitosamente", categoriaActualizada);
        } catch (Exception e) {
            return createErrorResponse("Error al actualizar categoría", e);
        }
    }
    
    public Map<String, Object> eliminarCategoria(Long id) {
        try {
            boolean eliminado = categoriaService.delete(id);
            if (eliminado) {
                return createSuccessResponse("Categoría eliminada exitosamente", null);
            } else {
                return createErrorResponse("No se pudo eliminar la categoría");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al eliminar categoría", e);
        }
    }
}