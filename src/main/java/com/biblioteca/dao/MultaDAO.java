package com.biblioteca.dao;

import com.biblioteca.model.Multa;
import com.biblioteca.config.DatabaseConfig;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MultaDAO implements BaseDAO<Multa, Long> {
    
    private static final String INSERT_SQL = "INSERT INTO multas (id_prestamo, monto, fecha_generacion, estado, motivo, dias_retraso) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM multas WHERE id_multa = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM multas ORDER BY fecha_generacion DESC";
    private static final String UPDATE_SQL = "UPDATE multas SET fecha_pago = ?, estado = ? WHERE id_multa = ?";
    private static final String DELETE_SQL = "DELETE FROM multas WHERE id_multa = ?";
    private static final String SELECT_BY_PRESTAMO_SQL = "SELECT * FROM multas WHERE id_prestamo = ?";
    private static final String SELECT_PENDIENTES_SQL = "SELECT * FROM multas WHERE estado = 'Pendiente'";
    private static final String SELECT_BY_USUARIO_SQL = "SELECT m.* FROM multas m INNER JOIN prestamos p ON m.id_prestamo = p.id_prestamo WHERE p.id_usuario = ?";
    
    @Override
    public Multa save(Multa multa) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, new String[]{"id_multa"})) {
            
            stmt.setLong(1, multa.getIdPrestamo());
            stmt.setBigDecimal(2, multa.getMonto());
            stmt.setDate(3, Date.valueOf(multa.getFechaGeneracion()));
            stmt.setString(4, multa.getEstado());
            stmt.setString(5, multa.getMotivo());
            stmt.setInt(6, multa.getDiasRetraso());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        multa.setIdMulta(generatedKeys.getLong(1));
                    }
                }
            }
            return multa;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar multa", e);
        }
    }
    
    @Override
    public Multa findById(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMulta(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar multa por ID", e);
        }
        return null;
    }
    
    @Override
    public List<Multa> findAll() {
        List<Multa> multas = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                multas.add(mapResultSetToMulta(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las multas", e);
        }
        return multas;
    }
    
    @Override
    public Multa update(Multa multa) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            if (multa.getFechaPago() != null) {
                stmt.setDate(1, Date.valueOf(multa.getFechaPago()));
            } else {
                stmt.setNull(1, Types.DATE);
            }
            stmt.setString(2, multa.getEstado());
            stmt.setLong(3, multa.getIdMulta());
            
            stmt.executeUpdate();
            return multa;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar multa", e);
        }
    }
    
    @Override
    public boolean delete(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar multa", e);
        }
    }
    
    public List<Multa> findByPrestamo(Long idPrestamo) {
        List<Multa> multas = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_PRESTAMO_SQL)) {
            
            stmt.setLong(1, idPrestamo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    multas.add(mapResultSetToMulta(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar multas por préstamo", e);
        }
        return multas;
    }
    
    public List<Multa> findPendientes() {
        List<Multa> multas = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_PENDIENTES_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                multas.add(mapResultSetToMulta(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar multas pendientes", e);
        }
        return multas;
    }
    
    public List<Multa> findByUsuario(Long idUsuario) {
        List<Multa> multas = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_USUARIO_SQL)) {
            
            stmt.setLong(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    multas.add(mapResultSetToMulta(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar multas por usuario", e);
        }
        return multas;
    }
    
    private Multa mapResultSetToMulta(ResultSet rs) throws SQLException {
        return Multa.builder()
                .idMulta(rs.getLong("id_multa"))
                .idPrestamo(rs.getLong("id_prestamo"))
                .monto(rs.getBigDecimal("monto"))
                .fechaGeneracion(rs.getDate("fecha_generacion").toLocalDate())
                .fechaPago(rs.getDate("fecha_pago") != null ? 
                    rs.getDate("fecha_pago").toLocalDate() : null)
                .estado(rs.getString("estado"))
                .motivo(rs.getString("motivo"))
                .diasRetraso(rs.getInt("dias_retraso"))
                .build();
    }
}