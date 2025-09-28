CREATE OR REPLACE TRIGGER trg_prestamo_insert
AFTER INSERT ON prestamos
FOR EACH ROW
BEGIN
    UPDATE ejemplares
    SET estado = 'Prestado'
    WHERE id_ejemplar = :NEW.id_ejemplar;
END;
/

CREATE OR REPLACE TRIGGER trg_prestamo_update
AFTER UPDATE OF devuelto ON prestamos
FOR EACH ROW
WHEN (NEW.devuelto = 'S')
BEGIN
    UPDATE ejemplares
    SET estado = 'Disponible'
    WHERE id_ejemplar = :NEW.id_ejemplar;
END;
/

CREATE OR REPLACE TRIGGER trg_validar_prestamo
BEFORE INSERT ON prestamos
FOR EACH ROW
DECLARE
    v_estado ejemplares.estado%TYPE;
BEGIN
    SELECT estado INTO v_estado
    FROM ejemplares
    WHERE id_ejemplar = :NEW.id_ejemplar;

    IF v_estado <> 'Disponible' THEN
        RAISE_APPLICATION_ERROR(-20001, 'El ejemplar no está disponible para préstamo.');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER trg_generar_multa
AFTER UPDATE OF devuelto ON prestamos
FOR EACH ROW
WHEN (NEW.devuelto = 'S')
DECLARE
    v_dias NUMBER;
    v_monto NUMBER(6,2);
BEGIN
    IF :NEW.fecha_devolucion < SYSDATE THEN
        v_dias := TRUNC(SYSDATE - :NEW.fecha_devolucion);
        v_monto := v_dias * 2; 
        
        INSERT INTO multas (id_usuario, monto, pagado, fecha_multa)
        VALUES (:NEW.id_usuario, v_monto, 'N', SYSDATE);
    END IF;
END;
/

CREATE OR REPLACE FUNCTION fn_monto_multas(p_id_usuario NUMBER)
RETURN NUMBER
IS
    v_total NUMBER(10,2);
BEGIN
    SELECT NVL(SUM(monto), 0)
    INTO v_total
    FROM multas
    WHERE id_usuario = p_id_usuario
      AND pagado = 'N';
    
    RETURN v_total;
END;
/

CREATE OR REPLACE FUNCTION fn_tiene_prestamos_pendientes(p_id_usuario NUMBER)
RETURN CHAR
IS
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM prestamos
    WHERE id_usuario = p_id_usuario
      AND devuelto = 'N';

    IF v_count > 0 THEN
        RETURN 'S';
    ELSE
        RETURN 'N';
    END IF;
END;
/


