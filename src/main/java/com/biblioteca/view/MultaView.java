package com.biblioteca.view;

import com.biblioteca.controller.MultaController;
import com.biblioteca.dto.MultaDTO;
import com.biblioteca.util.ComponentFactory;
import com.biblioteca.util.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class MultaView extends JPanel {
    private MultaController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public MultaView() {
        this.controller = new MultaController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        JLabel lblTitle = ComponentFactory.createTitleLabel("Gestión de Multas");
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JButton btnPendientes = ComponentFactory.createDangerButton("Ver Pendientes");
        btnPendientes.addActionListener(e -> loadPendientes());
        
        JButton btnTodas = ComponentFactory.createSecondaryButton("Ver Todas");
        btnTodas.addActionListener(e -> loadData());
        
        JButton btnGenerarPorRetraso = ComponentFactory.createPrimaryButton("Generar por Retraso");
        btnGenerarPorRetraso.addActionListener(e -> generarMultaPorRetraso());
        
        buttonPanel.add(btnPendientes);
        buttonPanel.add(btnTodas);
        buttonPanel.add(btnGenerarPorRetraso);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = ComponentFactory.createCard();
        tablePanel.setLayout(new BorderLayout());
        
        String[] columns = {"ID", "Usuario", "Libro", "Monto", "Fecha Generación", "Estado", "Motivo", "Días Retraso"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(UIConstants.NORMAL_FONT);
        table.setRowHeight(35);
        table.getTableHeader().setFont(UIConstants.HEADER_FONT);
        table.getTableHeader().setBackground(UIConstants.PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(UIConstants.SECONDARY_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        actionPanel.setBackground(Color.WHITE);
        
        JButton btnPagar = ComponentFactory.createSuccessButton("Pagar Multa");
        btnPagar.addActionListener(e -> pagarMulta());
        
        JButton btnCancelar = ComponentFactory.createDangerButton("Cancelar Multa");
        btnCancelar.addActionListener(e -> cancelarMulta());
        
        JButton btnCalcularInteres = ComponentFactory.createSecondaryButton("Calcular con Interés");
        btnCalcularInteres.addActionListener(e -> calcularMontoConInteres());
        
        JButton btnEliminar = ComponentFactory.createDangerButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarMulta());
        
        actionPanel.add(btnPagar);
        actionPanel.add(btnCancelar);
        actionPanel.add(btnCalcularInteres);
        actionPanel.add(btnEliminar);
        
        tablePanel.add(actionPanel, BorderLayout.SOUTH);
        
        add(tablePanel, BorderLayout.CENTER);
    }
    @SuppressWarnings("unchecked")
    private void loadData() {
        tableModel.setRowCount(0);
        
        SwingWorker<List<MultaDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<MultaDTO> doInBackground() {
                Map<String, Object> response = controller.listarMultas();
                if ((Boolean) response.get("success")) {
                    return (List<MultaDTO>) response.get("data");
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    List<MultaDTO> multas = get();
                    if (multas != null) {
                        for (MultaDTO m : multas) {
                            tableModel.addRow(new Object[]{
                                m.getIdMulta(),
                                m.getNombreUsuario(),
                                m.getTituloLibro(),
                                "$" + m.getMonto(),
                                m.getFechaGeneracion(),
                                m.getEstado(),
                                m.getMotivo(),
                                m.getDiasRetraso()
                            });
                        }
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(MultaView.this, "Error al cargar multas");
                }
            }
        };
        worker.execute();
    }
    @SuppressWarnings("unchecked")
    private void loadPendientes() {
        tableModel.setRowCount(0);
        
        SwingWorker<List<MultaDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<MultaDTO> doInBackground() {
                Map<String, Object> response = controller.listarMultasPendientes();
                if ((Boolean) response.get("success")) {
                    return (List<MultaDTO>) response.get("data");
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    List<MultaDTO> multas = get();
                    if (multas != null) {
                        for (MultaDTO m : multas) {
                            tableModel.addRow(new Object[]{
                                m.getIdMulta(),
                                m.getNombreUsuario(),
                                m.getTituloLibro(),
                                "$" + m.getMonto(),
                                m.getFechaGeneracion(),
                                m.getEstado(),
                                m.getMotivo(),
                                m.getDiasRetraso()
                            });
                        }
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(MultaView.this, "Error al cargar multas pendientes");
                }
            }
        };
        worker.execute();
    }
    
    private void pagarMulta() {
        int row = table.getSelectedRow();
        if (row == -1) {
            ComponentFactory.showWarning(this, "Seleccione una multa");
            return;
        }
        
        Long idMulta = (Long) tableModel.getValueAt(row, 0);
        
        if (!ComponentFactory.showConfirm(this, "¿Confirmar pago de multa?")) {
            return;
        }
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return controller.pagarMulta(idMulta);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(MultaView.this, "Multa pagada exitosamente");
                        loadData();
                    } else {
                        ComponentFactory.showError(MultaView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(MultaView.this, "Error al pagar multa");
                }
            }
        };
        worker.execute();
    }
    
    private void cancelarMulta() {
        int row = table.getSelectedRow();
        if (row == -1) {
            ComponentFactory.showWarning(this, "Seleccione una multa");
            return;
        }
        
        Long idMulta = (Long) tableModel.getValueAt(row, 0);
        String motivo = JOptionPane.showInputDialog(this, "Motivo de cancelación:");
        
        if (motivo != null && !motivo.trim().isEmpty()) {
            SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
                @Override
                protected Map<String, Object> doInBackground() {
                    return controller.cancelarMulta(idMulta, motivo);
                }
                
                @Override
                protected void done() {
                    try {
                        Map<String, Object> response = get();
                        if ((Boolean) response.get("success")) {
                            ComponentFactory.showSuccess(MultaView.this, "Multa cancelada exitosamente");
                            loadData();
                        } else {
                            ComponentFactory.showError(MultaView.this, (String) response.get("message"));
                        }
                    } catch (Exception ex) {
                        ComponentFactory.showError(MultaView.this, "Error al cancelar multa");
                    }
                }
            };
            worker.execute();
        }
    }
    
    private void calcularMontoConInteres() {
        int row = table.getSelectedRow();
        if (row == -1) {
            ComponentFactory.showWarning(this, "Seleccione una multa");
            return;
        }
        
        Long idMulta = (Long) tableModel.getValueAt(row, 0);
        
        SwingWorker<BigDecimal, Void> worker = new SwingWorker<>() {
            @Override
            protected BigDecimal doInBackground() {
                Map<String, Object> response = controller.calcularMontoConInteres(idMulta);
                if ((Boolean) response.get("success")) {
                    return (BigDecimal) response.get("data");
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    BigDecimal monto = get();
                    if (monto != null) {
                        JOptionPane.showMessageDialog(MultaView.this, 
                            "Monto con interés: $" + monto, 
                            "Cálculo de Interés", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(MultaView.this, "Error al calcular interés");
                }
            }
        };
        worker.execute();
    }
    
    private void generarMultaPorRetraso() {
        String idPrestamoStr = JOptionPane.showInputDialog(this, "Ingrese ID del préstamo:");
        if (idPrestamoStr != null && !idPrestamoStr.trim().isEmpty()) {
            try {
                Long idPrestamo = Long.parseLong(idPrestamoStr);
                
                SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Map<String, Object> doInBackground() {
                        return controller.generarMultaPorRetraso(idPrestamo);
                    }
                    
                    @Override
                    protected void done() {
                        try {
                            Map<String, Object> response = get();
                            if ((Boolean) response.get("success")) {
                                ComponentFactory.showSuccess(MultaView.this, "Multa generada exitosamente");
                                loadData();
                            } else {
                                ComponentFactory.showError(MultaView.this, (String) response.get("message"));
                            }
                        } catch (Exception ex) {
                            ComponentFactory.showError(MultaView.this, "Error al generar multa");
                        }
                    }
                };
                worker.execute();
            } catch (NumberFormatException ex) {
                ComponentFactory.showWarning(this, "ID de préstamo inválido");
            }
        }
    }
    
    private void eliminarMulta() {
        int row = table.getSelectedRow();
        if (row == -1) {
            ComponentFactory.showWarning(this, "Seleccione una multa");
            return;
        }
        
        if (!ComponentFactory.showConfirm(this, "¿Está seguro de eliminar esta multa?")) {
            return;
        }
        
        Long idMulta = (Long) tableModel.getValueAt(row, 0);
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return controller.eliminarMulta(idMulta);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(MultaView.this, "Multa eliminada exitosamente");
                        loadData();
                    } else {
                        ComponentFactory.showError(MultaView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(MultaView.this, "Error al eliminar multa");
                }
            }
        };
        worker.execute();
    }
}
