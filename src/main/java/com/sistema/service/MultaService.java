package com.biblioteca.service;

import com.biblioteca.dao.MultaDAO;
import com.biblioteca.model.Multa;

import java.util.List;

public class MultaService {
    private MultaDAO multaDAO = new MultaDAO();

    public void registrarMulta(Multa multa) {
        multaDAO.registrar(multa);
    }

    public List<Multa> listarMultas() {
        return multaDAO.listar();
    }

    public void pagarMulta(int idMulta) {
        multaDAO.marcarPagado(idMulta);
    }
}
