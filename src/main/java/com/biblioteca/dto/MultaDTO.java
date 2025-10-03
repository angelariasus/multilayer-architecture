package com.biblioteca.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultaDTO {
    private int idMulta;
    private int idUsuario;
    private double monto;
    private String pagado; 
    private Date fechaMulta;
}
