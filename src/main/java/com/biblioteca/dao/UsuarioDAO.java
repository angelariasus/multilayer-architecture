package com.biblioteca.dao;

import com.biblioteca.model.Usuario;
import com.biblioteca.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements BaseDAO<Usuario, Long> {
    
    private static final String INSERT_SQL = "INSERT INTO usuarios (nombre, tipo, estado, username, password, email, telefono, direccion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM usuarios WHERE id_usuario = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM usuarios ORDER BY fecha_registro DESC";
    private static final String UPDATE_SQL = "UPDATE usuarios SET nombre = ?, tipo = ?, estado = ?, email = ?, telefono = ?, direccion = ? WHERE id_usuario = ?";
    private static final String DELETE_SQL = "DELETE FROM usuarios WHERE id_usuario = ?";
    private static final String SELECT_BY_USERNAME_SQL = "SELECT * FROM usuarios WHERE username = ?";
    private static final String SELECT_BY_TIPO_SQL = "SELECT * FROM usuarios WHERE tipo = ?";
    
    @Override
    public Usuario save(Usuario usuario) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, new String[]{"id_usuario"})) {
            
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getTipo());
            stmt.setString(3, usuario.getEstado());
            stmt.setString(4, usuario.getUsername());
            stmt.setString(5, usuario.getPassword());
            stmt.setString(6, usuario.getEmail());
            stmt.setString(7, usuario.getTelefono());
            stmt.setString(8, usuario.getDireccion());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setIdUsuario(generatedKeys.getLong(1));
                    }
                }
            }
            return usuario;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar usuario", e);
        }
    }
    
    @Override
    public Usuario findById(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por ID", e);
        }
        return null;
    }
    
    @Override
    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los usuarios", e);
        }
        return usuarios;
    }
    
    @Override
    public Usuario update(Usuario usuario) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getTipo());
            stmt.setString(3, usuario.getEstado());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getTelefono());
            stmt.setString(6, usuario.getDireccion());
            stmt.setLong(7, usuario.getIdUsuario());
            
            stmt.executeUpdate();
            return usuario;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario", e);
        }
    }
    
    @Override
    public boolean delete(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario", e);
        }
    }
    
    public Usuario findByUsername(String username) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_USERNAME_SQL)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por username", e);
        }
        return null;
    }
    
    public List<Usuario> findByTipo(String tipo) {
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_TIPO_SQL)) {
            
            stmt.setString(1, tipo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapResultSetToUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuarios por tipo", e);
        }
        return usuarios;
    }
    
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        return Usuario.builder()
                .idUsuario(rs.getLong("id_usuario"))
                .nombre(rs.getString("nombre"))
                .tipo(rs.getString("tipo"))
                .estado(rs.getString("estado"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .email(rs.getString("email"))
                .telefono(rs.getString("telefono"))
                .direccion(rs.getString("direccion"))
                .fechaRegistro(rs.getDate("fecha_registro").toLocalDate())
                .build();
    }
}