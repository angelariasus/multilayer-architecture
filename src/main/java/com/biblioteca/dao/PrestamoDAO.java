package com.biblioteca.dao;

import com.biblioteca.model.Prestamo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    public void registrar(Prestamo prestamo) {
        String sql = "INSERT INTO prestamos(id_usuario, id_ejemplar, fecha_prestamo, fecha_devolucion, devuelto) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prestamo.getIdUsuario());
            ps.setInt(2, prestamo.getIdEjemplar());
            ps.setDate(3, new java.sql.Date(prestamo.getFechaPrestamo().getTime()));
            ps.setDate(4, new java.sql.Date(prestamo.getFechaDevolucion().getTime()));
            ps.setString(5, prestamo.getDevuelto());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Prestamo> listar() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamos";
        try (Connection conn = Conexion.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Prestamo(
                        rs.getInt("id_prestamo"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_ejemplar"),
                        rs.getDate("fecha_prestamo"),
                        rs.getDate("fecha_devolucion"),
                        rs.getString("devuelto")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void marcarDevuelto(int idPrestamo) {
        String sql = "UPDATE prestamos SET devuelto = 'S' WHERE id_prestamo = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
