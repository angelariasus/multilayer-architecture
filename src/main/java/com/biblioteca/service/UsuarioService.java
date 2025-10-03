package com.biblioteca.service;

import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.dao.MultaDAO;
import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.model.Usuario;
import com.biblioteca.exception.ServiceException;

import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioService implements BaseService<Usuario, UsuarioDTO, Long> {
    
    private final UsuarioDAO usuarioDAO;
    private final PrestamoDAO prestamoDAO;
    private final MultaDAO multaDAO;
    
    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
        this.prestamoDAO = new PrestamoDAO();
        this.multaDAO = new MultaDAO();
    }
    
    @Override
    public UsuarioDTO save(UsuarioDTO dto) {
        try {
            // Validaciones
            validateUsuarioDTO(dto);
            
            // Verificar username único
            Usuario existente = usuarioDAO.findByUsername(dto.getUsername());
            if (existente != null) {
                throw new ServiceException("Ya existe un usuario con ese username");
            }
            
            Usuario usuario = Usuario.builder()
                    .nombre(dto.getNombre().trim())
                    .tipo(dto.getTipo())
                    .estado(dto.getEstado() != null ? dto.getEstado() : "Activo")
                    .username(dto.getUsername().trim())
                    .password(encryptPassword("123456")) // Password por defecto
                    .email(dto.getEmail())
                    .telefono(dto.getTelefono())
                    .direccion(dto.getDireccion())
                    .fechaRegistro(LocalDate.now())
                    .build();
            
            Usuario saved = usuarioDAO.save(usuario);
            return convertToDTO(saved);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar usuario: " + e.getMessage(), e);
        }
    }
    
    @Override
    public UsuarioDTO findById(Long id) {
        try {
            Usuario usuario = usuarioDAO.findById(id);
            return usuario != null ? convertToDTO(usuario) : null;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<UsuarioDTO> findAll() {
        try {
            return usuarioDAO.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener usuarios: " + e.getMessage(), e);
        }
    }
    
    @Override
    public UsuarioDTO update(UsuarioDTO dto) {
        try {
            if (dto.getIdUsuario() == null) {
                throw new ServiceException("ID de usuario es requerido para actualizar");
            }
            
            Usuario existente = usuarioDAO.findById(dto.getIdUsuario());
            if (existente == null) {
                throw new ServiceException("Usuario no encontrado");
            }
            
            // Verificar username único (excluyendo el usuario actual)
            Usuario conMismoUsername = usuarioDAO.findByUsername(dto.getUsername());
            if (conMismoUsername != null && !conMismoUsername.getIdUsuario().equals(dto.getIdUsuario())) {
                throw new ServiceException("Ya existe otro usuario con ese username");
            }
            
            Usuario usuario = Usuario.builder()
                    .idUsuario(dto.getIdUsuario())
                    .nombre(dto.getNombre().trim())
                    .tipo(dto.getTipo())
                    .estado(dto.getEstado())
                    .username(existente.getUsername()) // No se cambia username
                    .password(existente.getPassword()) // No se cambia password aquí
                    .email(dto.getEmail())
                    .telefono(dto.getTelefono())
                    .direccion(dto.getDireccion())
                    .fechaRegistro(existente.getFechaRegistro())
                    .build();
            
            Usuario updated = usuarioDAO.update(usuario);
            return convertToDTO(updated);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(Long id) {
        try {
            // Verificar que no tenga préstamos activos
            List<com.biblioteca.model.Prestamo> prestamosActivos = prestamoDAO.findActivosByUsuario(id);
            if (!prestamosActivos.isEmpty()) {
                throw new ServiceException("No se puede eliminar el usuario porque tiene préstamos activos");
            }
            
            return usuarioDAO.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage(), e);
        }
    }
    
    public UsuarioDTO login(String username, String password) {
        try {
            Usuario usuario = usuarioDAO.findByUsername(username);
            if (usuario == null) {
                throw new ServiceException("Credenciales inválidas");
            }
            
            if (!"Activo".equals(usuario.getEstado())) {
                throw new ServiceException("Usuario inactivo o bloqueado");
            }
            
            if (!verifyPassword(password, usuario.getPassword())) {
                throw new ServiceException("Credenciales inválidas");
            }
            
            return convertToDTO(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Error en login: " + e.getMessage(), e);
        }
    }
    
    public boolean changePassword(Long idUsuario, String currentPassword, String newPassword) {
        try {
            Usuario usuario = usuarioDAO.findById(idUsuario);
            if (usuario == null) {
                throw new ServiceException("Usuario no encontrado");
            }
            
            if (!verifyPassword(currentPassword, usuario.getPassword())) {
                throw new ServiceException("Password actual incorrecto");
            }
            
            usuario.setPassword(encryptPassword(newPassword));
            usuarioDAO.update(usuario);
            return true;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al cambiar password: " + e.getMessage(), e);
        }
    }
    
    public List<UsuarioDTO> findByTipo(String tipo) {
        try {
            return usuarioDAO.findByTipo(tipo).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar usuarios por tipo: " + e.getMessage(), e);
        }
    }
    
    private void validateUsuarioDTO(UsuarioDTO dto) throws ServiceException {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new ServiceException("El nombre es obligatorio");
        }
        if (dto.getTipo() == null) {
            throw new ServiceException("El tipo de usuario es obligatorio");
        }
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new ServiceException("El username es obligatorio");
        }
    }
    
    private UsuarioDTO convertToDTO(Usuario usuario) {
        // Obtener estadísticas del usuario
        int prestamosActivos = prestamoDAO.findActivosByUsuario(usuario.getIdUsuario()).size();
        int multasPendientes = (int) multaDAO.findByUsuario(usuario.getIdUsuario()).stream()
                .filter(m -> "Pendiente".equals(m.getEstado()))
                .count();
        
        return UsuarioDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .tipo(usuario.getTipo())
                .estado(usuario.getEstado())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .direccion(usuario.getDireccion())
                .fechaRegistro(usuario.getFechaRegistro())
                .prestamosActivos(prestamosActivos)
                .multasPendientes(multasPendientes)
                .build();
    }
    
    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedPassword) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar password", e);
        }
    }
    
    private boolean verifyPassword(String password, String hashedPassword) {
        return encryptPassword(password).equals(hashedPassword);
    }
}