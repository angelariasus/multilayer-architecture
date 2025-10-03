package com.biblioteca.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {
    private int idReserva;
    private int idUsuario;
    private int idEjemplar;
    private Date fechaReserva;
}
