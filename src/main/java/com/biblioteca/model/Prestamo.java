package com.biblioteca.model;
import lombok.Data;
import java.util.Date;

@Data
public class Prestamo {
    private int idPrestamo;
    private int idUsuario;
    private int idEjemplar;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    private String devuelto;
}