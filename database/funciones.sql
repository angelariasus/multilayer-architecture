-- Función para verificar disponibilidad de libro
CREATE OR REPLACE FUNCTION verificar_disponibilidad_libro(p_id_libro NUMBER)
RETURN NUMBER
IS
    v_disponibles NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_disponibles
    FROM ejemplares
    WHERE id_libro = p_id_libro AND estado = 'Disponible';
    
    RETURN v_disponibles;
END;
/

-- Función para calcular multa por retraso
CREATE OR REPLACE FUNCTION calcular_multa(p_dias_retraso NUMBER)
RETURN NUMBER
IS
    v_monto NUMBER(10,2);
BEGIN
    -- S/. 1.00 por día de retraso
    v_monto := p_dias_retraso * 1.00;
    RETURN v_monto;
END;
/

-- Procedimiento para realizar préstamo
CREATE OR REPLACE PROCEDURE realizar_prestamo(
    p_id_usuario NUMBER,
    p_id_libro NUMBER,
    p_dias_prestamo NUMBER DEFAULT 15
)
IS
    v_id_ejemplar NUMBER;
    v_fecha_devolucion DATE;
    v_disponibles NUMBER;
BEGIN
    -- Verificar disponibilidad
    v_disponibles := verificar_disponibilidad_libro(p_id_libro);
    
    IF v_disponibles = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'No hay ejemplares disponibles');
    END IF;
    
    -- Obtener primer ejemplar disponible
    SELECT id_ejemplar INTO v_id_ejemplar
    FROM ejemplares
    WHERE id_libro = p_id_libro AND estado = 'Disponible'
    AND ROWNUM = 1;
    
    -- Calcular fecha de devolución
    v_fecha_devolucion := SYSDATE + p_dias_prestamo;
    
    -- Insertar préstamo
    INSERT INTO prestamos (id_usuario, id_libro, id_ejemplar, fecha_devolucion_esperada)
    VALUES (p_id_usuario, p_id_libro, v_id_ejemplar, v_fecha_devolucion);
    
    -- Actualizar estado del ejemplar
    UPDATE ejemplares 
    SET estado = 'Prestado'
    WHERE id_ejemplar = v_id_ejemplar;
    
    COMMIT;
END;
/

-- Procedimiento para devolver libro
CREATE OR REPLACE PROCEDURE devolver_libro(p_id_prestamo NUMBER)
IS
    v_id_ejemplar NUMBER;
    v_fecha_devolucion_esperada DATE;
    v_dias_retraso NUMBER;
    v_monto_multa NUMBER(10,2);
BEGIN
    -- Obtener datos del préstamo
    SELECT id_ejemplar, fecha_devolucion_esperada
    INTO v_id_ejemplar, v_fecha_devolucion_esperada
    FROM prestamos
    WHERE id_prestamo = p_id_prestamo AND estado = 'Activo';
    
    -- Actualizar préstamo
    UPDATE prestamos
    SET fecha_devolucion_real = SYSDATE, estado = 'Devuelto'
    WHERE id_prestamo = p_id_prestamo;
    
    -- Actualizar ejemplar
    UPDATE ejemplares
    SET estado = 'Disponible'
    WHERE id_ejemplar = v_id_ejemplar;
    
    -- Verificar si hay retraso y generar multa
    v_dias_retraso := TRUNC(SYSDATE) - TRUNC(v_fecha_devolucion_esperada);
    
    IF v_dias_retraso > 0 THEN
        v_monto_multa := calcular_multa(v_dias_retraso);
        
        INSERT INTO multas (id_prestamo, monto, motivo, dias_retraso)
        VALUES (p_id_prestamo, v_monto_multa, 'Devolución tardía', v_dias_retraso);
    END IF;
    
    COMMIT;
END;
/

-- Procedimiento para actualizar disponibilidad de libros
CREATE OR REPLACE PROCEDURE actualizar_disponibilidad_libros
IS
BEGIN
    UPDATE libros l
    SET cantidad_disponible = (
        SELECT COUNT(*)
        FROM ejemplares e
        WHERE e.id_libro = l.id_libro AND e.estado = 'Disponible'
    );
    
    -- Actualizar estado del libro basado en disponibilidad
    UPDATE libros
    SET estado = CASE
        WHEN cantidad_disponible > 0 THEN 'Disponible'
        ELSE 'Agotado'
    END;
    
    COMMIT;
END;
/