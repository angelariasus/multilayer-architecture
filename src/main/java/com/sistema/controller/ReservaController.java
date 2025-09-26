package com.biblioteca.controller;

import com.biblioteca.model.Reserva;
import com.biblioteca.service.ReservaService;

import java.util.Date;
import java.util.List;

public class ReservaController {
    private ReservaService reservaService = new ReservaService();

    public void registrarReserva(int idUsuario, int idEjemplar) {
        Reserva r = new Reserva(0, idUsuario, idEjemplar, new Date());
        reservaService.registrarReserva(r);
        System.out.println("Reserva registrada.");
    }

    public List<Reserva> listarReservas() {
        return reservaService.listarReservas();
    }
}
