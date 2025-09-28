package com.biblioteca.service;

import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.model.Usuario;

public class AuthService {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario autenticar(String username, String password) {
        Usuario user = usuarioDAO.login(username, password);
        if (user != null && "Activo".equalsIgnoreCase(user.getEstado())) {
            return user;
        }
        return null;
    }
}
