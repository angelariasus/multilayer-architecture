package com.biblioteca.dao;

import com.biblioteca.model.Ejemplar;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EjemplarDAO {

    public void insertar(Ejemplar ejemplar) {
        String sql = "INSERT INTO ejemplares(titulo, autor, editorial, anio, categoria, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ejemplar.getTitulo());
            ps.setString(2, ejemplar.getAutor());
            ps.setString(3, ejemplar.getEditorial());
            ps.setInt(4, ejemplar.getAnio());
            ps.setString(5, ejemplar.getCategoria());
            ps.setString(6, ejemplar.getEstado());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Ejemplar> listar() {
        List<Ejemplar> lista = new ArrayList<>();
        String sql = "SELECT * FROM ejemplares";
        try (Connection conn = Conexion.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Ejemplar(
                        rs.getInt("id_ejemplar"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("editorial"),
                        rs.getInt("anio"),
                        rs.getString("categoria"),
                        rs.getString("estado")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public Ejemplar buscarPorId(int id) {
        String sql = "SELECT * FROM ejemplares WHERE id_ejemplar = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Ejemplar(
                        rs.getInt("id_ejemplar"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("editorial"),
                        rs.getInt("anio"),
                        rs.getString("categoria"),
                        rs.getString("estado"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void actualizarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE ejemplares SET estado = ? WHERE id_ejemplar = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM ejemplares WHERE id_ejemplar = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
