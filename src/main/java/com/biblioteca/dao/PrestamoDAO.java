package com.biblioteca.dao;

import com.biblioteca.model.Prestamo;
import com.biblioteca.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO implements BaseDAO<Prestamo, Long> {
    
    private static final String INSERT_SQL = "INSERT INTO prestamos (id_usuario, id_libro, id_ejemplar, fecha_prestamo, fecha_devolucion_esperada, estado, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM prestamos WHERE id_prestamo = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM prestamos ORDER BY fecha_prestamo DESC";
    private static final String UPDATE_SQL = "UPDATE prestamos SET fecha_devolucion_real = ?, estado = ?, observaciones = ? WHERE id_prestamo = ?";
    private static final String DELETE_SQL = "DELETE FROM prestamos WHERE id_prestamo = ?";
    private static final String SELECT_BY_USUARIO_SQL = "SELECT * FROM prestamos WHERE id_usuario = ?";
    private static final String SELECT_ACTIVOS_BY_USUARIO_SQL = "SELECT * FROM prestamos WHERE id_usuario = ? AND estado = 'Activo'";
    private static final String SELECT_VENCIDOS_SQL = "SELECT * FROM prestamos WHERE fecha_devolucion_esperada < SYSDATE AND estado = 'Activo'";
    
    @Override
    public Prestamo save(Prestamo prestamo) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, new String[]{"id_prestamo"})) {
            
            stmt.setLong(1, prestamo.getIdUsuario());
            stmt.setLong(2, prestamo.getIdLibro());
            stmt.setLong(3, prestamo.getIdEjemplar());
            stmt.setDate(4, Date.valueOf(prestamo.getFechaPrestamo()));
            stmt.setDate(5, Date.valueOf(prestamo.getFechaDevolucionEsperada()));
            stmt.setString(6, prestamo.getEstado());
            stmt.setString(7, prestamo.getObservaciones());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        prestamo.setIdPrestamo(generatedKeys.getLong(1));
                    }
                }
            }
            return prestamo;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar préstamo", e);
        }
    }
    
    @Override
    public Prestamo findById(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPrestamo(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar préstamo por ID", e);
        }
        return null;
    }
    
    @Override
    public List<Prestamo> findAll() {
        List<Prestamo> prestamos = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                prestamos.add(mapResultSetToPrestamo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los préstamos", e);
        }
        return prestamos;
    }
    
    @Override
    public Prestamo update(Prestamo prestamo) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            if (prestamo.getFechaDevolucionReal() != null) {
                stmt.setDate(1, Date.valueOf(prestamo.getFechaDevolucionReal()));
            } else {
                stmt.setNull(1, Types.DATE);
            }
            stmt.setString(2, prestamo.getEstado());
            stmt.setString(3, prestamo.getObservaciones());
            stmt.setLong(4, prestamo.getIdPrestamo());
            
            stmt.executeUpdate();
            return prestamo;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar préstamo", e);
        }
    }
    
    @Override
    public boolean delete(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar préstamo", e);
        }
    }
    
    public List<Prestamo> findByUsuario(Long idUsuario) {
        List<Prestamo> prestamos = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_USUARIO_SQL)) {
            
            stmt.setLong(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    prestamos.add(mapResultSetToPrestamo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar préstamos por usuario", e);
        }
        return prestamos;
    }
    
    public List<Prestamo> findActivosByUsuario(Long idUsuario) {
        List<Prestamo> prestamos = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ACTIVOS_BY_USUARIO_SQL)) {
            
            stmt.setLong(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    prestamos.add(mapResultSetToPrestamo(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar préstamos activos por usuario", e);
        }
        return prestamos;
    }
    
    public List<Prestamo> findVencidos() {
        List<Prestamo> prestamos = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_VENCIDOS_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                prestamos.add(mapResultSetToPrestamo(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar préstamos vencidos", e);
        }
        return prestamos;
    }
    
    private Prestamo mapResultSetToPrestamo(ResultSet rs) throws SQLException {
        return Prestamo.builder()
                .idPrestamo(rs.getLong("id_prestamo"))
                .idUsuario(rs.getLong("id_usuario"))
                .idLibro(rs.getLong("id_libro"))
                .idEjemplar(rs.getLong("id_ejemplar"))
                .fechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate())
                .fechaDevolucionEsperada(rs.getDate("fecha_devolucion_esperada").toLocalDate())
                .fechaDevolucionReal(rs.getDate("fecha_devolucion_real") != null ? 
                    rs.getDate("fecha_devolucion_real").toLocalDate() : null)
                .estado(rs.getString("estado"))
                .observaciones(rs.getString("observaciones"))
                .build();
    }
}