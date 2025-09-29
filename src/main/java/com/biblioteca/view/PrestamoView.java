package com.biblioteca.view;

import com.biblioteca.model.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrestamoView extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Prestamo> prestamos;

    public PrestamoView() {
        prestamos = new ArrayList<>();

        setTitle("Gestión de Préstamos");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Tabla
        tableModel = new DefaultTableModel(new Object[]{"ID", "Usuario", "Ejemplar", "Fecha Préstamo", "Fecha Devolución", "Devuelto"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Botones
        JButton btnAgregar = new JButton("Agregar");
        JButton btnDevolver = new JButton("Marcar Devuelto");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCargar = new JButton("Cargar"); // 🔹 Nuevo botón

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnDevolver);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCargar);

        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Acción Agregar
        btnAgregar.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog(this, "ID Préstamo:");
                String idUsuarioStr = JOptionPane.showInputDialog(this, "ID Usuario:");
                String idEjemplarStr = JOptionPane.showInputDialog(this, "ID Ejemplar:");
                String fechaPrestamoStr = JOptionPane.showInputDialog(this, "Fecha Préstamo (yyyy-MM-dd):");
                String fechaDevolucionStr = JOptionPane.showInputDialog(this, "Fecha Devolución (yyyy-MM-dd):");

                int id = Integer.parseInt(idStr);
                int idUsuario = Integer.parseInt(idUsuarioStr);
                int idEjemplar = Integer.parseInt(idEjemplarStr);

                // Conversión de String → Date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaPrestamo = sdf.parse(fechaPrestamoStr);
                Date fechaDevolucion = sdf.parse(fechaDevolucionStr);

                Prestamo prestamo = new Prestamo(id, idUsuario, idEjemplar, fechaPrestamo, fechaDevolucion, "No");
                prestamos.add(prestamo);

                tableModel.addRow(new Object[]{
                        prestamo.getIdPrestamo(),
                        prestamo.getIdUsuario(),
                        prestamo.getIdEjemplar(),
                        sdf.format(prestamo.getFechaPrestamo()), // lo mostramos como String
                        sdf.format(prestamo.getFechaDevolucion()),
                        prestamo.getDevuelto()
                });

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error en ID: deben ser números.");
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use yyyy-MM-dd.");
            }
        });

        // Acción Marcar Devuelto
        btnDevolver.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                prestamos.get(row).setDevuelto("Sí");
                tableModel.setValueAt("Sí", row, 5);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un préstamo.");
            }
        });

        // Acción Eliminar
        btnEliminar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                prestamos.remove(row);
                tableModel.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un préstamo.");
            }
        });

        // Acción Cargar (🔹 datos de ejemplo por ahora)
        btnCargar.addActionListener(e -> {
            prestamos.clear();
            tableModel.setRowCount(0); // limpia la tabla

            // Simulación de carga
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                prestamos.add(new Prestamo(1, 101, 201, sdf.parse("2025-09-01"), sdf.parse("2025-09-15"), "No"));
                prestamos.add(new Prestamo(2, 102, 202, sdf.parse("2025-09-02"), sdf.parse("2025-09-16"), "Sí"));
                prestamos.add(new Prestamo(3, 103, 203, sdf.parse("2025-09-03"), sdf.parse("2025-09-17"), "No"));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            // Insertar en la tabla
            for (Prestamo p : prestamos) {
                tableModel.addRow(new Object[]{
                        p.getIdPrestamo(),
                        p.getIdUsuario(),
                        p.getIdEjemplar(),
                        sdf.format(p.getFechaPrestamo()),
                        sdf.format(p.getFechaDevolucion()),
                        p.getDevuelto()
                });
            }

            JOptionPane.showMessageDialog(this, "Préstamos cargados correctamente");
        });
    }
}
