package com.biblioteca.controller;

import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.model.Usuario;
import com.biblioteca.service.UsuarioService;

import java.util.ArrayList;
import java.util.List;

public class UsuarioController {
    private UsuarioService usuarioService = new UsuarioService();

    public void registrarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = convertirDTOaEntity(usuarioDTO);
        usuarioService.registrarUsuario(usuario);
        System.out.println("Usuario registrado.");
    }

    public List<UsuarioDTO> listarUsuariosDTO() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        List<UsuarioDTO> usuariosDTO = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            usuariosDTO.add(convertirEntityaDTO(usuario));
        }
        return usuariosDTO;
    }

    public void actualizarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = convertirDTOaEntity(usuarioDTO);
        usuarioService.actualizarUsuario(usuario);
        System.out.println("Usuario actualizado.");
    }

    public UsuarioDTO buscarUsuario(int id) {
        Usuario usuario = usuarioService.buscarUsuario(id);
        return usuario != null ? convertirEntityaDTO(usuario) : null;
    }

    private Usuario convertirDTOaEntity(UsuarioDTO dto) {
        return new Usuario(
            dto.getIdUsuario(),
            dto.getNombre(),
            dto.getTipo(),
            dto.getEstado() != null ? dto.getEstado() : "Activo",
            dto.getUsername(),
            dto.getPassword()
        );
    }

    private UsuarioDTO convertirEntityaDTO(Usuario usuario) {
        return new UsuarioDTO(
            usuario.getIdUsuario(),
            usuario.getNombre(),
            usuario.getTipo(),
            usuario.getEstado(),
            usuario.getUsername(),
            "****" 
        );
    }

    public void bloquearUsuario(int id) {
        usuarioService.bloquearUsuario(id);
        System.out.println("Usuario bloqueado.");
    }

    public void activarUsuario(int id) {
        usuarioService.activarUsuario(id);
        System.out.println("Usuario activado.");
    }

    public void eliminarUsuario(int id) {
        usuarioService.eliminarUsuario(id);
        System.out.println("Usuario eliminado.");
    }
}