package com.biblioteca.dao;

import com.biblioteca.model.Ejemplar;
import com.biblioteca.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EjemplarDAO implements BaseDAO<Ejemplar, Long> {
    
    private static final String INSERT_SQL = "INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado, fecha_adquisicion, observaciones, estado_fisico) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM ejemplares WHERE id_ejemplar = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM ejemplares ORDER BY fecha_adquisicion DESC";
    private static final String UPDATE_SQL = "UPDATE ejemplares SET ubicacion = ?, estado = ?, observaciones = ?, estado_fisico = ? WHERE id_ejemplar = ?";
    private static final String DELETE_SQL = "DELETE FROM ejemplares WHERE id_ejemplar = ?";
    private static final String SELECT_BY_LIBRO_SQL = "SELECT * FROM ejemplares WHERE id_libro = ?";
    private static final String SELECT_BY_CODIGO_SQL = "SELECT * FROM ejemplares WHERE codigo_ejemplar = ?";
    private static final String SELECT_DISPONIBLES_BY_LIBRO_SQL = "SELECT * FROM ejemplares WHERE id_libro = ? AND estado = 'Disponible'";
    
    @Override
    public Ejemplar save(Ejemplar ejemplar) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, new String[]{"id_ejemplar"})) {
            
            stmt.setLong(1, ejemplar.getIdLibro());
            stmt.setString(2, ejemplar.getCodigoEjemplar());
            stmt.setString(3, ejemplar.getUbicacion());
            stmt.setString(4, ejemplar.getEstado());
            stmt.setDate(5, Date.valueOf(ejemplar.getFechaAdquisicion()));
            stmt.setString(6, ejemplar.getObservaciones());
            stmt.setString(7, ejemplar.getEstadoFisico());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ejemplar.setIdEjemplar(generatedKeys.getLong(1));
                    }
                }
            }
            return ejemplar;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar ejemplar", e);
        }
    }
    
    @Override
    public Ejemplar findById(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEjemplar(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ejemplar por ID", e);
        }
        return null;
    }
    
    @Override
    public List<Ejemplar> findAll() {
        List<Ejemplar> ejemplares = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ejemplares.add(mapResultSetToEjemplar(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los ejemplares", e);
        }
        return ejemplares;
    }
    
    @Override
    public Ejemplar update(Ejemplar ejemplar) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            stmt.setString(1, ejemplar.getUbicacion());
            stmt.setString(2, ejemplar.getEstado());
            stmt.setString(3, ejemplar.getObservaciones());
            stmt.setString(4, ejemplar.getEstadoFisico());
            stmt.setLong(5, ejemplar.getIdEjemplar());
            
            stmt.executeUpdate();
            return ejemplar;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar ejemplar", e);
        }
    }
    
    @Override
    public boolean delete(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar ejemplar", e);
        }
    }
    
    public List<Ejemplar> findByLibro(Long idLibro) {
        List<Ejemplar> ejemplares = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_LIBRO_SQL)) {
            
            stmt.setLong(1, idLibro);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ejemplares.add(mapResultSetToEjemplar(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ejemplares por libro", e);
        }
        return ejemplares;
    }
    
    public Ejemplar findByCodigo(String codigo) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CODIGO_SQL)) {
            
            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEjemplar(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ejemplar por código", e);
        }
        return null;
    }
    
    public List<Ejemplar> findDisponiblesByLibro(Long idLibro) {
        List<Ejemplar> ejemplares = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_DISPONIBLES_BY_LIBRO_SQL)) {
            
            stmt.setLong(1, idLibro);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ejemplares.add(mapResultSetToEjemplar(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar ejemplares disponibles", e);
        }
        return ejemplares;
    }
    
    private Ejemplar mapResultSetToEjemplar(ResultSet rs) throws SQLException {
        return Ejemplar.builder()
                .idEjemplar(rs.getLong("id_ejemplar"))
                .idLibro(rs.getLong("id_libro"))
                .codigoEjemplar(rs.getString("codigo_ejemplar"))
                .ubicacion(rs.getString("ubicacion"))
                .estado(rs.getString("estado"))
                .fechaAdquisicion(rs.getDate("fecha_adquisicion").toLocalDate())
                .observaciones(rs.getString("observaciones"))
                .estadoFisico(rs.getString("estado_fisico"))
                .build();
    }
}