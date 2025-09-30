package com.biblioteca.dto;
import com.biblioteca.dao.PrestamoDAO;
public class PrestamoDTO {
    private PrestamoDAO prestamoDAO = new PrestamoDAO();
    public PrestamoDAO getPrestamoDAO() {
        return prestamoDAO;
    }
    public void setPrestamoDAO(PrestamoDAO prestamoDAO) {
        this.prestamoDAO = prestamoDAO;
    }
}
