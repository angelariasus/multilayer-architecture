package com.biblioteca.dao;

import com.biblioteca.model.Categoria;
import com.biblioteca.config.DatabaseConfig;
import com.biblioteca.exception.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO implements BaseDAO<Categoria, Long> {
    
    private static final String INSERT_SQL = "INSERT INTO categorias (nombre, descripcion, estado) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM categorias WHERE id_categoria = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM categorias ORDER BY fecha_creacion DESC";
    private static final String UPDATE_SQL = "UPDATE categorias SET nombre = ?, descripcion = ?, estado = ? WHERE id_categoria = ?";
    private static final String DELETE_SQL = "DELETE FROM categorias WHERE id_categoria = ?";
    private static final String SELECT_BY_NOMBRE_SQL = "SELECT * FROM categorias WHERE nombre = ?";
    
    @Override
    public Categoria save(Categoria categoria) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, new String[]{"id_categoria"})) {
            
            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setString(3, categoria.getEstado());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        categoria.setIdCategoria(generatedKeys.getLong(1));
                    }
                }
            }
            return categoria;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar categoría", e);
        }
    }
    
    @Override
    public Categoria findById(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategoria(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar categoría por ID", e);
        }
        return null;
    }
    
    @Override
    public List<Categoria> findAll() {
        List<Categoria> categorias = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categorias.add(mapResultSetToCategoria(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las categorías", e);
        }
        return categorias;
    }
    
    @Override
    public Categoria update(Categoria categoria) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setString(3, categoria.getEstado());
            stmt.setLong(4, categoria.getIdCategoria());
            
            stmt.executeUpdate();
            return categoria;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar categoría", e);
        }
    }
    
    @Override
    public boolean delete(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar categoría", e);
        }
    }
    
    public Categoria findByNombre(String nombre) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_NOMBRE_SQL)) {
            
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategoria(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar categoría por nombre", e);
        }
        return null;
    }
    
    private Categoria mapResultSetToCategoria(ResultSet rs) throws SQLException {
        return Categoria.builder()
                .idCategoria(rs.getLong("id_categoria"))
                .nombre(rs.getString("nombre"))
                .descripcion(rs.getString("descripcion"))
                .estado(rs.getString("estado"))
                .fechaCreacion(rs.getDate("fecha_creacion").toLocalDate())
                .build();
    }
}