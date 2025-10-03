package com.biblioteca.service;
import com.biblioteca.dao.EjemplarDAO;
import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.model.Ejemplar;
import com.biblioteca.model.Prestamo;
import java.util.List;
public class PrestamoService {
    private PrestamoDAO prestamoDAO = new PrestamoDAO();
    private EjemplarDAO ejemplarDAO = new EjemplarDAO();
    public boolean prestarEjemplar(Prestamo prestamo) {
        Ejemplar ejemplar = ejemplarDAO.buscarPorId(prestamo.getIdEjemplar());
        if (ejemplar == null) {
            System.out.println("El ejemplar no existe.");
            return false;
        }
        if (!"Disponible".equalsIgnoreCase(ejemplar.getEstado())) {
            System.out.println("El ejemplar no está disponible.");
            return false;
        }
        prestamoDAO.registrar(prestamo);
        ejemplarDAO.actualizarEstado(prestamo.getIdEjemplar(), "Prestado");

        System.out.println("Préstamo registrado con éxito.");
        return true;
    }
    public void devolverEjemplar(int idPrestamo, int idEjemplar) {
        prestamoDAO.marcarDevuelto(idPrestamo);
        ejemplarDAO.actualizarEstado(idEjemplar, "Disponible");
        System.out.println("Ejemplar devuelto correctamente.");
    }
    public List<Prestamo> listarPrestamos() {
        return prestamoDAO.listar();
    }
}

