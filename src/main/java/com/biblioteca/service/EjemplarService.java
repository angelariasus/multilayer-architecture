package com.biblioteca.service;

import com.biblioteca.dao.EjemplarDAO;
import com.biblioteca.dao.LibroDAO;
import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.dto.EjemplarDTO;
import com.biblioteca.model.Ejemplar;
import com.biblioteca.model.Libro;
import com.biblioteca.exception.ServiceException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EjemplarService implements BaseService<Ejemplar, EjemplarDTO, Long> {
    
    private final EjemplarDAO ejemplarDAO;
    private final LibroDAO libroDAO;
    private final PrestamoDAO prestamoDAO;
    
    public EjemplarService() {
        this.ejemplarDAO = new EjemplarDAO();
        this.libroDAO = new LibroDAO();
        this.prestamoDAO = new PrestamoDAO();
    }
    
    @Override
    public EjemplarDTO save(EjemplarDTO dto) {
        try {
            // Validaciones
            validateEjemplarDTO(dto);
            
            // Verificar que el libro existe
            Libro libro = libroDAO.findById(dto.getIdLibro());
            if (libro == null) {
                throw new ServiceException("El libro especificado no existe");
            }
            
            // Verificar código único
            Ejemplar existente = ejemplarDAO.findByCodigo(dto.getCodigoEjemplar());
            if (existente != null) {
                throw new ServiceException("Ya existe un ejemplar con ese código");
            }
            
            Ejemplar ejemplar = Ejemplar.builder()
                    .idLibro(dto.getIdLibro())
                    .codigoEjemplar(dto.getCodigoEjemplar().trim())
                    .ubicacion(dto.getUbicacion())
                    .estado(dto.getEstado() != null ? dto.getEstado() : "Disponible")
                    .fechaAdquisicion(dto.getFechaAdquisicion() != null ? dto.getFechaAdquisicion() : LocalDate.now())
                    .observaciones(dto.getObservaciones())
                    .estadoFisico(dto.getEstadoFisico() != null ? dto.getEstadoFisico() : "Bueno")
                    .build();
            
            Ejemplar saved = ejemplarDAO.save(ejemplar);
            
            // Actualizar cantidad total del libro
            libro.setCantidadTotal(libro.getCantidadTotal() + 1);
            if ("Disponible".equals(ejemplar.getEstado())) {
                libro.setCantidadDisponible(libro.getCantidadDisponible() + 1);
            }
            libroDAO.update(libro);
            
            return convertToDTO(saved);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar ejemplar: " + e.getMessage(), e);
        }
    }
    
    @Override
    public EjemplarDTO findById(Long id) {
        try {
            Ejemplar ejemplar = ejemplarDAO.findById(id);
            return ejemplar != null ? convertToDTO(ejemplar) : null;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar ejemplar: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<EjemplarDTO> findAll() {
        try {
            return ejemplarDAO.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener ejemplares: " + e.getMessage(), e);
        }
    }
    
    @Override
    public EjemplarDTO update(EjemplarDTO dto) {
        try {
            if (dto.getIdEjemplar() == null) {
                throw new ServiceException("ID de ejemplar es requerido para actualizar");
            }
            
            Ejemplar existente = ejemplarDAO.findById(dto.getIdEjemplar());
            if (existente == null) {
                throw new ServiceException("Ejemplar no encontrado");
            }
            
            // Verificar código único (excluyendo el ejemplar actual)
            Ejemplar conMismoCodigo = ejemplarDAO.findByCodigo(dto.getCodigoEjemplar());
            if (conMismoCodigo != null && !conMismoCodigo.getIdEjemplar().equals(dto.getIdEjemplar())) {
                throw new ServiceException("Ya existe otro ejemplar con ese código");
            }
            
            // Si cambia el estado, actualizar las cantidades del libro
            String estadoAnterior = existente.getEstado();
            String estadoNuevo = dto.getEstado();
            
            Ejemplar ejemplar = Ejemplar.builder()
                    .idEjemplar(dto.getIdEjemplar())
                    .idLibro(existente.getIdLibro()) // No se puede cambiar el libro
                    .codigoEjemplar(dto.getCodigoEjemplar().trim())
                    .ubicacion(dto.getUbicacion())
                    .estado(estadoNuevo)
                    .fechaAdquisicion(existente.getFechaAdquisicion())
                    .observaciones(dto.getObservaciones())
                    .estadoFisico(dto.getEstadoFisico())
                    .build();
            
            // Actualizar cantidades del libro si cambió el estado
            if (!estadoAnterior.equals(estadoNuevo)) {
                actualizarCantidadesLibro(existente.getIdLibro(), estadoAnterior, estadoNuevo);
            }
            
            Ejemplar updated = ejemplarDAO.update(ejemplar);
            return convertToDTO(updated);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar ejemplar: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(Long id) {
        try {
            Ejemplar ejemplar = ejemplarDAO.findById(id);
            if (ejemplar == null) {
                throw new ServiceException("Ejemplar no encontrado");
            }
            
            // Verificar que no esté prestado
            if ("Prestado".equals(ejemplar.getEstado())) {
                throw new ServiceException("No se puede eliminar un ejemplar que está prestado");
            }
            
            // Actualizar cantidades del libro
            Libro libro = libroDAO.findById(ejemplar.getIdLibro());
            if (libro != null) {
                libro.setCantidadTotal(libro.getCantidadTotal() - 1);
                if ("Disponible".equals(ejemplar.getEstado())) {
                    libro.setCantidadDisponible(libro.getCantidadDisponible() - 1);
                }
                libroDAO.update(libro);
            }
            
            return ejemplarDAO.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar ejemplar: " + e.getMessage(), e);
        }
    }
    
    public List<EjemplarDTO> findByLibro(Long idLibro) {
        try {
            return ejemplarDAO.findByLibro(idLibro).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar ejemplares por libro: " + e.getMessage(), e);
        }
    }
    
    public List<EjemplarDTO> findDisponiblesByLibro(Long idLibro) {
        try {
            return ejemplarDAO.findDisponiblesByLibro(idLibro).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar ejemplares disponibles: " + e.getMessage(), e);
        }
    }
    
    public EjemplarDTO findByCodigo(String codigo) {
        try {
            Ejemplar ejemplar = ejemplarDAO.findByCodigo(codigo);
            return ejemplar != null ? convertToDTO(ejemplar) : null;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar ejemplar por código: " + e.getMessage(), e);
        }
    }
    
    public EjemplarDTO cambiarEstado(Long idEjemplar, String nuevoEstado, String observaciones) {
        try {
            Ejemplar ejemplar = ejemplarDAO.findById(idEjemplar);
            if (ejemplar == null) {
                throw new ServiceException("Ejemplar no encontrado");
            }
            
            String estadoAnterior = ejemplar.getEstado();
            ejemplar.setEstado(nuevoEstado);
            ejemplar.setObservaciones(observaciones);
            
            // Actualizar cantidades del libro
            actualizarCantidadesLibro(ejemplar.getIdLibro(), estadoAnterior, nuevoEstado);
            
            Ejemplar updated = ejemplarDAO.update(ejemplar);
            return convertToDTO(updated);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al cambiar estado del ejemplar: " + e.getMessage(), e);
        }
    }
    
    private void actualizarCantidadesLibro(Long idLibro, String estadoAnterior, String estadoNuevo) {
        Libro libro = libroDAO.findById(idLibro);
        if (libro == null) return;
        
        // Si cambió de disponible a no disponible
        if ("Disponible".equals(estadoAnterior) && !"Disponible".equals(estadoNuevo)) {
            libro.setCantidadDisponible(libro.getCantidadDisponible() - 1);
        }
        // Si cambió de no disponible a disponible
        else if (!"Disponible".equals(estadoAnterior) && "Disponible".equals(estadoNuevo)) {
            libro.setCantidadDisponible(libro.getCantidadDisponible() + 1);
        }
        
        libroDAO.update(libro);
    }
    
    private void validateEjemplarDTO(EjemplarDTO dto) throws ServiceException {
        if (dto.getIdLibro() == null) {
            throw new ServiceException("ID de libro es obligatorio");
        }
        if (dto.getCodigoEjemplar() == null || dto.getCodigoEjemplar().trim().isEmpty()) {
            throw new ServiceException("Código de ejemplar es obligatorio");
        }
    }
    
    private EjemplarDTO convertToDTO(Ejemplar ejemplar) {
        // Obtener título del libro
        Libro libro = libroDAO.findById(ejemplar.getIdLibro());
        String tituloLibro = libro != null ? libro.getTitulo() : "Libro no encontrado";
        
        // Determinar si está disponible para préstamo
        boolean disponibleParaPrestamo = "Disponible".equals(ejemplar.getEstado()) && 
                                       "Bueno".equals(ejemplar.getEstadoFisico()) || 
                                       "Excelente".equals(ejemplar.getEstadoFisico());
        
        return EjemplarDTO.builder()
                .idEjemplar(ejemplar.getIdEjemplar())
                .idLibro(ejemplar.getIdLibro())
                .tituloLibro(tituloLibro)
                .codigoEjemplar(ejemplar.getCodigoEjemplar())
                .ubicacion(ejemplar.getUbicacion())
                .estado(ejemplar.getEstado())
                .fechaAdquisicion(ejemplar.getFechaAdquisicion())
                .observaciones(ejemplar.getObservaciones())
                .estadoFisico(ejemplar.getEstadoFisico())
                .disponibleParaPrestamo(disponibleParaPrestamo)
                .build();
    }
}