package com.biblioteca.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
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
    
    // Campos adicionales
    private int prestamosActivos;
    private int multasPendientes;
    private boolean tieneSanciones;
}