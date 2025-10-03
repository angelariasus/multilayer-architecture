package com.biblioteca.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    private Long idUsuario;
    private String nombre;
    private String tipo; // 'Administrativo', 'Bibliotecario', 'Estudiante', 'Docente'
    private String estado; // 'Activo', 'Bloqueado', 'Inactivo'
    private String username;
    private String password;
    private String email;
    private String telefono;
    private String direccion;
    private LocalDate fechaRegistro;
}