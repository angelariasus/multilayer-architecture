package com.sistema.model;
import lombok.Data;
import java.util.Date;

@Data
public class Multa {
    private int idMulta;
    private int idUsuario;
    private double monto;
    private String pagado; 
    private Date fechaMulta;
}