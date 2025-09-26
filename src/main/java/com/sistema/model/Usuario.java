package com.sistema.model;
import lombok.Data;

@Data
public class Usuario {
    private int idUsuario;
    private String nombre;
    private String tipo;
    private String estado; 
}
