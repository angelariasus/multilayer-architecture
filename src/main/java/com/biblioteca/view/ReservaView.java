package com.biblioteca.view;

import com.biblioteca.model.Reserva;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservaView extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Reserva> reservas;

    public ReservaView() {
        reservas = new ArrayList<>();

        setTitle("Gestión de Reservas");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID Reserva", "ID Usuario", "ID Ejemplar", "Fecha Reserva"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Agregar");
        JButton refreshButton = new JButton("Refrescar");

        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Agregar reserva
        addButton.addActionListener(e -> {
            int idReserva = reservas.size() + 1;
            int idUsuario = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID Usuario:"));
            int idEjemplar = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID Ejemplar:"));

            Reserva reserva = new Reserva(idReserva, idUsuario, idEjemplar, new Date());
            reservas.add(reserva);
            refreshTable();
        });

        // Refrescar tabla
        refreshButton.addActionListener(e -> refreshTable());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Reserva reserva : reservas) {
            tableModel.addRow(new Object[]{
                    reserva.getIdReserva(),
                    reserva.getIdUsuario(),
                    reserva.getIdEjemplar(),
                    reserva.getFechaReserva()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReservaView().setVisible(true));
    }
}
