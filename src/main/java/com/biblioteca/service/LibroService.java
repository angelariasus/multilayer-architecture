package com.biblioteca.service;

import com.biblioteca.dao.LibroDAO;
import com.biblioteca.dao.CategoriaDAO;
import com.biblioteca.dao.EjemplarDAO;
import com.biblioteca.dto.LibroDTO;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Categoria;
import com.biblioteca.exception.ServiceException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class LibroService implements BaseService<Libro, LibroDTO, Long> {
    
    private final LibroDAO libroDAO;
    private final CategoriaDAO categoriaDAO;
    private final EjemplarDAO ejemplarDAO;
    
    public LibroService() {
        this.libroDAO = new LibroDAO();
        this.categoriaDAO = new CategoriaDAO();
        this.ejemplarDAO = new EjemplarDAO();
    }
    
    @Override
    public LibroDTO save(LibroDTO dto) {
        try {
            // Validaciones
            validateLibroDTO(dto);
            
            // Verificar ISBN único si se proporciona
            if (dto.getIsbn() != null && !dto.getIsbn().trim().isEmpty()) {
                Libro existente = libroDAO.findByIsbn(dto.getIsbn());
                if (existente != null) {
                    throw new ServiceException("Ya existe un libro con ese ISBN");
                }
            }
            
            // Verificar que la categoría existe
            Categoria categoria = categoriaDAO.findById(dto.getIdCategoria());
            if (categoria == null) {
                throw new ServiceException("La categoría especificada no existe");
            }
            
            Libro libro = Libro.builder()
                    .titulo(dto.getTitulo().trim())
                    .autor(dto.getAutor().trim())
                    .isbn(dto.getIsbn() != null ? dto.getIsbn().trim() : null)
                    .idCategoria(dto.getIdCategoria())
                    .cantidadDisponible(dto.getCantidadTotal() != null ? dto.getCantidadTotal() : 0)
                    .cantidadTotal(dto.getCantidadTotal() != null ? dto.getCantidadTotal() : 0)
                    .estado("Disponible")
                    .fechaPublicacion(dto.getFechaPublicacion())
                    .editorial(dto.getEditorial())
                    .descripcion(dto.getDescripcion())
                    .ubicacion(dto.getUbicacion())
                    .fechaRegistro(LocalDate.now())
                    .build();
            
            Libro saved = libroDAO.save(libro);
            return convertToDTO(saved);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar libro: " + e.getMessage(), e);
        }
    }
    
    @Override
    public LibroDTO findById(Long id) {
        try {
            Libro libro = libroDAO.findById(id);
            return libro != null ? convertToDTO(libro) : null;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar libro: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<LibroDTO> findAll() {
        try {
            return libroDAO.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener libros: " + e.getMessage(), e);
        }
    }
    
    @Override
    public LibroDTO update(LibroDTO dto) {
        try {
            if (dto.getIdLibro() == null) {
                throw new ServiceException("ID de libro es requerido para actualizar");
            }
            
            Libro existente = libroDAO.findById(dto.getIdLibro());
            if (existente == null) {
                throw new ServiceException("Libro no encontrado");
            }
            
            // Verificar ISBN único (excluyendo el libro actual)
            if (dto.getIsbn() != null && !dto.getIsbn().trim().isEmpty()) {
                Libro conMismoISBN = libroDAO.findByIsbn(dto.getIsbn());
                if (conMismoISBN != null && !conMismoISBN.getIdLibro().equals(dto.getIdLibro())) {
                    throw new ServiceException("Ya existe otro libro con ese ISBN");
                }
            }
            
            Libro libro = Libro.builder()
                    .idLibro(dto.getIdLibro())
                    .titulo(dto.getTitulo().trim())
                    .autor(dto.getAutor().trim())
                    .isbn(dto.getIsbn())
                    .idCategoria(dto.getIdCategoria())
                    .cantidadDisponible(dto.getCantidadDisponible())
                    .cantidadTotal(dto.getCantidadTotal())
                    .estado(dto.getEstado())
                    .fechaPublicacion(dto.getFechaPublicacion())
                    .editorial(dto.getEditorial())
                    .descripcion(dto.getDescripcion())
                    .ubicacion(dto.getUbicacion())
                    .fechaRegistro(existente.getFechaRegistro())
                    .build();
            
            Libro updated = libroDAO.update(libro);
            return convertToDTO(updated);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar libro: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(Long id) {
        try {
            // Verificar que no tenga ejemplares
            List<com.biblioteca.model.Ejemplar> ejemplares = ejemplarDAO.findByLibro(id);
            if (!ejemplares.isEmpty()) {
                throw new ServiceException("No se puede eliminar el libro porque tiene ejemplares asociados");
            }
            
            return libroDAO.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar libro: " + e.getMessage(), e);
        }
    }
    
    public List<LibroDTO> searchByTitleOrAuthor(String searchTerm) {
        try {
            return libroDAO.searchByTitleOrAuthor(searchTerm).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error en búsqueda: " + e.getMessage(), e);
        }
    }
    
    public List<LibroDTO> findByCategoria(Long idCategoria) {
        try {
            return libroDAO.findByCategoria(idCategoria).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar libros por categoría: " + e.getMessage(), e);
        }
    }
    
    public List<LibroDTO> findDisponibles() {
        try {
            return libroDAO.findAll().stream()
                    .filter(l -> "Disponible".equals(l.getEstado()) && l.getCantidadDisponible() > 0)
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar libros disponibles: " + e.getMessage(), e);
        }
    }
    
    private void validateLibroDTO(LibroDTO dto) throws ServiceException {
        if (dto.getTitulo() == null || dto.getTitulo().trim().isEmpty()) {
            throw new ServiceException("El título es obligatorio");
        }
        if (dto.getAutor() == null || dto.getAutor().trim().isEmpty()) {
            throw new ServiceException("El autor es obligatorio");
        }
        if (dto.getIdCategoria() == null) {
            throw new ServiceException("La categoría es obligatoria");
        }
    }
    
    private LibroDTO convertToDTO(Libro libro) {
        // Obtener nombre de categoría
        Categoria categoria = categoriaDAO.findById(libro.getIdCategoria());
        String nombreCategoria = categoria != null ? categoria.getNombre() : "Sin categoría";
        
        // Obtener ejemplares disponibles
        int ejemplaresDisponibles = ejemplarDAO.findDisponiblesByLibro(libro.getIdLibro()).size();
        
        return LibroDTO.builder()
                .idLibro(libro.getIdLibro())
                .titulo(libro.getTitulo())
                .autor(libro.getAutor())
                .isbn(libro.getIsbn())
                .idCategoria(libro.getIdCategoria())
                .nombreCategoria(nombreCategoria)
                .cantidadDisponible(libro.getCantidadDisponible())
                .cantidadTotal(libro.getCantidadTotal())
                .estado(libro.getEstado())
                .fechaPublicacion(libro.getFechaPublicacion())
                .editorial(libro.getEditorial())
                .descripcion(libro.getDescripcion())
                .ubicacion(libro.getUbicacion())
                .fechaRegistro(libro.getFechaRegistro())
                .ejemplaresDisponibles(ejemplaresDisponibles)
                .vecesPrestado(0) // TODO: Calcular desde préstamos
                .build();
    }
}