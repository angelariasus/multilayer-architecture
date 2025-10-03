package com.biblioteca.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrestamoDTO {
    private int idPrestamo;
    private int idUsuario;
    private int idEjemplar;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    private String devuelto;
}
