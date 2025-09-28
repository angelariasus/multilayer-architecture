package com.biblioteca.model;
import lombok.Data;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {
    private int idReserva;
    private int idUsuario;
    private int idEjemplar;
    private Date fechaReserva;
}