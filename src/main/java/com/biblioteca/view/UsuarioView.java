package com.biblioteca.view;

import com.biblioteca.controller.UsuarioController;
import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.util.ComponentFactory;
import com.biblioteca.util.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class UsuarioView extends JPanel {
    private UsuarioController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNombre, txtUsername, txtEmail, txtTelefono, txtDireccion;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbTipo, cmbEstado;
    private JButton btnGuardar, btnNuevo, btnEditar, btnEliminar, btnCambiarPassword;
    private Long idUsuarioSeleccionado;
    
    public UsuarioView() {
        this.controller = new UsuarioController();
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        JLabel lblTitle = ComponentFactory.createTitleLabel("Gestión de Usuarios");
        headerPanel.add(lblTitle, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(800);
        splitPane.setBorder(null);
        
        splitPane.setLeftComponent(createTablePanel());
        splitPane.setRightComponent(createFormPanel());
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createTablePanel() {
        JPanel panel = ComponentFactory.createCard();
        panel.setLayout(new BorderLayout(10, 10));
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(Color.WHITE);
        
        JLabel lblFiltro = ComponentFactory.createLabel("Filtrar por tipo:");
        JComboBox<String> cmbFiltroTipo = ComponentFactory.createComboBox(
            new String[]{"TODOS", "ESTUDIANTE", "DOCENTE", "ADMINISTRATIVO", "EXTERNO"});
        cmbFiltroTipo.addActionListener(e -> {
            String tipo = (String) cmbFiltroTipo.getSelectedItem();
            if ("TODOS".equals(tipo)) {
                loadData();
            } else {
                loadByTipo(tipo);
            }
        });
        
        filterPanel.add(lblFiltro);
        filterPanel.add(cmbFiltroTipo);
        
        panel.add(filterPanel, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Nombre", "Username", "Tipo", "Email", "Teléfono", "Estado"};
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
                loadSelectedUsuario();
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
        
        JLabel lblFormTitle = ComponentFactory.createSubtitleLabel("Datos del Usuario");
        lblFormTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(lblFormTitle, BorderLayout.NORTH);
        
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        
        int row = 0;
        
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Nombre Completo:"), gbc);
        gbc.gridy = row++;
        txtNombre = ComponentFactory.createTextField();
        fieldsPanel.add(txtNombre, gbc);
        
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Username:"), gbc);
        gbc.gridy = row++;
        txtUsername = ComponentFactory.createTextField();
        fieldsPanel.add(txtUsername, gbc);
        
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Password:"), gbc);
        gbc.gridy = row++;
        txtPassword = new JPasswordField();
        txtPassword.setFont(UIConstants.NORMAL_FONT);
        txtPassword.setPreferredSize(new Dimension(250, UIConstants.INPUT_HEIGHT));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        fieldsPanel.add(txtPassword, gbc);
        
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Tipo:"), gbc);
        gbc.gridy = row++;
        cmbTipo = ComponentFactory.createComboBox(new String[]{"ESTUDIANTE", "DOCENTE", "ADMINISTRATIVO", "EXTERNO"});
        fieldsPanel.add(cmbTipo, gbc);
        
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Email:"), gbc);
        gbc.gridy = row++;
        txtEmail = ComponentFactory.createTextField();
        fieldsPanel.add(txtEmail, gbc);
        
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Teléfono:"), gbc);
        gbc.gridy = row++;
        txtTelefono = ComponentFactory.createTextField();
        fieldsPanel.add(txtTelefono, gbc);
        
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Dirección:"), gbc);
        gbc.gridy = row++;
        txtDireccion = ComponentFactory.createTextField();
        fieldsPanel.add(txtDireccion, gbc);
        
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Estado:"), gbc);
        gbc.gridy = row++;
        cmbEstado = ComponentFactory.createComboBox(new String[]{"ACTIVO", "INACTIVO", "BLOQUEADO"});
        fieldsPanel.add(cmbEstado, gbc);
        
        JScrollPane scrollFields = new JScrollPane(fieldsPanel);
        scrollFields.setBorder(null);
        scrollFields.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollFields, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        btnNuevo = ComponentFactory.createSecondaryButton("Nuevo");
        btnNuevo.addActionListener(e -> clearForm());
        
        btnGuardar = ComponentFactory.createSuccessButton("Guardar");
        btnGuardar.addActionListener(e -> saveUsuario());
        
        btnEditar = ComponentFactory.createPrimaryButton("Editar");
        btnEditar.addActionListener(e -> updateUsuario());
        btnEditar.setEnabled(false);
        
        btnEliminar = ComponentFactory.createDangerButton("Eliminar");
        btnEliminar.addActionListener(e -> deleteUsuario());
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
        
        SwingWorker<List<UsuarioDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<UsuarioDTO> doInBackground() {
                Map<String, Object> response = controller.listarUsuarios();
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
                        for (UsuarioDTO user : usuarios) {
                            tableModel.addRow(new Object[]{
                                user.getIdUsuario(),
                                user.getNombre(),
                                user.getUsername(),
                                user.getTipo(),
                                user.getEmail(),
                                user.getTelefono(),
                                user.getEstado()
                            });
                        }
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(UsuarioView.this, "Error al cargar usuarios");
                }
            }
        };
        worker.execute();
    }
    @SuppressWarnings("unchecked")
    private void loadByTipo(String tipo) {
        tableModel.setRowCount(0);
        
        SwingWorker<List<UsuarioDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<UsuarioDTO> doInBackground() {
                Map<String, Object> response = controller.listarUsuariosPorTipo(tipo);
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
                        for (UsuarioDTO user : usuarios) {
                            tableModel.addRow(new Object[]{
                                user.getIdUsuario(),
                                user.getNombre(),
                                user.getUsername(),
                                user.getTipo(),
                                user.getEmail(),
                                user.getTelefono(),
                                user.getEstado()
                            });
                        }
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(UsuarioView.this, "Error al cargar usuarios");
                }
            }
        };
        worker.execute();
    }
    
    private void loadSelectedUsuario() {
        int row = table.getSelectedRow();
        if (row != -1) {
            idUsuarioSeleccionado = (Long) tableModel.getValueAt(row, 0);
            
            SwingWorker<UsuarioDTO, Void> worker = new SwingWorker<>() {
                @Override
                protected UsuarioDTO doInBackground() {
                    Map<String, Object> response = controller.obtenerUsuario(idUsuarioSeleccionado);
                    if ((Boolean) response.get("success")) {
                        return (UsuarioDTO) response.get("data");
                    }
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        UsuarioDTO usuario = get();
                        if (usuario != null) {
                            txtNombre.setText(usuario.getNombre());
                            txtUsername.setText(usuario.getUsername());
                            txtEmail.setText(usuario.getEmail());
                            txtTelefono.setText(usuario.getTelefono());
                            txtDireccion.setText(usuario.getDireccion());
                            cmbTipo.setSelectedItem(usuario.getTipo());
                            cmbEstado.setSelectedItem(usuario.getEstado());
                            txtPassword.setText(""); // Don't show password
                            
                            btnEditar.setEnabled(true);
                            btnEliminar.setEnabled(true);
                            btnGuardar.setEnabled(false);
                        }
                    } catch (Exception ex) {
                        ComponentFactory.showError(UsuarioView.this, "Error al cargar datos del usuario");
                    }
                }
            };
            worker.execute();
        }
    }
    
    private void saveUsuario() {
        if (!validateForm()) return;
        
        UsuarioDTO dto = UsuarioDTO.builder()
            .nombre(txtNombre.getText().trim())
            .username(txtUsername.getText().trim())
            .tipo((String) cmbTipo.getSelectedItem())
            .email(txtEmail.getText().trim())
            .telefono(txtTelefono.getText().trim())
            .direccion(txtDireccion.getText().trim())
            .estado((String) cmbEstado.getSelectedItem())
            .build();
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return controller.crearUsuario(dto);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(UsuarioView.this, "Usuario creado exitosamente");
                        clearForm();
                        loadData();
                    } else {
                        ComponentFactory.showError(UsuarioView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(UsuarioView.this, "Error al guardar usuario");
                }
            }
        };
        worker.execute();
    }
    
    private void updateUsuario() {
        if (!validateForm() || idUsuarioSeleccionado == null) return;
        
        UsuarioDTO dto = UsuarioDTO.builder()
            .idUsuario(idUsuarioSeleccionado)
            .nombre(txtNombre.getText().trim())
            .username(txtUsername.getText().trim())
            .tipo((String) cmbTipo.getSelectedItem())
            .email(txtEmail.getText().trim())
            .telefono(txtTelefono.getText().trim())
            .direccion(txtDireccion.getText().trim())
            .estado((String) cmbEstado.getSelectedItem())
            .build();
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return controller.actualizarUsuario(dto);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(UsuarioView.this, "Usuario actualizado exitosamente");
                        clearForm();
                        loadData();
                    } else {
                        ComponentFactory.showError(UsuarioView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(UsuarioView.this, "Error al actualizar usuario");
                }
            }
        };
        worker.execute();
    }
    
    private void deleteUsuario() {
        if (idUsuarioSeleccionado == null) return;
        
        if (!ComponentFactory.showConfirm(this, "¿Está seguro de eliminar este usuario?")) {
            return;
        }
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return controller.eliminarUsuario(idUsuarioSeleccionado);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(UsuarioView.this, "Usuario eliminado exitosamente");
                        clearForm();
                        loadData();
                    } else {
                        ComponentFactory.showError(UsuarioView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(UsuarioView.this, "Error al eliminar usuario");
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
        if (txtUsername.getText().trim().isEmpty()) {
            ComponentFactory.showWarning(this, "El username es obligatorio");
            return false;
        }
        if (idUsuarioSeleccionado == null && txtPassword.getPassword().length == 0) {
            ComponentFactory.showWarning(this, "El password es obligatorio para nuevos usuarios");
            return false;
        }
        return true;
    }
    
    private void clearForm() {
        idUsuarioSeleccionado = null;
        txtNombre.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        cmbTipo.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        table.clearSelection();
        
        btnGuardar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}
