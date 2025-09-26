package com.biblioteca.dao;

import com.biblioteca.model.Multa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MultaDAO {

    public void registrar(Multa multa) {
        String sql = "INSERT INTO multas(id_usuario, monto, pagado, fecha_multa) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, multa.getIdUsuario());
            ps.setDouble(2, multa.getMonto());
            ps.setString(3, multa.getPagado());
            ps.setDate(4, new java.sql.Date(multa.getFechaMulta().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Multa> listar() {
        List<Multa> lista = new ArrayList<>();
        String sql = "SELECT * FROM multas";
        try (Connection conn = Conexion.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Multa(
                        rs.getInt("id_multa"),
                        rs.getInt("id_usuario"),
                        rs.getDouble("monto"),
                        rs.getString("pagado"),
                        rs.getDate("fecha_multa")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public void marcarPagado(int idMulta) {
        String sql = "UPDATE multas SET pagado = 'S' WHERE id_multa = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMulta);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
