package com.biblioteca.dao;

import com.biblioteca.model.Reserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public void registrar(Reserva reserva) {
        String sql = "INSERT INTO reservas(id_usuario, id_ejemplar, fecha_reserva) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reserva.getIdUsuario());
            ps.setInt(2, reserva.getIdEjemplar());
            ps.setDate(3, new java.sql.Date(reserva.getFechaReserva().getTime()));
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Reserva> listar() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas";
        try (Connection conn = Conexion.getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Reserva(
                        rs.getInt("id_reserva"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_ejemplar"),
                        rs.getDate("fecha_reserva")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }
}
