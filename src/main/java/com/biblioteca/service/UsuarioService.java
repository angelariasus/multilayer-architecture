package com.biblioteca.service;

import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.model.Usuario;

import java.util.List;

public class UsuarioService {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void registrarUsuario(Usuario usuario) {
        usuarioDAO.insertar(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listar();
    }

    public Usuario buscarUsuario(int id) {
        return usuarioDAO.buscarPorId(id);
    }

    public void actualizarUsuario(Usuario usuario) {
        usuarioDAO.actualizarUsuario(usuario);
    }

    public void bloquearUsuario(int id) {
        usuarioDAO.actualizarEstado(id, "Bloqueado");
    }

    public void activarUsuario(int id) {
        usuarioDAO.actualizarEstado(id, "Activo");
    }

    public void eliminarUsuario(int id) {
        usuarioDAO.eliminar(id);
    }
}
