package com.biblioteca.controller;

import com.biblioteca.dto.LibroDTO;
import com.biblioteca.service.LibroService;

import java.util.List;
import java.util.Map;

public class LibroController extends BaseController {
    
    private final LibroService libroService;
    
    public LibroController() {
        this.libroService = new LibroService();
    }
    
    public Map<String, Object> crearLibro(LibroDTO libroDTO) {
        try {
            LibroDTO nuevoLibro = libroService.save(libroDTO);
            return createSuccessResponse("Libro creado exitosamente", nuevoLibro);
        } catch (Exception e) {
            return createErrorResponse("Error al crear libro", e);
        }
    }
    
    public Map<String, Object> obtenerLibro(Long id) {
        try {
            LibroDTO libro = libroService.findById(id);
            if (libro != null) {
                return createSuccessResponse("Libro encontrado", libro);
            } else {
                return createErrorResponse("Libro no encontrado");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al buscar libro", e);
        }
    }
    
    public Map<String, Object> listarLibros() {
        try {
            List<LibroDTO> libros = libroService.findAll();
            return createSuccessResponse("Libros obtenidos exitosamente", libros);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener libros", e);
        }
    }
    
    public Map<String, Object> buscarLibros(String searchTerm) {
        try {
            List<LibroDTO> libros = libroService.searchByTitleOrAuthor(searchTerm);
            return createSuccessResponse("Búsqueda realizada exitosamente", libros);
        } catch (Exception e) {
            return createErrorResponse("Error en la búsqueda", e);
        }
    }
    
    public Map<String, Object> listarLibrosPorCategoria(Long idCategoria) {
        try {
            List<LibroDTO> libros = libroService.findByCategoria(idCategoria);
            return createSuccessResponse("Libros por categoría obtenidos exitosamente", libros);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener libros por categoría", e);
        }
    }
    
    public Map<String, Object> listarLibrosDisponibles() {
        try {
            List<LibroDTO> libros = libroService.findDisponibles();
            return createSuccessResponse("Libros disponibles obtenidos exitosamente", libros);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener libros disponibles", e);
        }
    }
    
    public Map<String, Object> actualizarLibro(LibroDTO libroDTO) {
        try {
            LibroDTO libroActualizado = libroService.update(libroDTO);
            return createSuccessResponse("Libro actualizado exitosamente", libroActualizado);
        } catch (Exception e) {
            return createErrorResponse("Error al actualizar libro", e);
        }
    }
    
    public Map<String, Object> eliminarLibro(Long id) {
        try {
            boolean eliminado = libroService.delete(id);
            if (eliminado) {
                return createSuccessResponse("Libro eliminado exitosamente", null);
            } else {
                return createErrorResponse("No se pudo eliminar el libro");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al eliminar libro", e);
        }
    }
}