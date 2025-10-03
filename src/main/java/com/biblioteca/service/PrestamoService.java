package com.biblioteca.service;

import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.dao.LibroDAO;
import com.biblioteca.dao.EjemplarDAO;
import com.biblioteca.dao.MultaDAO;
import com.biblioteca.dto.PrestamoDTO;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Ejemplar;
import com.biblioteca.model.Multa;
import com.biblioteca.exception.ServiceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class PrestamoService implements BaseService<Prestamo, PrestamoDTO, Long> {
    
    private final PrestamoDAO prestamoDAO;
    private final UsuarioDAO usuarioDAO;
    private final LibroDAO libroDAO;
    private final EjemplarDAO ejemplarDAO;
    private final MultaDAO multaDAO;
    
    public PrestamoService() {
        this.prestamoDAO = new PrestamoDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.libroDAO = new LibroDAO();
        this.ejemplarDAO = new EjemplarDAO();
        this.multaDAO = new MultaDAO();
    }
    
    @Override
    public PrestamoDTO save(PrestamoDTO dto) {
        try {
            // Validaciones
            validatePrestamoDTO(dto);
            
            // Verificar que el usuario existe y está activo
            Usuario usuario = usuarioDAO.findById(dto.getIdUsuario());
            if (usuario == null || !"Activo".equals(usuario.getEstado())) {
                throw new ServiceException("Usuario no válido o inactivo");
            }
            
            // Verificar que el ejemplar existe y está disponible
            Ejemplar ejemplar = ejemplarDAO.findById(dto.getIdEjemplar());
            if (ejemplar == null || !"Disponible".equals(ejemplar.getEstado())) {
                throw new ServiceException("Ejemplar no disponible");
            }
            
            // Verificar límites de préstamo por tipo de usuario
            List<Prestamo> prestamosActivos = prestamoDAO.findActivosByUsuario(dto.getIdUsuario());
            int limite = getLimitePrestamoPorTipo(usuario.getTipo());
            if (prestamosActivos.size() >= limite) {
                throw new ServiceException("Usuario ha alcanzado el límite de préstamos simultáneos");
            }
            
            // Verificar que no tenga multas pendientes
            List<Multa> multasPendientes = multaDAO.findByUsuario(dto.getIdUsuario()).stream()
                    .filter(m -> "Pendiente".equals(m.getEstado()))
                    .collect(Collectors.toList());
            if (!multasPendientes.isEmpty()) {
                throw new ServiceException("Usuario tiene multas pendientes");
            }
            
            // Crear préstamo
            LocalDate fechaPrestamo = LocalDate.now();
            LocalDate fechaDevolucion = fechaPrestamo.plusDays(getDiasPrestamoPorTipo(usuario.getTipo()));
            
            Prestamo prestamo = Prestamo.builder()
                    .idUsuario(dto.getIdUsuario())
                    .idLibro(ejemplar.getIdLibro())
                    .idEjemplar(dto.getIdEjemplar())
                    .fechaPrestamo(fechaPrestamo)
                    .fechaDevolucionEsperada(fechaDevolucion)
                    .estado("Activo")
                    .observaciones(dto.getObservaciones())
                    .build();
            
            // Actualizar estado del ejemplar
            ejemplar.setEstado("Prestado");
            ejemplarDAO.update(ejemplar);
            
            // Actualizar cantidad disponible del libro
            Libro libro = libroDAO.findById(ejemplar.getIdLibro());
            libro.setCantidadDisponible(libro.getCantidadDisponible() - 1);
            libroDAO.update(libro);
            
            Prestamo saved = prestamoDAO.save(prestamo);
            return convertToDTO(saved);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al crear préstamo: " + e.getMessage(), e);
        }
    }
    
    @Override
    public PrestamoDTO findById(Long id) {
        try {
            Prestamo prestamo = prestamoDAO.findById(id);
            return prestamo != null ? convertToDTO(prestamo) : null;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar préstamo: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<PrestamoDTO> findAll() {
        try {
            return prestamoDAO.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener préstamos: " + e.getMessage(), e);
        }
    }
    
    @Override
    public PrestamoDTO update(PrestamoDTO dto) {
        try {
            if (dto.getIdPrestamo() == null) {
                throw new ServiceException("ID de préstamo es requerido");
            }
            
            Prestamo existente = prestamoDAO.findById(dto.getIdPrestamo());
            if (existente == null) {
                throw new ServiceException("Préstamo no encontrado");
            }
            
            existente.setObservaciones(dto.getObservaciones());
            Prestamo updated = prestamoDAO.update(existente);
            return convertToDTO(updated);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar préstamo: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(Long id) {
        try {
            return prestamoDAO.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar préstamo: " + e.getMessage(), e);
        }
    }
    
    public PrestamoDTO devolver(Long idPrestamo, String observaciones) {
        try {
            Prestamo prestamo = prestamoDAO.findById(idPrestamo);
            if (prestamo == null) {
                throw new ServiceException("Préstamo no encontrado");
            }
            
            if (!"Activo".equals(prestamo.getEstado())) {
                throw new ServiceException("El préstamo no está activo");
            }
            
            LocalDate fechaDevolucion = LocalDate.now();
            prestamo.setFechaDevolucionReal(fechaDevolucion);
            prestamo.setEstado("Devuelto");
            prestamo.setObservaciones(observaciones);
            
            // Actualizar estado del ejemplar
            Ejemplar ejemplar = ejemplarDAO.findById(prestamo.getIdEjemplar());
            ejemplar.setEstado("Disponible");
            ejemplarDAO.update(ejemplar);
            
            // Actualizar cantidad disponible del libro
            Libro libro = libroDAO.findById(prestamo.getIdLibro());
            libro.setCantidadDisponible(libro.getCantidadDisponible() + 1);
            libroDAO.update(libro);
            
            // Generar multa si está vencido
            if (fechaDevolucion.isAfter(prestamo.getFechaDevolucionEsperada())) {
                generarMulta(prestamo, fechaDevolucion);
            }
            
            Prestamo updated = prestamoDAO.update(prestamo);
            return convertToDTO(updated);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al devolver libro: " + e.getMessage(), e);
        }
    }
    
    public List<PrestamoDTO> findByUsuario(Long idUsuario) {
        try {
            return prestamoDAO.findByUsuario(idUsuario).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar préstamos por usuario: " + e.getMessage(), e);
        }
    }
    
    public List<PrestamoDTO> findVencidos() {
        try {
            return prestamoDAO.findVencidos().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar préstamos vencidos: " + e.getMessage(), e);
        }
    }
    
    private void generarMulta(Prestamo prestamo, LocalDate fechaDevolucion) {
        int diasRetraso = (int) ChronoUnit.DAYS.between(prestamo.getFechaDevolucionEsperada(), fechaDevolucion);
        BigDecimal montoPorDia = new BigDecimal("2.00"); // S/. 2.00 por día
        BigDecimal montoTotal = montoPorDia.multiply(BigDecimal.valueOf(diasRetraso));
        
        Multa multa = Multa.builder()
                .idPrestamo(prestamo.getIdPrestamo())
                .monto(montoTotal)
                .fechaGeneracion(LocalDate.now())
                .estado("Pendiente")
                .motivo("Devolución tardía")
                .diasRetraso(diasRetraso)
                .build();
        
        multaDAO.save(multa);
    }
    
    private int getLimitePrestamoPorTipo(String tipoUsuario) {
        switch (tipoUsuario) {
            case "Estudiante": return 3;
            case "Docente": return 5;
            case "Administrativo": return 4;
            case "Bibliotecario": return 10;
            default: return 2;
        }
    }
    
    private int getDiasPrestamoPorTipo(String tipoUsuario) {
        switch (tipoUsuario) {
            case "Estudiante": return 7;
            case "Docente": return 14;
            case "Administrativo": return 10;
            case "Bibliotecario": return 30;
            default: return 7;
        }
    }
    
    private void validatePrestamoDTO(PrestamoDTO dto) throws ServiceException {
        if (dto.getIdUsuario() == null) {
            throw new ServiceException("ID de usuario es obligatorio");
        }
        if (dto.getIdEjemplar() == null) {
            throw new ServiceException("ID de ejemplar es obligatorio");
        }
    }
    
    private PrestamoDTO convertToDTO(Prestamo prestamo) {
        // Obtener datos relacionados
        Usuario usuario = usuarioDAO.findById(prestamo.getIdUsuario());
        Libro libro = libroDAO.findById(prestamo.getIdLibro());
        Ejemplar ejemplar = ejemplarDAO.findById(prestamo.getIdEjemplar());
        
        // Calcular días de retraso
        int diasRetraso = 0;
        boolean estaVencido = false;
        if ("Activo".equals(prestamo.getEstado()) && LocalDate.now().isAfter(prestamo.getFechaDevolucionEsperada())) {
            diasRetraso = (int) ChronoUnit.DAYS.between(prestamo.getFechaDevolucionEsperada(), LocalDate.now());
            estaVencido = true;
        }
        
        // Verificar si tiene multa
        boolean tieneMulta = !multaDAO.findByPrestamo(prestamo.getIdPrestamo()).isEmpty();
        
        return PrestamoDTO.builder()
                .idPrestamo(prestamo.getIdPrestamo())
                .idUsuario(prestamo.getIdUsuario())
                .nombreUsuario(usuario != null ? usuario.getNombre() : "Usuario no encontrado")
                .tipoUsuario(usuario != null ? usuario.getTipo() : "")
                .idLibro(prestamo.getIdLibro())
                .tituloLibro(libro != null ? libro.getTitulo() : "Libro no encontrado")
                .autorLibro(libro != null ? libro.getAutor() : "")
                .idEjemplar(prestamo.getIdEjemplar())
                .codigoEjemplar(ejemplar != null ? ejemplar.getCodigoEjemplar() : "")
                .fechaPrestamo(prestamo.getFechaPrestamo())
                .fechaDevolucionEsperada(prestamo.getFechaDevolucionEsperada())
                .fechaDevolucionReal(prestamo.getFechaDevolucionReal())
                .estado(prestamo.getEstado())
                .observaciones(prestamo.getObservaciones())
                .diasRetraso(diasRetraso)
                .tieneMulta(tieneMulta)
                .estaVencido(estaVencido)
                .build();
    }
}