package com.biblioteca.service;

import com.biblioteca.dao.ReservaDAO;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.dao.LibroDAO;
import com.biblioteca.dao.EjemplarDAO;
import com.biblioteca.dto.ReservaDTO;
import com.biblioteca.model.Reserva;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Ejemplar;
import com.biblioteca.exception.ServiceException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class ReservaService implements BaseService<Reserva, ReservaDTO, Long> {
    
    private final ReservaDAO reservaDAO;
    private final UsuarioDAO usuarioDAO;
    private final LibroDAO libroDAO;
    private final EjemplarDAO ejemplarDAO;
    
    public ReservaService() {
        this.reservaDAO = new ReservaDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.libroDAO = new LibroDAO();
        this.ejemplarDAO = new EjemplarDAO();
    }
    
    @Override
    public ReservaDTO save(ReservaDTO dto) {
        try {
            // Validaciones
            validateReservaDTO(dto);
            
            // Verificar que el usuario existe y está activo
            Usuario usuario = usuarioDAO.findById(dto.getIdUsuario());
            if (usuario == null || !"Activo".equals(usuario.getEstado())) {
                throw new ServiceException("Usuario no válido o inactivo");
            }
            
            // Verificar que el libro existe
            Libro libro = libroDAO.findById(dto.getIdLibro());
            if (libro == null) {
                throw new ServiceException("Libro no encontrado");
            }
            
            // Verificar que no tenga ya una reserva activa para este libro
            List<Reserva> reservasUsuario = reservaDAO.findByUsuario(dto.getIdUsuario());
            boolean tieneReservaActiva = reservasUsuario.stream()
                    .anyMatch(r -> r.getIdLibro().equals(dto.getIdLibro()) && "Activa".equals(r.getEstado()));
            
            if (tieneReservaActiva) {
                throw new ServiceException("Ya tiene una reserva activa para este libro");
            }
            
            // Verificar límite de reservas por usuario
            long reservasActivas = reservasUsuario.stream()
                    .filter(r -> "Activa".equals(r.getEstado()))
                    .count();
            
            int limiteReservas = getLimiteReservasPorTipo(usuario.getTipo());
            if (reservasActivas >= limiteReservas) {
                throw new ServiceException("Ha alcanzado el límite de reservas simultáneas");
            }
            
            // Solo permitir reserva si no hay ejemplares disponibles
            List<Ejemplar> ejemplaresDisponibles = ejemplarDAO.findDisponiblesByLibro(dto.getIdLibro());
            if (!ejemplaresDisponibles.isEmpty()) {
                throw new ServiceException("Hay ejemplares disponibles. No es necesario hacer reserva");
            }
            
            // Calcular posición en la cola
            List<Reserva> reservasLibro = reservaDAO.findActivasByLibro(dto.getIdLibro());
            int posicionCola = reservasLibro.size() + 1;
            
            // Calcular fecha de vencimiento (7 días desde la fecha de reserva)
            LocalDate fechaReserva = LocalDate.now();
            LocalDate fechaVencimiento = fechaReserva.plusDays(7);
            
            Reserva reserva = Reserva.builder()
                    .idUsuario(dto.getIdUsuario())
                    .idLibro(dto.getIdLibro())
                    .fechaReserva(fechaReserva)
                    .fechaVencimiento(fechaVencimiento)
                    .estado("Activa")
                    .posicionCola(posicionCola)
                    .observaciones(dto.getObservaciones())
                    .build();
            
            Reserva saved = reservaDAO.save(reserva);
            return convertToDTO(saved);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al crear reserva: " + e.getMessage(), e);
        }
    }
    
    @Override
    public ReservaDTO findById(Long id) {
        try {
            Reserva reserva = reservaDAO.findById(id);
            return reserva != null ? convertToDTO(reserva) : null;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar reserva: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<ReservaDTO> findAll() {
        try {
            return reservaDAO.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener reservas: " + e.getMessage(), e);
        }
    }
    
    @Override
    public ReservaDTO update(ReservaDTO dto) {
        try {
            if (dto.getIdReserva() == null) {
                throw new ServiceException("ID de reserva es requerido para actualizar");
            }
            
            Reserva existente = reservaDAO.findById(dto.getIdReserva());
            if (existente == null) {
                throw new ServiceException("Reserva no encontrada");
            }
            
            // Solo se pueden actualizar observaciones
            existente.setObservaciones(dto.getObservaciones());
            
            Reserva updated = reservaDAO.update(existente);
            return convertToDTO(updated);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar reserva: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(Long id) {
        try {
            Reserva reserva = reservaDAO.findById(id);
            if (reserva == null) {
                throw new ServiceException("Reserva no encontrada");
            }
            
            // Reorganizar la cola si se elimina una reserva activa
            if ("Activa".equals(reserva.getEstado())) {
                reorganizarCola(reserva.getIdLibro(), reserva.getPosicionCola());
            }
            
            return reservaDAO.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar reserva: " + e.getMessage(), e);
        }
    }
    
    public ReservaDTO cumplirReserva(Long idReserva) {
        try {
            Reserva reserva = reservaDAO.findById(idReserva);
            if (reserva == null) {
                throw new ServiceException("Reserva no encontrada");
            }
            
            if (!"Activa".equals(reserva.getEstado())) {
                throw new ServiceException("Solo se pueden cumplir reservas activas");
            }
            
            if (reserva.getPosicionCola() > 1) {
                throw new ServiceException("Esta reserva aún no está en primera posición");
            }
            
            // Verificar que hay ejemplares disponibles
            List<Ejemplar> ejemplaresDisponibles = ejemplarDAO.findDisponiblesByLibro(reserva.getIdLibro());
            if (ejemplaresDisponibles.isEmpty()) {
                throw new ServiceException("No hay ejemplares disponibles para cumplir la reserva");
            }
            
            reserva.setEstado("Cumplida");
            reserva.setFechaRecogida(LocalDate.now());
            
            // Reorganizar la cola
            reorganizarCola(reserva.getIdLibro(), reserva.getPosicionCola());
            
            Reserva updated = reservaDAO.update(reserva);
            return convertToDTO(updated);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al cumplir reserva: " + e.getMessage(), e);
        }
    }
    
    public ReservaDTO cancelarReserva(Long idReserva, String motivo) {
        try {
            Reserva reserva = reservaDAO.findById(idReserva);
            if (reserva == null) {
                throw new ServiceException("Reserva no encontrada");
            }
            
            if (!"Activa".equals(reserva.getEstado())) {
                throw new ServiceException("Solo se pueden cancelar reservas activas");
            }
            
            reserva.setEstado("Cancelada");
            reserva.setObservaciones(reserva.getObservaciones() + " - CANCELADA: " + motivo);
            
            // Reorganizar la cola
            reorganizarCola(reserva.getIdLibro(), reserva.getPosicionCola());
            
            Reserva updated = reservaDAO.update(reserva);
            return convertToDTO(updated);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al cancelar reserva: " + e.getMessage(), e);
        }
    }
    
    public List<ReservaDTO> findByUsuario(Long idUsuario) {
        try {
            return reservaDAO.findByUsuario(idUsuario).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar reservas por usuario: " + e.getMessage(), e);
        }
    }
    
    public List<ReservaDTO> findActivasByLibro(Long idLibro) {
        try {
            return reservaDAO.findActivasByLibro(idLibro).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar reservas activas por libro: " + e.getMessage(), e);
        }
    }
    
    public List<ReservaDTO> findVencidas() {
        try {
            return reservaDAO.findVencidas().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar reservas vencidas: " + e.getMessage(), e);
        }
    }
    
    public void procesarReservasVencidas() {
        try {
            List<Reserva> reservasVencidas = reservaDAO.findVencidas();
            
            for (Reserva reserva : reservasVencidas) {
                reserva.setEstado("Vencida");
                reservaDAO.update(reserva);
                
                // Reorganizar la cola
                reorganizarCola(reserva.getIdLibro(), reserva.getPosicionCola());
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar reservas vencidas: " + e.getMessage(), e);
        }
    }
    
    public void notificarDisponibilidad(Long idLibro) {
        try {
            // Buscar la primera reserva activa en la cola
            List<Reserva> reservasActivas = reservaDAO.findActivasByLibro(idLibro);
            if (!reservasActivas.isEmpty()) {
                Reserva primeraReserva = reservasActivas.get(0);
                
                // Extender fecha de vencimiento para dar tiempo a recoger
                primeraReserva.setFechaVencimiento(LocalDate.now().plusDays(2));
                reservaDAO.update(primeraReserva);
                
                // TODO: Enviar notificación al usuario
                System.out.println("Notificación enviada al usuario: " + primeraReserva.getIdUsuario());
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error al notificar disponibilidad: " + e.getMessage(), e);
        }
    }
    
    private void reorganizarCola(Long idLibro, int posicionEliminada) {
        List<Reserva> reservasActivas = reservaDAO.findActivasByLibro(idLibro);
        
        for (Reserva reserva : reservasActivas) {
            if (reserva.getPosicionCola() > posicionEliminada) {
                reserva.setPosicionCola(reserva.getPosicionCola() - 1);
                reservaDAO.update(reserva);
            }
        }
    }
    
    private int getLimiteReservasPorTipo(String tipoUsuario) {
        switch (tipoUsuario) {
            case "Estudiante": return 2;
            case "Docente": return 3;
            case "Administrativo": return 3;
            case "Bibliotecario": return 5;
            default: return 1;
        }
    }
    
    private void validateReservaDTO(ReservaDTO dto) throws ServiceException {
        if (dto.getIdUsuario() == null) {
            throw new ServiceException("ID de usuario es obligatorio");
        }
        if (dto.getIdLibro() == null) {
            throw new ServiceException("ID de libro es obligatorio");
        }
    }
    
    private ReservaDTO convertToDTO(Reserva reserva) {
        // Obtener datos relacionados
        Usuario usuario = usuarioDAO.findById(reserva.getIdUsuario());
        Libro libro = libroDAO.findById(reserva.getIdLibro());
        
        String nombreUsuario = usuario != null ? usuario.getNombre() : "Usuario no encontrado";
        String tituloLibro = libro != null ? libro.getTitulo() : "Libro no encontrado";
        String autorLibro = libro != null ? libro.getAutor() : "";
        
        // Calcular días restantes
        int diasRestantes = 0;
        if ("Activa".equals(reserva.getEstado())) {
            diasRestantes = (int) ChronoUnit.DAYS.between(LocalDate.now(), reserva.getFechaVencimiento());
        }
        
        // Determinar si puede recoger (primera posición y hay ejemplares disponibles)
        boolean puedeRecoger = false;
        if ("Activa".equals(reserva.getEstado()) && reserva.getPosicionCola() == 1) {
            List<Ejemplar> ejemplaresDisponibles = ejemplarDAO.findDisponiblesByLibro(reserva.getIdLibro());
            puedeRecoger = !ejemplaresDisponibles.isEmpty();
        }
        
        return ReservaDTO.builder()
                .idReserva(reserva.getIdReserva())
                .idUsuario(reserva.getIdUsuario())
                .nombreUsuario(nombreUsuario)
                .idLibro(reserva.getIdLibro())
                .tituloLibro(tituloLibro)
                .autorLibro(autorLibro)
                .fechaReserva(reserva.getFechaReserva())
                .fechaVencimiento(reserva.getFechaVencimiento())
                .fechaRecogida(reserva.getFechaRecogida())
                .estado(reserva.getEstado())
                .posicionCola(reserva.getPosicionCola())
                .observaciones(reserva.getObservaciones())
                .diasRestantes(diasRestantes)
                .puedeRecoger(puedeRecoger)
                .build();
    }
}