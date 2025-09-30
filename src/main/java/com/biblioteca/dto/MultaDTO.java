package com.biblioteca.dto;
import com.biblioteca.dao.MultaDAO;
public class MultaDTO {
    private MultaDAO multaDAO = new MultaDAO();
    public MultaDAO getMultaDAO() {
        return multaDAO;
    }
    public void setMultaDAO(MultaDAO multaDAO) {
        this.multaDAO = multaDAO;
    }
}
