package com.biblioteca.service;

import com.biblioteca.dao.EjemplarDAO;
import com.biblioteca.model.Ejemplar;

import java.util.List;

public class EjemplarService {
    private EjemplarDAO ejemplarDAO = new EjemplarDAO();

    public void registrarEjemplar(Ejemplar ejemplar) {
        ejemplarDAO.insertar(ejemplar);
    }

    public List<Ejemplar> listarEjemplares() {
        return ejemplarDAO.listar();
    }

    public Ejemplar buscarEjemplar(int id) {
        return ejemplarDAO.buscarPorId(id);
    }

    public void cambiarEstadoEjemplar(int id, String estado) {
        ejemplarDAO.actualizarEstado(id, estado);
    }

    public void eliminarEjemplar(int id) {
        ejemplarDAO.eliminar(id);
    }
}
