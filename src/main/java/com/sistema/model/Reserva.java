package com.sistema.model;
import lombok.Data;
import java.util.Date;

@Data
public class Reserva {
    private int idReserva;
    private int idUsuario;
    private int idEjemplar;
    private Date fechaReserva;
}