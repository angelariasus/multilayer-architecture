package com.biblioteca.model;
import lombok.Data;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prestamo {
    private int idPrestamo;
    private int idUsuario;
    private int idEjemplar;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    private String devuelto;
}