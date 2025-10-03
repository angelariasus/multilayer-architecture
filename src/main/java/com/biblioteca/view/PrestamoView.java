package com.biblioteca.view;

import com.biblioteca.controller.PrestamoController;
import com.biblioteca.controller.UsuarioController;
import com.biblioteca.controller.EjemplarController;
import com.biblioteca.controller.LibroController;
import com.biblioteca.dto.PrestamoDTO;
import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.dto.EjemplarDTO;
import com.biblioteca.dto.LibroDTO;
import com.biblioteca.util.ComponentFactory;
import com.biblioteca.util.UIConstants;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

public class PrestamoView extends JPanel {
    private PrestamoController controller;
    private UsuarioController usuarioController;
    private EjemplarController ejemplarController;
    private LibroController libroController;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public PrestamoView() {
        this.controller = new PrestamoController();
        this.usuarioController = new UsuarioController();
        this.ejemplarController = new EjemplarController();
        this.libroController = new LibroController();
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
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Préstamo", true);
        dialog.setLayout(new BorderLayout(20, 20));
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        
        // Usuario selection
        gbc.gridy = 0;
        formPanel.add(ComponentFactory.createLabel("Usuario:"), gbc);
        gbc.gridy = 1;
        JComboBox<String> cmbUsuario = new JComboBox<>();
        cmbUsuario.setFont(UIConstants.NORMAL_FONT);
        cmbUsuario.setPreferredSize(new Dimension(400, UIConstants.INPUT_HEIGHT));
        formPanel.add(cmbUsuario, gbc);
        
        // Libro selection
        gbc.gridy = 2;
        formPanel.add(ComponentFactory.createLabel("Libro:"), gbc);
        gbc.gridy = 3;
        JComboBox<String> cmbLibro = new JComboBox<>();
        cmbLibro.setFont(UIConstants.NORMAL_FONT);
        cmbLibro.setPreferredSize(new Dimension(400, UIConstants.INPUT_HEIGHT));
        formPanel.add(cmbLibro, gbc);
        
        // Ejemplar selection
        gbc.gridy = 4;
        formPanel.add(ComponentFactory.createLabel("Ejemplar Disponible:"), gbc);
        gbc.gridy = 5;
        JComboBox<String> cmbEjemplar = new JComboBox<>();
        cmbEjemplar.setFont(UIConstants.NORMAL_FONT);
        cmbEjemplar.setPreferredSize(new Dimension(400, UIConstants.INPUT_HEIGHT));
        cmbEjemplar.setEnabled(false);
        formPanel.add(cmbEjemplar, gbc);
        
        // Expected return date
        gbc.gridy = 6;
        formPanel.add(ComponentFactory.createLabel("Fecha Devolución Esperada:"), gbc);
        gbc.gridy = 7;
        JDateChooser dateFechaDevolucion = new JDateChooser();
        dateFechaDevolucion.setFont(UIConstants.NORMAL_FONT);
        dateFechaDevolucion.setPreferredSize(new Dimension(400, UIConstants.INPUT_HEIGHT));
        dateFechaDevolucion.setMinSelectableDate(new java.util.Date());
        formPanel.add(dateFechaDevolucion, gbc);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        
        // Load users
        @SuppressWarnings("unchecked")
        SwingWorker<List<UsuarioDTO>, Void> userWorker = new SwingWorker<>() {
            @Override
            protected List<UsuarioDTO> doInBackground() {
                Map<String, Object> response = usuarioController.listarUsuarios();
                if ((Boolean) response.get("success")) {
                    return (List<UsuarioDTO>) response.get("data");
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    List<UsuarioDTO> usuarios = get();
                    if (usuarios != null) {
                        for (UsuarioDTO u : usuarios) {
                            cmbUsuario.addItem(u.getIdUsuario() + " - " + u.getNombre() + " (" + u.getTipo() + ")");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        userWorker.execute();
        
        // Load books
        @SuppressWarnings("unchecked")
        SwingWorker<List<LibroDTO>, Void> bookWorker = new SwingWorker<>() {
            @Override
            protected List<LibroDTO> doInBackground() {
                Map<String, Object> response = libroController.listarLibros();
                if ((Boolean) response.get("success")) {
                    return (List<LibroDTO>) response.get("data");
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    List<LibroDTO> libros = get();
                    if (libros != null) {
                        for (LibroDTO l : libros) {
                            cmbLibro.addItem(l.getIdLibro() + " - " + l.getTitulo() + " (" + l.getAutor() + ")");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        bookWorker.execute();
        
        // When book is selected, load available exemplars
        cmbLibro.addActionListener(e -> {
            if (cmbLibro.getSelectedIndex() > -1) {
                String selected = (String) cmbLibro.getSelectedItem();
                Long idLibro = Long.parseLong(selected.split(" - ")[0]);
                
                cmbEjemplar.removeAllItems();
                cmbEjemplar.setEnabled(false);
                
                // Load available exemplars
                @SuppressWarnings("unchecked")
                SwingWorker<List<EjemplarDTO>, Void> ejemplarWorker = new SwingWorker<>() {
                    @Override
                    protected List<EjemplarDTO> doInBackground() {
                        Map<String, Object> response = ejemplarController.listarEjemplaresDisponibles(idLibro);
                        if ((Boolean) response.get("success")) {
                            return (List<EjemplarDTO>) response.get("data");
                        }
                        return null;
                    }
                    
                    @Override
                    protected void done() {
                        try {
                            List<EjemplarDTO> ejemplares = get();
                            if (ejemplares != null && !ejemplares.isEmpty()) {
                                for (EjemplarDTO ej : ejemplares) {
                                    cmbEjemplar.addItem(ej.getIdEjemplar() + " - " + ej.getCodigoEjemplar() + " (" + ej.getEstado() + ")");
                                }
                                cmbEjemplar.setEnabled(true);
                            } else {
                                cmbEjemplar.addItem("No hay ejemplares disponibles");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                ejemplarWorker.execute();
            }
        });
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnGuardar = ComponentFactory.createPrimaryButton("Crear Préstamo");
        btnGuardar.addActionListener(e -> {
            if (cmbUsuario.getSelectedIndex() == -1 || cmbLibro.getSelectedIndex() == -1 || 
                cmbEjemplar.getSelectedIndex() == -1 || dateFechaDevolucion.getDate() == null) {
                ComponentFactory.showWarning(dialog, "Complete todos los campos");
                return;
            }
            
            if (!cmbEjemplar.isEnabled() || cmbEjemplar.getItemCount() == 0 || 
                cmbEjemplar.getSelectedItem().toString().contains("No hay ejemplares")) {
                ComponentFactory.showWarning(dialog, "No hay ejemplares disponibles para este libro");
                return;
            }
            
            String selectedUsuario = (String) cmbUsuario.getSelectedItem();
            Long idUsuario = Long.parseLong(selectedUsuario.split(" - ")[0]);
            
            String selectedLibro = (String) cmbLibro.getSelectedItem();
            Long idLibro = Long.parseLong(selectedLibro.split(" - ")[0]);
            
            String selectedEjemplar = (String) cmbEjemplar.getSelectedItem();
            Long idEjemplar = Long.parseLong(selectedEjemplar.split(" - ")[0]);
            
            LocalDate fechaDevolucion = dateFechaDevolucion.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
            
            PrestamoDTO prestamo = PrestamoDTO.builder()
                .idUsuario(idUsuario)
                .idLibro(idLibro)
                .idEjemplar(idEjemplar)
                .fechaPrestamo(LocalDate.now())
                .fechaDevolucionEsperada(fechaDevolucion)
                .estado("ACTIVO")
                .build();
            
            SwingWorker<Map<String, Object>, Void> saveWorker = new SwingWorker<>() {
                @Override
                protected Map<String, Object> doInBackground() {
                    return controller.crearPrestamo(prestamo);
                }
                
                @Override
                protected void done() {
                    try {
                        Map<String, Object> response = get();
                        if ((Boolean) response.get("success")) {
                            ComponentFactory.showSuccess(PrestamoView.this, "Préstamo creado exitosamente");
                            dialog.dispose();
                            loadData();
                        } else {
                            ComponentFactory.showError(dialog, (String) response.get("message"));
                        }
                    } catch (Exception ex) {
                        ComponentFactory.showError(dialog, "Error al crear préstamo");
                    }
                }
            };
            saveWorker.execute();
        });
        
        JButton btnCancelar = ComponentFactory.createSecondaryButton("Cancelar");
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
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
