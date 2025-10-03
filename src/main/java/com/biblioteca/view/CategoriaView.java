package com.biblioteca.view;

import com.biblioteca.controller.CategoriaController;
import com.biblioteca.dto.CategoriaDTO;
import com.biblioteca.util.ComponentFactory;
import com.biblioteca.util.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class CategoriaView extends JPanel {
    private CategoriaController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNombre, txtDescripcion, txtBuscar;
    private JComboBox<String> cmbEstado;
    private JButton btnGuardar, btnNuevo, btnEditar, btnEliminar;
    private Long idCategoriaSeleccionada;
    
    public CategoriaView() {
        this.controller = new CategoriaController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JLabel lblTitle = ComponentFactory.createTitleLabel("Gestión de Categorías");
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content - Split panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(800);
        splitPane.setBorder(null);
        
        // Left side - Table
        JPanel tablePanel = createTablePanel();
        splitPane.setLeftComponent(tablePanel);
        
        // Right side - Form
        JPanel formPanel = createFormPanel();
        splitPane.setRightComponent(formPanel);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createTablePanel() {
        JPanel panel = ComponentFactory.createCard();
        panel.setLayout(new BorderLayout(10, 10));
        
        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(Color.WHITE);
        
        JLabel lblBuscar = ComponentFactory.createLabel("Buscar:");
        txtBuscar = ComponentFactory.createTextField();
        txtBuscar.setPreferredSize(new Dimension(300, UIConstants.INPUT_HEIGHT));
        
        JButton btnBuscar = ComponentFactory.createPrimaryButton("🔍 Buscar");
        btnBuscar.addActionListener(e -> loadData());
        
        JButton btnMostrarTodas = ComponentFactory.createSecondaryButton("Ver Todas");
        btnMostrarTodas.addActionListener(e -> {
            txtBuscar.setText("");
            loadData();
        });
        
        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnMostrarTodas);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Nombre", "Descripción", "Estado", "Total Libros"};
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
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                loadSelectedCategoria();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = ComponentFactory.createCard();
        panel.setLayout(new BorderLayout());
        
        // Form title
        JLabel lblFormTitle = ComponentFactory.createSubtitleLabel("Datos de Categoría");
        lblFormTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(lblFormTitle, BorderLayout.NORTH);
        
        // Form fields
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        
        // Nombre
        gbc.gridy = 0;
        fieldsPanel.add(ComponentFactory.createLabel("Nombre:"), gbc);
        gbc.gridy = 1;
        txtNombre = ComponentFactory.createTextField();
        txtNombre.setPreferredSize(new Dimension(300, UIConstants.INPUT_HEIGHT));
        fieldsPanel.add(txtNombre, gbc);
        
        // Descripción
        gbc.gridy = 2;
        fieldsPanel.add(ComponentFactory.createLabel("Descripción:"), gbc);
        gbc.gridy = 3;
        txtDescripcion = ComponentFactory.createTextField();
        txtDescripcion.setPreferredSize(new Dimension(300, UIConstants.INPUT_HEIGHT));
        fieldsPanel.add(txtDescripcion, gbc);
        
        // Estado
        gbc.gridy = 4;
        fieldsPanel.add(ComponentFactory.createLabel("Estado:"), gbc);
        gbc.gridy = 5;
        cmbEstado = ComponentFactory.createComboBox(new String[]{"ACTIVO", "INACTIVO"});
        cmbEstado.setPreferredSize(new Dimension(300, UIConstants.INPUT_HEIGHT));
        fieldsPanel.add(cmbEstado, gbc);
        
        panel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        buttonPanel.setBackground(Color.WHITE);
        
        btnNuevo = ComponentFactory.createSecondaryButton("Nuevo");
        btnNuevo.addActionListener(e -> clearForm());
        
        btnGuardar = ComponentFactory.createSuccessButton("Guardar");
        btnGuardar.addActionListener(e -> saveCategoria());
        
        btnEditar = ComponentFactory.createPrimaryButton("Editar");
        btnEditar.addActionListener(e -> updateCategoria());
        btnEditar.setEnabled(false);
        
        btnEliminar = ComponentFactory.createDangerButton("Eliminar");
        btnEliminar.addActionListener(e -> deleteCategoria());
        btnEliminar.setEnabled(false);
        
        buttonPanel.add(btnNuevo);
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    @SuppressWarnings("unchecked")
    private void loadData() {
        tableModel.setRowCount(0);
        
        SwingWorker<List<CategoriaDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<CategoriaDTO> doInBackground() {
                Map<String, Object> response = controller.listarCategorias();
                if ((Boolean) response.get("success")) {
                    return (List<CategoriaDTO>) response.get("data");
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    List<CategoriaDTO> categorias = get();
                    if (categorias != null) {
                        for (CategoriaDTO cat : categorias) {
                            tableModel.addRow(new Object[]{
                                cat.getIdCategoria(),
                                cat.getNombre(),
                                cat.getDescripcion(),
                                cat.getEstado(),
                                cat.getTotalLibros() != null ? cat.getTotalLibros() : 0
                            });
                        }
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(CategoriaView.this, "Error al cargar categorías");
                }
            }
        };
        worker.execute();
    }
    
    private void loadSelectedCategoria() {
        int row = table.getSelectedRow();
        if (row != -1) {
            idCategoriaSeleccionada = (Long) tableModel.getValueAt(row, 0);
            txtNombre.setText((String) tableModel.getValueAt(row, 1));
            txtDescripcion.setText((String) tableModel.getValueAt(row, 2));
            cmbEstado.setSelectedItem(tableModel.getValueAt(row, 3));
            
            btnEditar.setEnabled(true);
            btnEliminar.setEnabled(true);
            btnGuardar.setEnabled(false);
        }
    }
    
    private void saveCategoria() {
        if (!validateForm()) return;
        
        CategoriaDTO dto = CategoriaDTO.builder()
            .nombre(txtNombre.getText().trim())
            .descripcion(txtDescripcion.getText().trim())
            .estado((String) cmbEstado.getSelectedItem())
            .build();
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return controller.crearCategoria(dto);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(CategoriaView.this, "Categoría creada exitosamente");
                        clearForm();
                        loadData();
                    } else {
                        ComponentFactory.showError(CategoriaView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(CategoriaView.this, "Error al guardar categoría");
                }
            }
        };
        worker.execute();
    }
    
    private void updateCategoria() {
        if (!validateForm() || idCategoriaSeleccionada == null) return;
        
        CategoriaDTO dto = CategoriaDTO.builder()
            .idCategoria(idCategoriaSeleccionada)
            .nombre(txtNombre.getText().trim())
            .descripcion(txtDescripcion.getText().trim())
            .estado((String) cmbEstado.getSelectedItem())
            .build();
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return controller.actualizarCategoria(dto);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(CategoriaView.this, "Categoría actualizada exitosamente");
                        clearForm();
                        loadData();
                    } else {
                        ComponentFactory.showError(CategoriaView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(CategoriaView.this, "Error al actualizar categoría");
                }
            }
        };
        worker.execute();
    }
    
    private void deleteCategoria() {
        if (idCategoriaSeleccionada == null) return;
        
        if (!ComponentFactory.showConfirm(this, "¿Está seguro de eliminar esta categoría?")) {
            return;
        }
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return controller.eliminarCategoria(idCategoriaSeleccionada);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(CategoriaView.this, "Categoría eliminada exitosamente");
                        clearForm();
                        loadData();
                    } else {
                        ComponentFactory.showError(CategoriaView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(CategoriaView.this, "Error al eliminar categoría");
                }
            }
        };
        worker.execute();
    }
    
    private boolean validateForm() {
        if (txtNombre.getText().trim().isEmpty()) {
            ComponentFactory.showWarning(this, "El nombre es obligatorio");
            return false;
        }
        return true;
    }
    
    private void clearForm() {
        idCategoriaSeleccionada = null;
        txtNombre.setText("");
        txtDescripcion.setText("");
        cmbEstado.setSelectedIndex(0);
        table.clearSelection();
        
        btnGuardar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}
