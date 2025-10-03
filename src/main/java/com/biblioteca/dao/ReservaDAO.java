package com.biblioteca.dao;

import com.biblioteca.model.Reserva;
import com.biblioteca.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO implements BaseDAO<Reserva, Long> {
    
    private static final String INSERT_SQL = "INSERT INTO reservas (id_usuario, id_libro, fecha_reserva, fecha_vencimiento, estado, posicion_cola, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM reservas WHERE id_reserva = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM reservas ORDER BY fecha_reserva DESC";
    private static final String UPDATE_SQL = "UPDATE reservas SET fecha_recogida = ?, estado = ?, observaciones = ? WHERE id_reserva = ?";
    private static final String DELETE_SQL = "DELETE FROM reservas WHERE id_reserva = ?";
    private static final String SELECT_BY_USUARIO_SQL = "SELECT * FROM reservas WHERE id_usuario = ?";
    private static final String SELECT_ACTIVAS_BY_LIBRO_SQL = "SELECT * FROM reservas WHERE id_libro = ? AND estado = 'Activa' ORDER BY posicion_cola";
    private static final String SELECT_VENCIDAS_SQL = "SELECT * FROM reservas WHERE fecha_vencimiento < SYSDATE AND estado = 'Activa'";
    
    @Override
    public Reserva save(Reserva reserva) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, new String[]{"id_reserva"})) {
            
            stmt.setLong(1, reserva.getIdUsuario());
            stmt.setLong(2, reserva.getIdLibro());
            stmt.setDate(3, Date.valueOf(reserva.getFechaReserva()));
            stmt.setDate(4, Date.valueOf(reserva.getFechaVencimiento()));
            stmt.setString(5, reserva.getEstado());
            stmt.setInt(6, reserva.getPosicionCola());
            stmt.setString(7, reserva.getObservaciones());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        reserva.setIdReserva(generatedKeys.getLong(1));
                    }
                }
            }
            return reserva;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar reserva", e);
        }
    }
    
    @Override
    public Reserva findById(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReserva(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar reserva por ID", e);
        }
        return null;
    }
    
    @Override
    public List<Reserva> findAll() {
        List<Reserva> reservas = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                reservas.add(mapResultSetToReserva(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las reservas", e);
        }
        return reservas;
    }
    
    @Override
    public Reserva update(Reserva reserva) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            if (reserva.getFechaRecogida() != null) {
                stmt.setDate(1, Date.valueOf(reserva.getFechaRecogida()));
            } else {
                stmt.setNull(1, Types.DATE);
            }
            stmt.setString(2, reserva.getEstado());
            stmt.setString(3, reserva.getObservaciones());
            stmt.setLong(4, reserva.getIdReserva());
            
            stmt.executeUpdate();
            return reserva;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar reserva", e);
        }
    }
    
    @Override
    public boolean delete(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar reserva", e);
        }
    }
    
    public List<Reserva> findByUsuario(Long idUsuario) {
        List<Reserva> reservas = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_USUARIO_SQL)) {
            
            stmt.setLong(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapResultSetToReserva(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar reservas por usuario", e);
        }
        return reservas;
    }
    
    public List<Reserva> findActivasByLibro(Long idLibro) {
        List<Reserva> reservas = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ACTIVAS_BY_LIBRO_SQL)) {
            
            stmt.setLong(1, idLibro);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapResultSetToReserva(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar reservas activas por libro", e);
        }
        return reservas;
    }
    
    public List<Reserva> findVencidas() {
        List<Reserva> reservas = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_VENCIDAS_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                reservas.add(mapResultSetToReserva(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar reservas vencidas", e);
        }
        return reservas;
    }
    
    private Reserva mapResultSetToReserva(ResultSet rs) throws SQLException {
        return Reserva.builder()
                .idReserva(rs.getLong("id_reserva"))
                .idUsuario(rs.getLong("id_usuario"))
                .idLibro(rs.getLong("id_libro"))
                .fechaReserva(rs.getDate("fecha_reserva").toLocalDate())
                .fechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate())
                .fechaRecogida(rs.getDate("fecha_recogida") != null ? 
                    rs.getDate("fecha_recogida").toLocalDate() : null)
                .estado(rs.getString("estado"))
                .posicionCola(rs.getInt("posicion_cola"))
                .observaciones(rs.getString("observaciones"))
                .build();
    }
}