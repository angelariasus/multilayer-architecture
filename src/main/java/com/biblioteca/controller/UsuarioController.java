package com.biblioteca.controller;

import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.exception.ServiceException;
import com.biblioteca.service.UsuarioService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioController extends BaseController {
    
    private final UsuarioService usuarioService;
    
    public UsuarioController() {
        this.usuarioService = new UsuarioService();
    }
    
    public Map<String, Object> crearUsuario(UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO nuevoUsuario = usuarioService.save(usuarioDTO);
            return createSuccessResponse("Usuario creado exitosamente", nuevoUsuario);
        } catch (Exception e) {
            return createErrorResponse("Error al crear usuario", e);
        }
    }
    
    public Map<String, Object> obtenerUsuario(Long id) {
        try {
            UsuarioDTO usuario = usuarioService.findById(id);
            if (usuario != null) {
                return createSuccessResponse("Usuario encontrado", usuario);
            } else {
                return createErrorResponse("Usuario no encontrado");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al buscar usuario", e);
        }
    }
    
    public Map<String, Object> listarUsuarios() {
        try {
            List<UsuarioDTO> usuarios = usuarioService.findAll();
            return createSuccessResponse("Usuarios obtenidos exitosamente", usuarios);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener usuarios", e);
        }
    }
    
    public Map<String, Object> listarUsuariosPorTipo(String tipo) {
        try {
            List<UsuarioDTO> usuarios = usuarioService.findByTipo(tipo);
            return createSuccessResponse("Usuarios por tipo obtenidos exitosamente", usuarios);
        } catch (Exception e) {
            return createErrorResponse("Error al obtener usuarios por tipo", e);
        }
    }
    
    public Map<String, Object> actualizarUsuario(UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO usuarioActualizado = usuarioService.update(usuarioDTO);
            return createSuccessResponse("Usuario actualizado exitosamente", usuarioActualizado);
        } catch (Exception e) {
            return createErrorResponse("Error al actualizar usuario", e);
        }
    }
    
    public Map<String, Object> eliminarUsuario(Long id) {
        try {
            boolean eliminado = usuarioService.delete(id);
            if (eliminado) {
                return createSuccessResponse("Usuario eliminado exitosamente", null);
            } else {
                return createErrorResponse("No se pudo eliminar el usuario");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al eliminar usuario", e);
        }
    }
    
    public Map<String, Object> login(String username, String password) {
        try {
            UsuarioDTO usuario = usuarioService.login(username, password);
            if (usuario != null) {
                return createSuccessResponse("Login exitoso", usuario);
            } else {
                return createErrorResponse("Usuario o contraseña incorrectos");
            }
        } catch (Exception e) {
            return createErrorResponse("Error en login", e);
        }
    }
    
    public Map<String, Object> cambiarPassword(Long idUsuario, String currentPassword, String newPassword) {
        try {
            boolean cambiado = usuarioService.changePassword(idUsuario, currentPassword, newPassword);
            if (cambiado) {
                return createSuccessResponse("Password cambiado exitosamente", null);
            } else {
                return createErrorResponse("No se pudo cambiar el password");
            }
        } catch (Exception e) {
            return createErrorResponse("Error al cambiar password", e);
        }
    }
}