package com.biblioteca.service;

import com.biblioteca.dao.MultaDAO;
import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.dao.LibroDAO;
import com.biblioteca.dto.MultaDTO;
import com.biblioteca.model.Multa;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.Libro;
import com.biblioteca.exception.ServiceException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class MultaService implements BaseService<Multa, MultaDTO, Long> {
    
    private final MultaDAO multaDAO;
    private final PrestamoDAO prestamoDAO;
    private final UsuarioDAO usuarioDAO;
    private final LibroDAO libroDAO;
    
    public MultaService() {
        this.multaDAO = new MultaDAO();
        this.prestamoDAO = new PrestamoDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.libroDAO = new LibroDAO();
    }
    
    @Override
    public MultaDTO save(MultaDTO dto) {
        try {
            // Validaciones
            validateMultaDTO(dto);
            
            // Verificar que el préstamo existe
            Prestamo prestamo = prestamoDAO.findById(dto.getIdPrestamo());
            if (prestamo == null) {
                throw new ServiceException("El préstamo especificado no existe");
            }
            
            Multa multa = Multa.builder()
                    .idPrestamo(dto.getIdPrestamo())
                    .monto(dto.getMonto())
                    .fechaGeneracion(dto.getFechaGeneracion() != null ? dto.getFechaGeneracion() : LocalDate.now())
                    .estado(dto.getEstado() != null ? dto.getEstado() : "Pendiente")
                    .motivo(dto.getMotivo())
                    .diasRetraso(dto.getDiasRetraso())
                    .build();
            
            Multa saved = multaDAO.save(multa);
            return convertToDTO(saved);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar multa: " + e.getMessage(), e);
        }
    }
    
    @Override
    public MultaDTO findById(Long id) {
        try {
            Multa multa = multaDAO.findById(id);
            return multa != null ? convertToDTO(multa) : null;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar multa: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<MultaDTO> findAll() {
        try {
            return multaDAO.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener multas: " + e.getMessage(), e);
        }
    }
    
    @Override
    public MultaDTO update(MultaDTO dto) {
        try {
            if (dto.getIdMulta() == null) {
                throw new ServiceException("ID de multa es requerido para actualizar");
            }
            
            Multa existente = multaDAO.findById(dto.getIdMulta());
            if (existente == null) {
                throw new ServiceException("Multa no encontrada");
            }
            
            // Solo se puede actualizar el estado y fecha de pago
            existente.setEstado(dto.getEstado());
            if ("Pagada".equals(dto.getEstado())) {
                existente.setFechaPago(LocalDate.now());
            }
            
            Multa updated = multaDAO.update(existente);
            return convertToDTO(updated);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar multa: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(Long id) {
        try {
            Multa multa = multaDAO.findById(id);
            if (multa == null) {
                throw new ServiceException("Multa no encontrada");
            }
            
            // Solo se pueden eliminar multas canceladas
            if (!"Cancelada".equals(multa.getEstado())) {
                throw new ServiceException("Solo se pueden eliminar multas canceladas");
            }
            
            return multaDAO.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar multa: " + e.getMessage(), e);
        }
    }
    
    public MultaDTO pagarMulta(Long idMulta) {
        try {
            Multa multa = multaDAO.findById(idMulta);
            if (multa == null) {
                throw new ServiceException("Multa no encontrada");
            }
            
            if (!"Pendiente".equals(multa.getEstado())) {
                throw new ServiceException("Solo se pueden pagar multas pendientes");
            }
            
            multa.setEstado("Pagada");
            multa.setFechaPago(LocalDate.now());
            
            Multa updated = multaDAO.update(multa);
            return convertToDTO(updated);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al pagar multa: " + e.getMessage(), e);
        }
    }
    
    public MultaDTO cancelarMulta(Long idMulta, String motivo) {
        try {
            Multa multa = multaDAO.findById(idMulta);
            if (multa == null) {
                throw new ServiceException("Multa no encontrada");
            }
            
            if (!"Pendiente".equals(multa.getEstado())) {
                throw new ServiceException("Solo se pueden cancelar multas pendientes");
            }
            
            multa.setEstado("Cancelada");
            multa.setMotivo(multa.getMotivo() + " - CANCELADA: " + motivo);
            
            Multa updated = multaDAO.update(multa);
            return convertToDTO(updated);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al cancelar multa: " + e.getMessage(), e);
        }
    }
    
    public List<MultaDTO> findByUsuario(Long idUsuario) {
        try {
            return multaDAO.findByUsuario(idUsuario).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar multas por usuario: " + e.getMessage(), e);
        }
    }
    
    public List<MultaDTO> findPendientes() {
        try {
            return multaDAO.findPendientes().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar multas pendientes: " + e.getMessage(), e);
        }
    }
    
    public List<MultaDTO> findByPrestamo(Long idPrestamo) {
        try {
            return multaDAO.findByPrestamo(idPrestamo).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar multas por préstamo: " + e.getMessage(), e);
        }
    }
    
    public MultaDTO generarMultaPorRetraso(Long idPrestamo) {
        try {
            Prestamo prestamo = prestamoDAO.findById(idPrestamo);
            if (prestamo == null) {
                throw new ServiceException("Préstamo no encontrado");
            }
            
            if (!"Activo".equals(prestamo.getEstado())) {
                throw new ServiceException("El préstamo debe estar activo");
            }
            
            LocalDate fechaActual = LocalDate.now();
            if (!fechaActual.isAfter(prestamo.getFechaDevolucionEsperada())) {
                throw new ServiceException("El préstamo aún no está vencido");
            }
            
            // Verificar si ya tiene multa por este préstamo
            List<Multa> multasExistentes = multaDAO.findByPrestamo(idPrestamo);
            if (!multasExistentes.isEmpty()) {
                throw new ServiceException("Ya existe una multa para este préstamo");
            }
            
            int diasRetraso = (int) ChronoUnit.DAYS.between(prestamo.getFechaDevolucionEsperada(), fechaActual);
            BigDecimal montoPorDia = new BigDecimal("2.00"); // S/. 2.00 por día
            BigDecimal montoTotal = montoPorDia.multiply(BigDecimal.valueOf(diasRetraso));
            
            Multa multa = Multa.builder()
                    .idPrestamo(idPrestamo)
                    .monto(montoTotal)
                    .fechaGeneracion(fechaActual)
                    .estado("Pendiente")
                    .motivo("Devolución tardía")
                    .diasRetraso(diasRetraso)
                    .build();
            
            Multa saved = multaDAO.save(multa);
            return convertToDTO(saved);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar multa por retraso: " + e.getMessage(), e);
        }
    }
    
    public BigDecimal calcularMontoConInteres(Long idMulta) {
        try {
            Multa multa = multaDAO.findById(idMulta);
            if (multa == null) {
                throw new ServiceException("Multa no encontrada");
            }
            
            if (!"Pendiente".equals(multa.getEstado())) {
                return multa.getMonto();
            }
            
            // Calcular interés por días transcurridos (0.5% diario)
            long diasTranscurridos = ChronoUnit.DAYS.between(multa.getFechaGeneracion(), LocalDate.now());
            if (diasTranscurridos <= 0) {
                return multa.getMonto();
            }
            
            BigDecimal tasaInteresDiario = new BigDecimal("0.005"); // 0.5%
            BigDecimal factorInteres = BigDecimal.ONE.add(tasaInteresDiario.multiply(BigDecimal.valueOf(diasTranscurridos)));
            
            return multa.getMonto().multiply(factorInteres).setScale(2, BigDecimal.ROUND_HALF_UP);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al calcular monto con interés: " + e.getMessage(), e);
        }
    }
    
    private void validateMultaDTO(MultaDTO dto) throws ServiceException {
        if (dto.getIdPrestamo() == null) {
            throw new ServiceException("ID de préstamo es obligatorio");
        }
        if (dto.getMonto() == null || dto.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("El monto debe ser mayor a cero");
        }
    }
    
    private MultaDTO convertToDTO(Multa multa) {
        // Obtener datos del préstamo y usuario
        Prestamo prestamo = prestamoDAO.findById(multa.getIdPrestamo());
        String nombreUsuario = "Usuario no encontrado";
        String tituloLibro = "Libro no encontrado";
        
        if (prestamo != null) {
            Usuario usuario = usuarioDAO.findById(prestamo.getIdUsuario());
            if (usuario != null) {
                nombreUsuario = usuario.getNombre();
            }
            
            Libro libro = libroDAO.findById(prestamo.getIdLibro());
            if (libro != null) {
                tituloLibro = libro.getTitulo();
            }
        }
        
        // Calcular días de vencimiento
        int diasVencimiento = 0;
        if ("Pendiente".equals(multa.getEstado())) {
            diasVencimiento = (int) ChronoUnit.DAYS.between(multa.getFechaGeneracion(), LocalDate.now());
        }
        
        // Calcular monto con interés
        BigDecimal montoConInteres = calcularMontoConInteres(multa.getIdMulta());
        
        return MultaDTO.builder()
                .idMulta(multa.getIdMulta())
                .idPrestamo(multa.getIdPrestamo())
                .nombreUsuario(nombreUsuario)
                .tituloLibro(tituloLibro)
                .monto(multa.getMonto())
                .fechaGeneracion(multa.getFechaGeneracion())
                .fechaPago(multa.getFechaPago())
                .estado(multa.getEstado())
                .motivo(multa.getMotivo())
                .diasRetraso(multa.getDiasRetraso())
                .diasVencimiento(diasVencimiento)
                .montoConInteres(montoConInteres)
                .build();
    }
}