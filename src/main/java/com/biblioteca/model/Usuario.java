package com.biblioteca.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private int idUsuario;
    private String nombre;
    private String tipo; 
    private String estado; 
    private String username;
    private String password;
    private String email;
    private String telefono;
    private String direccion;
    private String fechaRegistro;
}