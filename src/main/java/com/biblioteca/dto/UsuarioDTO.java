package com.biblioteca.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private Long idUsuario;
    private String nombre;
    private String tipo;
    private String estado;
    private String username;
    // password excluido por seguridad
    private String email;
    private String telefono;
    private String direccion;
    private LocalDate fechaRegistro;
    private Integer prestamosActivos; // Campo adicional
    private Integer multasPendientes; // Campo adicional
}