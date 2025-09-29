package com.biblioteca.view;

import com.biblioteca.model.Multa;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MultaView extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Multa> multas;

    public MultaView() {
        multas = new ArrayList<>();

        setTitle("Gestión de Multas");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID Multa", "ID Usuario", "Monto", "Pagado", "Fecha Multa"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Agregar");
        JButton payButton = new JButton("Marcar como Pagado");
        JButton refreshButton = new JButton("Refrescar");

        buttonPanel.add(addButton);
        buttonPanel.add(payButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Agregar multa
        addButton.addActionListener(e -> {
            int idMulta = multas.size() + 1;
            int idUsuario = Integer.parseInt(JOptionPane.showInputDialog("Ingrese ID Usuario:"));
            double monto = Double.parseDouble(JOptionPane.showInputDialog("Ingrese Monto:"));

            // Usamos el constructor completo
            Multa multa = new Multa(idMulta, idUsuario, monto, "No", new Date());
            multas.add(multa);
            refreshTable();
        });

        // Marcar como pagado
        payButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Multa multa = multas.get(selectedRow);
                multa.setPagado("Sí"); // ← usamos getPagado/setPagado (no getPagada)
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una multa para marcar como pagada.");
            }
        });

        // Refrescar tabla
        refreshButton.addActionListener(e -> refreshTable());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Multa multa : multas) {
            tableModel.addRow(new Object[]{
                    multa.getIdMulta(),
                    multa.getIdUsuario(),
                    multa.getMonto(),
                    multa.getPagado(),
                    multa.getFechaMulta()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MultaView().setVisible(true));
    }
}
