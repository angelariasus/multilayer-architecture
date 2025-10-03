package com.biblioteca.service;

import com.biblioteca.dao.CategoriaDAO;
import com.biblioteca.dao.LibroDAO;
import com.biblioteca.dto.CategoriaDTO;
import com.biblioteca.model.Categoria;
import com.biblioteca.exception.ServiceException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriaService implements BaseService<Categoria, CategoriaDTO, Long> {
    
    private final CategoriaDAO categoriaDAO;
    private final LibroDAO libroDAO;
    
    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAO();
        this.libroDAO = new LibroDAO();
    }
    
    @Override
    public CategoriaDTO save(CategoriaDTO dto) {
        try {
            // Validaciones
            if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
                throw new ServiceException("El nombre de la categoría es obligatorio");
            }
            
            // Verificar que no exista otra categoría con el mismo nombre
            Categoria existente = categoriaDAO.findByNombre(dto.getNombre());
            if (existente != null) {
                throw new ServiceException("Ya existe una categoría con ese nombre");
            }
            
            Categoria categoria = Categoria.builder()
                    .nombre(dto.getNombre().trim())
                    .descripcion(dto.getDescripcion())
                    .estado(dto.getEstado() != null ? dto.getEstado() : "Activa")
                    .fechaCreacion(LocalDate.now())
                    .build();
            
            Categoria saved = categoriaDAO.save(categoria);
            return convertToDTO(saved);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar categoría: " + e.getMessage(), e);
        }
    }
    
    @Override
    public CategoriaDTO findById(Long id) {
        try {
            Categoria categoria = categoriaDAO.findById(id);
            return categoria != null ? convertToDTO(categoria) : null;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar categoría: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<CategoriaDTO> findAll() {
        try {
            return categoriaDAO.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener categorías: " + e.getMessage(), e);
        }
    }
    
    @Override
    public CategoriaDTO update(CategoriaDTO dto) {
        try {
            // Validaciones
            if (dto.getIdCategoria() == null) {
                throw new ServiceException("ID de categoría es requerido para actualizar");
            }
            
            Categoria existente = categoriaDAO.findById(dto.getIdCategoria());
            if (existente == null) {
                throw new ServiceException("Categoría no encontrada");
            }
            
            // Verificar nombre único (excluyendo la categoría actual)
            Categoria conMismoNombre = categoriaDAO.findByNombre(dto.getNombre());
            if (conMismoNombre != null && !conMismoNombre.getIdCategoria().equals(dto.getIdCategoria())) {
                throw new ServiceException("Ya existe otra categoría con ese nombre");
            }
            
            Categoria categoria = Categoria.builder()
                    .idCategoria(dto.getIdCategoria())
                    .nombre(dto.getNombre().trim())
                    .descripcion(dto.getDescripcion())
                    .estado(dto.getEstado())
                    .fechaCreacion(existente.getFechaCreacion())
                    .build();
            
            Categoria updated = categoriaDAO.update(categoria);
            return convertToDTO(updated);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar categoría: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(Long id) {
        try {
            // Verificar que no tenga libros asociados
            List<com.biblioteca.model.Libro> libros = libroDAO.findByCategoria(id);
            if (!libros.isEmpty()) {
                throw new ServiceException("No se puede eliminar la categoría porque tiene libros asociados");
            }
            
            return categoriaDAO.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar categoría: " + e.getMessage(), e);
        }
    }
    
    public List<CategoriaDTO> findActivas() {
        try {
            return categoriaDAO.findAll().stream()
                    .filter(c -> "Activa".equals(c.getEstado()))
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener categorías activas: " + e.getMessage(), e);
        }
    }
    
    private CategoriaDTO convertToDTO(Categoria categoria) {
        // Obtener cantidad de libros en esta categoría
        int totalLibros = libroDAO.findByCategoria(categoria.getIdCategoria()).size();
        
        return CategoriaDTO.builder()
                .idCategoria(categoria.getIdCategoria())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .estado(categoria.getEstado())
                .fechaCreacion(categoria.getFechaCreacion())
                .totalLibros(totalLibros)
                .build();
    }
}