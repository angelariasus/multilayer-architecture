package com.biblioteca.view;

import com.biblioteca.controller.PrestamoController;
import com.biblioteca.controller.UsuarioController;
import com.biblioteca.controller.EjemplarController;
import com.biblioteca.dto.PrestamoDTO;
import com.biblioteca.util.ComponentFactory;
import com.biblioteca.util.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class PrestamoView extends JPanel {
    private PrestamoController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public PrestamoView() {
        this.controller = new PrestamoController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        JLabel lblTitle = ComponentFactory.createTitleLabel("Gestión de Préstamos");
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JButton btnNuevoPrestamo = ComponentFactory.createPrimaryButton("+ Nuevo Préstamo");
        btnNuevoPrestamo.addActionListener(e -> showNuevoPrestamoDialog());
        
        JButton btnVencidos = ComponentFactory.createDangerButton("Ver Vencidos");
        btnVencidos.addActionListener(e -> loadVencidos());
        
        JButton btnTodos = ComponentFactory.createSecondaryButton("Ver Todos");
        btnTodos.addActionListener(e -> loadData());
        
        buttonPanel.add(btnNuevoPrestamo);
        buttonPanel.add(btnVencidos);
        buttonPanel.add(btnTodos);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = ComponentFactory.createCard();
        tablePanel.setLayout(new BorderLayout());
        
        String[] columns = {"ID", "Usuario", "Libro", "Ejemplar", "Fecha Préstamo", "Fecha Devolución", "Estado", "Días Retraso"};
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
        
        JButton btnDevolver = ComponentFactory.createSuccessButton("Devolver Libro");
        btnDevolver.addActionListener(e -> devolverLibro());
        
        JButton btnEliminar = ComponentFactory.createDangerButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarPrestamo());
        
        actionPanel.add(btnDevolver);
        actionPanel.add(btnEliminar);
        
        tablePanel.add(actionPanel, BorderLayout.SOUTH);
        
        add(tablePanel, BorderLayout.CENTER);
    }
    @SuppressWarnings("unchecked")
    private void loadData() {
        tableModel.setRowCount(0);
        
        SwingWorker<List<PrestamoDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<PrestamoDTO> doInBackground() {
                Map<String, Object> response = controller.listarPrestamos();
                if ((Boolean) response.get("success")) {
                    return (List<PrestamoDTO>) response.get("data");
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    List<PrestamoDTO> prestamos = get();
                    if (prestamos != null) {
                        for (PrestamoDTO p : prestamos) {
                            tableModel.addRow(new Object[]{
                                p.getIdPrestamo(),
                                p.getNombreUsuario(),
                                p.getTituloLibro(),
                                p.getCodigoEjemplar(),
                                p.getFechaPrestamo(),
                                p.getFechaDevolucionEsperada(),
                                p.getEstado(),
                                p.getDiasRetraso() != null ? p.getDiasRetraso() : 0
                            });
                        }
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(PrestamoView.this, "Error al cargar préstamos");
                }
            }
        };
        worker.execute();
    }
    @SuppressWarnings("unchecked")
    private void loadVencidos() {
        tableModel.setRowCount(0);
        
        SwingWorker<List<PrestamoDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<PrestamoDTO> doInBackground() {
                Map<String, Object> response = controller.listarPrestamosVencidos();
                if ((Boolean) response.get("success")) {
                    return (List<PrestamoDTO>) response.get("data");
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    List<PrestamoDTO> prestamos = get();
                    if (prestamos != null) {
                        for (PrestamoDTO p : prestamos) {
                            tableModel.addRow(new Object[]{
                                p.getIdPrestamo(),
                                p.getNombreUsuario(),
                                p.getTituloLibro(),
                                p.getCodigoEjemplar(),
                                p.getFechaPrestamo(),
                                p.getFechaDevolucionEsperada(),
                                p.getEstado(),
                                p.getDiasRetraso()
                            });
                        }
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(PrestamoView.this, "Error al cargar préstamos vencidos");
                }
            }
        };
        worker.execute();
    }
    
    private void showNuevoPrestamoDialog() {
        ComponentFactory.showWarning(this, "Funcionalidad de nuevo préstamo - Implementar diálogo completo");
    }
    
    private void devolverLibro() {
        int row = table.getSelectedRow();
        if (row == -1) {
            ComponentFactory.showWarning(this, "Seleccione un préstamo");
            return;
        }
        
        Long idPrestamo = (Long) tableModel.getValueAt(row, 0);
        String observaciones = JOptionPane.showInputDialog(this, "Observaciones de devolución:");
        
        if (observaciones != null) {
            SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
                @Override
                protected Map<String, Object> doInBackground() {
                    return controller.devolverLibro(idPrestamo, observaciones);
                }
                
                @Override
                protected void done() {
                    try {
                        Map<String, Object> response = get();
                        if ((Boolean) response.get("success")) {
                            ComponentFactory.showSuccess(PrestamoView.this, "Libro devuelto exitosamente");
                            loadData();
                        } else {
                            ComponentFactory.showError(PrestamoView.this, (String) response.get("message"));
                        }
                    } catch (Exception ex) {
                        ComponentFactory.showError(PrestamoView.this, "Error al devolver libro");
                    }
                }
            };
            worker.execute();
        }
    }
    
    private void eliminarPrestamo() {
        int row = table.getSelectedRow();
        if (row == -1) {
            ComponentFactory.showWarning(this, "Seleccione un préstamo");
            return;
        }
        
        if (!ComponentFactory.showConfirm(this, "¿Está seguro de eliminar este préstamo?")) {
            return;
        }
        
        Long idPrestamo = (Long) tableModel.getValueAt(row, 0);
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return controller.eliminarPrestamo(idPrestamo);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(PrestamoView.this, "Préstamo eliminado exitosamente");
                        loadData();
                    } else {
                        ComponentFactory.showError(PrestamoView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(PrestamoView.this, "Error al eliminar préstamo");
                }
            }
        };
        worker.execute();
    }
}
