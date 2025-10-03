package com.biblioteca.controller;

import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.model.Usuario;
import com.biblioteca.service.UsuarioService;

import java.util.List;

public class UsuarioController {
    private UsuarioService usuarioService = new UsuarioService();

    public void registrarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario(0, usuarioDTO.getNombre(), usuarioDTO.getTipo(), "Activo", usuarioDTO.getUsername(), usuarioDTO.getPassword());
        usuarioService.registrarUsuario(usuario);
        System.out.println("Usuario registrado.");
    }

    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    public Usuario buscarUsuario(int id) {
        return usuarioService.buscarUsuario(id);
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
        System.out.println("🗑 Usuario eliminado.");
    }
}
