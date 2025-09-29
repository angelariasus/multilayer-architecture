package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsDAO {

    public static int contarEjemplares() {
        String sql = "SELECT COUNT(*) FROM ejemplares";
        return ejecutarConteo(sql);
    }

    public static int contarUsuariosActivos() {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE estado = 'Activo'";
        return ejecutarConteo(sql);
    }

    public static int contarPrestamosActivos() {
        String sql = "SELECT COUNT(*) FROM prestamos WHERE devuelto = 'No'";
        return ejecutarConteo(sql);
    }

    public static int contarMultasPendientes() {
        String sql = "SELECT COUNT(*) FROM multas WHERE estado = 'Pendiente'";
        return ejecutarConteo(sql);
    }

    private static int ejecutarConteo(String sql) {
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; 
    }
}
