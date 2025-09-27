package com.biblioteca.service;

import com.biblioteca.dao.ReservaDAO;
import com.biblioteca.model.Reserva;

import java.util.List;

public class ReservaService {
    private ReservaDAO reservaDAO = new ReservaDAO();

    public void registrarReserva(Reserva reserva) {
        reservaDAO.registrar(reserva);
    }

    public List<Reserva> listarReservas() {
        return reservaDAO.listar();
    }
}
