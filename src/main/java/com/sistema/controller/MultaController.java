package com.biblioteca.controller;

import com.biblioteca.model.Multa;
import com.biblioteca.service.MultaService;

import java.util.Date;
import java.util.List;

public class MultaController {
    private MultaService multaService = new MultaService();

    public void registrarMulta(int idUsuario, double monto) {
        Multa m = new Multa(0, idUsuario, monto, "N", new Date());
        multaService.registrarMulta(m);
        System.out.println("Multa registrada.");
    }

    public List<Multa> listarMultas() {
        return multaService.listarMultas();
    }

    public void pagarMulta(int idMulta) {
        multaService.pagarMulta(idMulta);
        System.out.println("Multa pagada.");
    }
}
