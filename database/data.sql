-- Insertar categorías
INSERT INTO categorias (nombre, descripcion) VALUES 
('Ficción', 'Novelas y cuentos de ficción');
INSERT INTO categorias (nombre, descripcion) VALUES 
('Ciencias', 'Libros de ciencias exactas y naturales');
INSERT INTO categorias (nombre, descripcion) VALUES 
('Historia', 'Libros históricos y biografías');
INSERT INTO categorias (nombre, descripcion) VALUES 
('Tecnología', 'Libros de informática y tecnología');
INSERT INTO categorias (nombre, descripcion) VALUES 
('Literatura Clásica', 'Obras clásicas de la literatura universal');

-- Insertar usuarios
INSERT INTO usuarios (nombre, tipo, username, password, email, telefono) VALUES 
('María González', 'Bibliotecario', 'mgonzalez', 'password123', 'maria.gonzalez@biblioteca.edu', '987654321');
INSERT INTO usuarios (nombre, tipo, username, password, email, telefono) VALUES 
('Juan Pérez', 'Estudiante', 'jperez', 'student123', 'juan.perez@estudiante.edu', '987654322');
INSERT INTO usuarios (nombre, tipo, username, password, email, telefono) VALUES 
('Ana Rodríguez', 'Docente', 'arodriguez', 'teacher123', 'ana.rodriguez@docente.edu', '987654323');
INSERT INTO usuarios (nombre, tipo, username, password, email, telefono) VALUES 
('Carlos López', 'Administrativo', 'clopez', 'admin123', 'carlos.lopez@admin.edu', '987654324');
INSERT INTO usuarios (nombre, tipo, username, password, email, telefono) VALUES 
('Laura Martín', 'Estudiante', 'lmartin', 'student456', 'laura.martin@estudiante.edu', '987654325');

-- Insertar libros
INSERT INTO libros (titulo, autor, isbn, id_categoria, cantidad_total, cantidad_disponible, editorial, fecha_publicacion) VALUES 
('Cien años de soledad', 'Gabriel García Márquez', '978-0060883287', 1, 5, 3, 'Editorial Sudamericana', DATE '1967-06-05');
INSERT INTO libros (titulo, autor, isbn, id_categoria, cantidad_total, cantidad_disponible, editorial, fecha_publicacion) VALUES 
('Fundamentos de Física', 'David Halliday', '978-0471804143', 2, 8, 6, 'Wiley', DATE '2000-01-15');
INSERT INTO libros (titulo, autor, isbn, id_categoria, cantidad_total, cantidad_disponible, editorial, fecha_publicacion) VALUES 
('Historia del Perú', 'Jorge Basadre', '978-9972407109', 3, 4, 2, 'Editorial Universitaria', DATE '1968-12-01');
INSERT INTO libros (titulo, autor, isbn, id_categoria, cantidad_total, cantidad_disponible, editorial, fecha_publicacion) VALUES 
('Java: The Complete Reference', 'Herbert Schildt', '978-0071808558', 4, 6, 4, 'McGraw-Hill', DATE '2014-03-20');
INSERT INTO libros (titulo, autor, isbn, id_categoria, cantidad_total, cantidad_disponible, editorial, fecha_publicacion) VALUES 
('Don Quijote de la Mancha', 'Miguel de Cervantes', '978-8424116392', 5, 3, 1, 'Editorial Espasa', DATE '1605-01-16');

-- Insertar ejemplares
-- Ejemplares para "Cien años de soledad"
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(1, 'LIB001-001', 'Estante A-1', 'Bueno');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(1, 'LIB001-002', 'Estante A-1', 'Excelente');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico, estado) VALUES 
(1, 'LIB001-003', 'Estante A-1', 'Bueno', 'Prestado');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(1, 'LIB001-004', 'Estante A-1', 'Regular');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(1, 'LIB001-005', 'Estante A-1', 'Bueno');

-- Ejemplares para "Fundamentos de Física"
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(2, 'LIB002-001', 'Estante B-2', 'Excelente');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(2, 'LIB002-002', 'Estante B-2', 'Bueno');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico, estado) VALUES 
(2, 'LIB002-003', 'Estante B-2', 'Bueno', 'Prestado');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico, estado) VALUES 
(2, 'LIB002-004', 'Estante B-2', 'Excelente', 'Prestado');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(2, 'LIB002-005', 'Estante B-2', 'Bueno');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(2, 'LIB002-006', 'Estante B-2', 'Regular');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(2, 'LIB002-007', 'Estante B-2', 'Bueno');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(2, 'LIB002-008', 'Estante B-2', 'Excelente');

-- Continuar con los demás libros...
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(3, 'LIB003-001', 'Estante C-3', 'Bueno');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(3, 'LIB003-002', 'Estante C-3', 'Regular');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico, estado) VALUES 
(3, 'LIB003-003', 'Estante C-3', 'Bueno', 'Prestado');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico, estado) VALUES 
(3, 'LIB003-004', 'Estante C-3', 'Excelente', 'Prestado');

INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(4, 'LIB004-001', 'Estante D-4', 'Excelente');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(4, 'LIB004-002', 'Estante D-4', 'Bueno');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(4, 'LIB004-003', 'Estante D-4', 'Bueno');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico, estado) VALUES 
(4, 'LIB004-004', 'Estante D-4', 'Regular', 'Prestado');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(4, 'LIB004-005', 'Estante D-4', 'Excelente');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico, estado) VALUES 
(4, 'LIB004-006', 'Estante D-4', 'Bueno', 'Prestado');

INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico) VALUES 
(5, 'LIB005-001', 'Estante E-5', 'Regular');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico, estado) VALUES 
(5, 'LIB005-002', 'Estante E-5', 'Bueno', 'Prestado');
INSERT INTO ejemplares (id_libro, codigo_ejemplar, ubicacion, estado_fisico, estado) VALUES 
(5, 'LIB005-003', 'Estante E-5', 'Excelente', 'Prestado');

-- Insertar préstamos activos
INSERT INTO prestamos (id_usuario, id_libro, id_ejemplar, fecha_prestamo, fecha_devolucion_esperada) VALUES 
(2, 1, 3, SYSDATE - 5, SYSDATE + 10);
INSERT INTO prestamos (id_usuario, id_libro, id_ejemplar, fecha_prestamo, fecha_devolucion_esperada) VALUES 
(3, 2, 3, SYSDATE - 3, SYSDATE + 12);
INSERT INTO prestamos (id_usuario, id_libro, id_ejemplar, fecha_prestamo, fecha_devolucion_esperada) VALUES 
(5, 2, 4, SYSDATE - 8, SYSDATE + 7);
INSERT INTO prestamos (id_usuario, id_libro, id_ejemplar, fecha_prestamo, fecha_devolucion_esperada) VALUES 
(2, 3, 3, SYSDATE - 20, SYSDATE - 5);
INSERT INTO prestamos (id_usuario, id_libro, id_ejemplar, fecha_prestamo, fecha_devolucion_esperada) VALUES 
(3, 3, 4, SYSDATE - 12, SYSDATE + 3);
INSERT INTO prestamos (id_usuario, id_libro, id_ejemplar, fecha_prestamo, fecha_devolucion_esperada) VALUES 
(5, 4, 4, SYSDATE - 6, SYSDATE + 9);
INSERT INTO prestamos (id_usuario, id_libro, id_ejemplar, fecha_prestamo, fecha_devolucion_esperada) VALUES 
(2, 4, 6, SYSDATE - 2, SYSDATE + 13);
INSERT INTO prestamos (id_usuario, id_libro, id_ejemplar, fecha_prestamo, fecha_devolucion_esperada) VALUES 
(3, 5, 2, SYSDATE - 10, SYSDATE + 5);
INSERT INTO prestamos (id_usuario, id_libro, id_ejemplar, fecha_prestamo, fecha_devolucion_esperada) VALUES 
(5, 5, 3, SYSDATE - 1, SYSDATE + 14);

-- Insertar reservas
INSERT INTO reservas (id_usuario, id_libro, fecha_reserva, fecha_vencimiento) VALUES 
(2, 5, SYSDATE, SYSDATE + 3);
INSERT INTO reservas (id_usuario, id_libro, fecha_reserva, fecha_vencimiento) VALUES 
(3, 1, SYSDATE - 1, SYSDATE + 2);

-- Insertar multas (para préstamos vencidos)
INSERT INTO multas (id_prestamo, monto, motivo, dias_retraso) VALUES 
(4, 5.00, 'Devolución tardía', 5);

-- Actualizar disponibilidad después de las inserciones
EXEC actualizar_disponibilidad_libros;

COMMIT;