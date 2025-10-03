package com.biblioteca.dao;

import com.biblioteca.model.Libro;
import com.biblioteca.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO implements BaseDAO<Libro, Long> {
    
    private static final String INSERT_SQL = "INSERT INTO libros (titulo, autor, isbn, id_categoria, cantidad_disponible, cantidad_total, estado, fecha_publicacion, editorial, descripcion, ubicacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM libros WHERE id_libro = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM libros ORDER BY fecha_registro DESC";
    private static final String UPDATE_SQL = "UPDATE libros SET titulo = ?, autor = ?, isbn = ?, id_categoria = ?, cantidad_disponible = ?, cantidad_total = ?, estado = ?, fecha_publicacion = ?, editorial = ?, descripcion = ?, ubicacion = ? WHERE id_libro = ?";
    private static final String DELETE_SQL = "DELETE FROM libros WHERE id_libro = ?";
    private static final String SELECT_BY_ISBN_SQL = "SELECT * FROM libros WHERE isbn = ?";
    private static final String SELECT_BY_CATEGORIA_SQL = "SELECT * FROM libros WHERE id_categoria = ?";
    private static final String SEARCH_BY_TITLE_OR_AUTHOR_SQL = "SELECT * FROM libros WHERE UPPER(titulo) LIKE UPPER(?) OR UPPER(autor) LIKE UPPER(?)";
    
    @Override
    public Libro save(Libro libro) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, new String[]{"id_libro"})) {
            
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getIsbn());
            stmt.setLong(4, libro.getIdCategoria());
            stmt.setInt(5, libro.getCantidadDisponible());
            stmt.setInt(6, libro.getCantidadTotal());
            stmt.setString(7, libro.getEstado());
            stmt.setDate(8, Date.valueOf(libro.getFechaPublicacion()));
            stmt.setString(9, libro.getEditorial());
            stmt.setString(10, libro.getDescripcion());
            stmt.setString(11, libro.getUbicacion());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        libro.setIdLibro(generatedKeys.getLong(1));
                    }
                }
            }
            return libro;
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar libro", e);
        }
    }
    
    @Override
    public Libro findById(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLibro(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar libro por ID", e);
        }
        return null;
    }
    
    @Override
    public List<Libro> findAll() {
        List<Libro> libros = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                libros.add(mapResultSetToLibro(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los libros", e);
        }
        return libros;
    }
    
    @Override
    public Libro update(Libro libro) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getIsbn());
            stmt.setLong(4, libro.getIdCategoria());
            stmt.setInt(5, libro.getCantidadDisponible());
            stmt.setInt(6, libro.getCantidadTotal());
            stmt.setString(7, libro.getEstado());
            stmt.setDate(8, Date.valueOf(libro.getFechaPublicacion()));
            stmt.setString(9, libro.getEditorial());
            stmt.setString(10, libro.getDescripcion());
            stmt.setString(11, libro.getUbicacion());
            stmt.setLong(12, libro.getIdLibro());
            
            stmt.executeUpdate();
            return libro;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar libro", e);
        }
    }
    
    @Override
    public boolean delete(Long id) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar libro", e);
        }
    }
    
    public Libro findByIsbn(String isbn) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ISBN_SQL)) {
            
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLibro(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar libro por ISBN", e);
        }
        return null;
    }
    
    public List<Libro> findByCategoria(Long idCategoria) {
        List<Libro> libros = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CATEGORIA_SQL)) {
            
            stmt.setLong(1, idCategoria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(mapResultSetToLibro(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar libros por categoría", e);
        }
        return libros;
    }
    
    public List<Libro> searchByTitleOrAuthor(String searchTerm) {
        List<Libro> libros = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_TITLE_OR_AUTHOR_SQL)) {
            
            String pattern = "%" + searchTerm + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(mapResultSetToLibro(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar libros", e);
        }
        return libros;
    }
    
    private Libro mapResultSetToLibro(ResultSet rs) throws SQLException {
        return Libro.builder()
                .idLibro(rs.getLong("id_libro"))
                .titulo(rs.getString("titulo"))
                .autor(rs.getString("autor"))
                .isbn(rs.getString("isbn"))
                .idCategoria(rs.getLong("id_categoria"))
                .cantidadDisponible(rs.getInt("cantidad_disponible"))
                .cantidadTotal(rs.getInt("cantidad_total"))
                .estado(rs.getString("estado"))
                .fechaPublicacion(rs.getDate("fecha_publicacion").toLocalDate())
                .editorial(rs.getString("editorial"))
                .descripcion(rs.getString("descripcion"))
                .ubicacion(rs.getString("ubicacion"))
                .fechaRegistro(rs.getDate("fecha_registro").toLocalDate())
                .build();
    }
}