package com.biblioteca.dao;

import com.biblioteca.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios(nombre, tipo, estado) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getTipo());
            ps.setString(3, usuario.getEstado());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = Conexion.getConexion();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getString("estado"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = Conexion.getConexion();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getString("estado"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }


    public void actualizarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE usuarios SET estado = ? WHERE id_usuario = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Usuario login(String username, String password) {
        String sql = "SELECT id_usuario, username, password, nombre, tipo, estado FROM usuarios WHERE UPPER(username) = UPPER(?) AND password = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getString("estado"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }
}
