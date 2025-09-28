package com.biblioteca.model;
import lombok.Data;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Multa {
    private int idMulta;
    private int idUsuario;
    private double monto;
    private String pagado; 
    private Date fechaMulta;
}