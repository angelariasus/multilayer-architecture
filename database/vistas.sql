-- Vista de libros con información completa
CREATE OR REPLACE VIEW vista_libros_completa AS
SELECT 
    l.id_libro,
    l.titulo,
    l.autor,
    l.isbn,
    c.nombre AS categoria,
    l.cantidad_disponible,
    l.cantidad_total,
    l.estado,
    l.editorial,
    l.fecha_publicacion,
    l.ubicacion
FROM libros l
LEFT JOIN categorias c ON l.id_categoria = c.id_categoria;

-- Vista de préstamos activos con información del usuario y libro
CREATE OR REPLACE VIEW vista_prestamos_activos AS
SELECT 
    p.id_prestamo,
    u.nombre AS usuario,
    u.tipo AS tipo_usuario,
    l.titulo AS libro,
    l.autor,
    p.fecha_prestamo,
    p.fecha_devolucion_esperada,
    CASE 
        WHEN p.fecha_devolucion_esperada < SYSDATE THEN 'VENCIDO'
        ELSE 'VIGENTE'
    END AS estado_devolucion,
    GREATEST(0, TRUNC(SYSDATE - p.fecha_devolucion_esperada)) AS dias_retraso
FROM prestamos p
JOIN usuarios u ON p.id_usuario = u.id_usuario
JOIN libros l ON p.id_libro = l.id_libro
WHERE p.estado = 'Activo';

-- Vista de multas pendientes
CREATE OR REPLACE VIEW vista_multas_pendientes AS
SELECT 
    m.id_multa,
    u.nombre AS usuario,
    l.titulo AS libro,
    m.monto,
    m.fecha_generacion,
    m.motivo,
    m.dias_retraso
FROM multas m
JOIN prestamos p ON m.id_prestamo = p.id_prestamo
JOIN usuarios u ON p.id_usuario = u.id_usuario
JOIN libros l ON p.id_libro = l.id_libro
WHERE m.estado = 'Pendiente';

-- Vista de estadísticas de biblioteca
CREATE OR REPLACE VIEW vista_estadisticas AS
SELECT 
    (SELECT COUNT(*) FROM libros) AS total_libros,
    (SELECT COUNT(*) FROM ejemplares WHERE estado = 'Disponible') AS ejemplares_disponibles,
    (SELECT COUNT(*) FROM prestamos WHERE estado = 'Activo') AS prestamos_activos,
    (SELECT COUNT(*) FROM reservas WHERE estado = 'Activa') AS reservas_activas,
    (SELECT COUNT(*) FROM multas WHERE estado = 'Pendiente') AS multas_pendientes,
    (SELECT SUM(monto) FROM multas WHERE estado = 'Pendiente') AS monto_multas_pendientes
FROM DUAL;