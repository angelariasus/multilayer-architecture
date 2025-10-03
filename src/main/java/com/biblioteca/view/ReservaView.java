package com.biblioteca.view;

import com.biblioteca.controller.ReservaController;
import com.biblioteca.dto.ReservaDTO;
import com.biblioteca.util.ComponentFactory;
import com.biblioteca.util.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ReservaView extends JPanel {
    private ReservaController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public ReservaView() {
        this.controller = new ReservaController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        JLabel lblTitle = ComponentFactory.createTitleLabel("Gestión de Reservas");
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JButton btnNuevaReserva = ComponentFactory.createPrimaryButton("+ Nueva Reserva");
        btnNuevaReserva.addActionListener(e -> ComponentFactory.showWarning(this, "Funcionalidad en desarrollo"));
        
        JButton btnVencidas = ComponentFactory.createDangerButton("Ver Vencidas");
        btnVencidas.addActionListener(e -> loadVencidas());
        
        JButton btnTodas = ComponentFactory.createSecondaryButton("Ver Todas");
        btnTodas.addActionListener(e -> loadData());
        
        JButton btnProcesarVencidas = ComponentFactory.createSecondaryButton("Procesar Vencidas");
        btnProcesarVencidas.addActionListener(e -> procesarVencidas());
        
        buttonPanel.add(btnNuevaReserva);
        buttonPanel.add(btnVencidas);
        buttonPanel.add(btnTodas);
        buttonPanel.add(btnProcesarVencidas);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = ComponentFactory.createCard();
        tablePanel.setLayout(new BorderLayout());
        
        String[] columns = {"ID", "Usuario", "Libro", "Fecha Reserva", "Fecha Vencimiento", "Estado", "Posición Cola"};
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
        
        JButton btnCumplir = ComponentFactory.createSuccessButton("Cumplir Reserva");
        btnCumplir.addActionListener(e -> cumplirReserva());
        
        JButton btnCancelar = ComponentFactory.createDangerButton("Cancelar Reserva");
        btnCancelar.addActionListener(e -> cancelarReserva());
        
        JButton btnEliminar = ComponentFactory.createDangerButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarReserva());
        
        actionPanel.add(btnCumplir);
        actionPanel.add(btnCancelar);
        actionPanel.add(btnEliminar);
        
        tablePanel.add(actionPanel, BorderLayout.SOUTH);
        
        add(tablePanel, BorderLayout.CENTER);
    }
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        tableModel.setRowCount(0);
        
        SwingWorker<List<ReservaDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<ReservaDTO> doInBackground() {
                Map<String, Object> response = controller.listarReservas();
                if ((Boolean) response.get("success")) {
                    return (List<ReservaDTO>) response.get("data");
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    List<ReservaDTO> reservas = get();
                    if (reservas != null) {
                        for (ReservaDTO r : reservas) {
                            tableModel.addRow(new Object[]{
                                r.getIdReserva(),
                                r.getNombreUsuario(),
                                r.getTituloLibro(),
                                r.getFechaReserva(),
                                r.getFechaVencimiento(),
                                r.getEstado(),
                                r.getPosicionCola()
                            });
                        }
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(ReservaView.this, "Error al cargar reservas");
                }
            }
        };
        worker.execute();
    }
    
    @SuppressWarnings("unchecked")
    private void loadVencidas() {
        tableModel.setRowCount(0);
        
        SwingWorker<List<ReservaDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<ReservaDTO> doInBackground() {
                Map<String, Object> response = controller.listarReservasVencidas();
                if ((Boolean) response.get("success")) {
                    return (List<ReservaDTO>) response.get("data");
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    List<ReservaDTO> reservas = get();
                    if (reservas != null) {
                        for (ReservaDTO r : reservas) {
                            tableModel.addRow(new Object[]{
                                r.getIdReserva(),
                                r.getNombreUsuario(),
                                r.getTituloLibro(),
                                r.getFechaReserva(),
                                r.getFechaVencimiento(),
                                r.getEstado(),
                                r.getPosicionCola()
                            });
                        }
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(ReservaView.this, "Error al cargar reservas vencidas");
                }
            }
        };
        worker.execute();
    }
    
    private void cumplirReserva() {
        int row = table.getSelectedRow();
        if (row == -1) {
            ComponentFactory.showWarning(this, "Seleccione una reserva");
            return;
        }
        
        Long idReserva = (Long) tableModel.getValueAt(row, 0);
        
        if (!ComponentFactory.showConfirm(this, "¿Confirmar cumplimiento de reserva?")) {
            return;
        }
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return controller.cumplirReserva(idReserva);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(ReservaView.this, "Reserva cumplida exitosamente");
                        loadData();
                    } else {
                        ComponentFactory.showError(ReservaView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(ReservaView.this, "Error al cumplir reserva");
                }
            }
        };
        worker.execute();
    }
    
    private void cancelarReserva() {
        int row = table.getSelectedRow();
        if (row == -1) {
            ComponentFactory.showWarning(this, "Seleccione una reserva");
            return;
        }
        
        Long idReserva = (Long) tableModel.getValueAt(row, 0);
        String motivo = JOptionPane.showInputDialog(this, "Motivo de cancelación:");
        
        if (motivo != null && !motivo.trim().isEmpty()) {
            SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
                @Override
                protected Map<String, Object> doInBackground() {
                    return controller.cancelarReserva(idReserva, motivo);
                }
                
                @Override
                protected void done() {
                    try {
                        Map<String, Object> response = get();
                        if ((Boolean) response.get("success")) {
                            ComponentFactory.showSuccess(ReservaView.this, "Reserva cancelada exitosamente");
                            loadData();
                        } else {
                            ComponentFactory.showError(ReservaView.this, (String) response.get("message"));
                        }
                    } catch (Exception ex) {
                        ComponentFactory.showError(ReservaView.this, "Error al cancelar reserva");
                    }
                }
            };
            worker.execute();
        }
    }
    
    private void procesarVencidas() {
        if (!ComponentFactory.showConfirm(this, "¿Procesar todas las reservas vencidas?")) {
            return;
        }
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return controller.procesarReservasVencidas();
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(ReservaView.this, "Reservas vencidas procesadas exitosamente");
                        loadData();
                    } else {
                        ComponentFactory.showError(ReservaView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(ReservaView.this, "Error al procesar reservas vencidas");
                }
            }
        };
        worker.execute();
    }
    
    private void eliminarReserva() {
        int row = table.getSelectedRow();
        if (row == -1) {
            ComponentFactory.showWarning(this, "Seleccione una reserva");
            return;
        }
        
        if (!ComponentFactory.showConfirm(this, "¿Está seguro de eliminar esta reserva?")) {
            return;
        }
        
        Long idReserva = (Long) tableModel.getValueAt(row, 0);
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return controller.eliminarReserva(idReserva);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(ReservaView.this, "Reserva eliminada exitosamente");
                        loadData();
                    } else {
                        ComponentFactory.showError(ReservaView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(ReservaView.this, "Error al eliminar reserva");
                }
            }
        };
        worker.execute();
    }
}
