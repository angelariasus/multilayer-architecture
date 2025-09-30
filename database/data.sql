-- ========================
-- INSERTAR USUARIOS
-- ========================
INSERT INTO usuarios (id_usuario, nombre, tipo, estado, username, password)
VALUES (1, 'Ana Torres', 'Alumno', 'Activo', 'ana.torres', '1234');

INSERT INTO usuarios (id_usuario, nombre, tipo, estado, username, password)
VALUES (2, 'Luis García', 'Alumno', 'Activo', 'luis.garcia', '1234');

INSERT INTO usuarios (id_usuario, nombre, tipo, estado, username, password)
VALUES (3, 'María López', 'Docente', 'Activo', 'maria.lopez', 'abcd');

INSERT INTO usuarios (id_usuario, nombre, tipo, estado, username, password)
VALUES (4, 'Carlos Ramos', 'Administrativo', 'Activo', 'carlos.ramos', 'admin');

INSERT INTO usuarios (id_usuario, nombre, tipo, estado, username, password)
VALUES (5, 'Sofía Delgado', 'Alumno', 'Bloqueado', 'sofia.delgado', '5678');

INSERT INTO usuarios (id_usuario, nombre, tipo, estado, username, password)
VALUES (6, 'Pedro Castillo', 'Docente', 'Activo', 'pedro.castillo', 'abcd');

INSERT INTO usuarios (id_usuario, nombre, tipo, estado, username, password)
VALUES (7, 'Laura Fernández', 'Alumno', 'Activo', 'laura.fernandez', 'pass1');

INSERT INTO usuarios (id_usuario, nombre, tipo, estado, username, password)
VALUES (8, 'Diego Medina', 'Alumno', 'Activo', 'diego.medina', 'pass2');

INSERT INTO usuarios (id_usuario, nombre, tipo, estado, username, password)
VALUES (9, 'Carmen Soto', 'Docente', 'Activo', 'carmen.soto', 'doc123');

INSERT INTO usuarios (id_usuario, nombre, tipo, estado, username, password)
VALUES (10, 'Javier Paredes', 'Alumno', 'Activo', 'javier.paredes', 'jp2025');

COMMIT;


-- ========================
-- INSERTAR EJEMPLARES
-- ========================
INSERT INTO ejemplares (id_ejemplar, titulo, autor, editorial, anio, categoria, estado) 
VALUES (1, 'Programación en Java', 'Deitel', 'Pearson', 2021, 'Programación', 'Disponible');

INSERT INTO ejemplares (id_ejemplar, titulo, autor, editorial, anio, categoria, estado) 
VALUES (2, 'Base de Datos: Diseño y Administración', 'Coronel', 'Cengage', 2019, 'Bases de Datos', 'Disponible');

INSERT INTO ejemplares (id_ejemplar, titulo, autor, editorial, anio, categoria, estado) 
VALUES (3, 'Estructuras de Datos en C++', 'Weiss', 'Pearson', 2020, 'Programación', 'Disponible');

INSERT INTO ejemplares (id_ejemplar, titulo, autor, editorial, anio, categoria, estado) 
VALUES (4, 'Sistemas Operativos', 'Silberschatz', 'McGraw-Hill', 2018, 'Sistemas', 'Disponible');

INSERT INTO ejemplares (id_ejemplar, titulo, autor, editorial, anio, categoria, estado) 
VALUES (5, 'Redes de Computadoras', 'Tanenbaum', 'Pearson', 2022, 'Redes', 'Disponible');

INSERT INTO ejemplares (id_ejemplar, titulo, autor, editorial, anio, categoria, estado) 
VALUES (6, 'Ingeniería de Software', 'Pressman', 'McGraw-Hill', 2021, 'Software', 'Disponible');

INSERT INTO ejemplares (id_ejemplar, titulo, autor, editorial, anio, categoria, estado) 
VALUES (7, 'Matemáticas Discretas', 'Rosen', 'McGraw-Hill', 2019, 'Matemáticas', 'Disponible');

INSERT INTO ejemplares (id_ejemplar, titulo, autor, editorial, anio, categoria, estado) 
VALUES (8, 'Algoritmos: Diseño y Análisis', 'Cormen', 'MIT Press', 2020, 'Algoritmos', 'Disponible');

INSERT INTO ejemplares (id_ejemplar, titulo, autor, editorial, anio, categoria, estado) 
VALUES (9, 'Inteligencia Artificial', 'RussellNorvig', 'Pearson', 2021, 'IA', 'Disponible');

INSERT INTO ejemplares (id_ejemplar, titulo, autor, editorial, anio, categoria, estado) 
VALUES (10, 'Machine Learning', 'Goodfellow', 'MIT Press', 2019, 'IA', 'Disponible');

COMMIT;


-- ========================
-- INSERTAR PRÉSTAMOS
-- ========================
INSERT INTO prestamos (id_prestamo, id_usuario, id_ejemplar, fecha_prestamo, fecha_devolucion, devuelto)
VALUES (1, 1, 1, DATE '2025-09-01', DATE '2025-09-08', 'N');

INSERT INTO prestamos (id_prestamo, id_usuario, id_ejemplar, fecha_prestamo, fecha_devolucion, devuelto)
VALUES (2, 2, 3, DATE '2025-08-20', DATE '2025-08-27', 'S');

INSERT INTO prestamos (id_prestamo, id_usuario, id_ejemplar, fecha_prestamo, fecha_devolucion, devuelto)
VALUES (3, 3, 4, DATE '2025-09-10', DATE '2025-09-17', 'N');

INSERT INTO prestamos (id_prestamo, id_usuario, id_ejemplar, fecha_prestamo, fecha_devolucion, devuelto)
VALUES (4, 5, 2, DATE '2025-08-01', DATE '2025-08-10', 'S');

COMMIT;

-- ========================
-- INSERTAR RESERVAS
-- ========================
INSERT INTO reservas (id_reserva, id_usuario, id_ejemplar, fecha_reserva)
VALUES (1, 4, 6, DATE '2025-09-15');

INSERT INTO reservas (id_reserva, id_usuario, id_ejemplar, fecha_reserva)
VALUES (2, 7, 7, DATE '2025-09-18');

INSERT INTO reservas (id_reserva, id_usuario, id_ejemplar, fecha_reserva)
VALUES (3, 8, 9, DATE '2025-09-19');

COMMIT;


-- ========================
-- INSERTAR MULTAS
-- ========================
INSERT INTO multas (id_multa, id_usuario, monto, pagado, fecha_multa)
VALUES (1, 2, 10.50, 'N', DATE '2025-09-05');

INSERT INTO multas (id_multa, id_usuario, monto, pagado, fecha_multa)
VALUES (2, 5, 15.00, 'S', DATE '2025-08-15');

INSERT INTO multas (id_multa, id_usuario, monto, pagado, fecha_multa)
VALUES (3, 1, 5.00, 'N', DATE '2025-09-10');

INSERT INTO multas (id_multa, id_usuario, monto, pagado, fecha_multa)
VALUES (4, 9, 20.00, 'N', DATE '2025-09-20');

COMMIT;