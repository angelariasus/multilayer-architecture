package com.biblioteca.controller;

import com.biblioteca.model.Usuario;
import com.biblioteca.service.AuthService;

public class AuthController {
    private AuthService authService = new AuthService();

    public Usuario login(String username, String password) {
        return authService.autenticar(username, password);
    }
}
