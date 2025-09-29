package com.biblioteca.view;

import com.biblioteca.controller.UsuarioController;
import com.biblioteca.model.Usuario;
import com.biblioteca.util.UIConstants;
import com.biblioteca.util.Validador;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UsuarioView extends JFrame {
    private UsuarioController usuarioController;
    private JTable usuariosTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextField nombreField, tipoField, usernameField;
    private JButton addButton, updateButton, deleteButton, clearButton, refreshButton;
    private TableRowSorter<DefaultTableModel> sorter;

    public UsuarioView() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.usuarioController = new UsuarioController();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadUsuarios();
        
        setTitle("Sistema de Biblioteca - Gestión de Usuarios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);
    }

    private void initializeComponents() {
        String[] columnNames = {"ID", "Nombre", "Tipo", "Estado", "Username"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        usuariosTable = new JTable(tableModel);
        usuariosTable.setFont(UIConstants.BODY_FONT);
        usuariosTable.setRowHeight(35);
        usuariosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        usuariosTable.getTableHeader().setFont(UIConstants.HEADER_FONT);
        usuariosTable.getTableHeader().setBackground(UIConstants.BACKGROUND_COLOR);
        usuariosTable.setGridColor(new Color(229, 231, 235));
        
        sorter = new TableRowSorter<>(tableModel);
        usuariosTable.setRowSorter(sorter);
        
        searchField = UIConstants.createStyledTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        
        nombreField = UIConstants.createStyledTextField();
        tipoField = UIConstants.createStyledTextField(); // será usado para tipo
        usernameField = UIConstants.createStyledTextField(); // será usado para username
        
        addButton = UIConstants.createPrimaryButton("Agregar Usuario");
        updateButton = UIConstants.createStyledButton("Actualizar", UIConstants.WARNING_COLOR);
        deleteButton = UIConstants.createStyledButton("Eliminar", UIConstants.ERROR_COLOR);
        clearButton = UIConstants.createSecondaryButton("Limpiar");
        refreshButton = UIConstants.createSecondaryButton("Actualizar Lista");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(900);
        splitPane.setDividerSize(1);
        splitPane.setBorder(null);
        
        // Panel de tabla
        JPanel tablePanel = createTablePanel();
        splitPane.setLeftComponent(tablePanel);
        
        // Panel de formulario
        JPanel formPanel = createFormPanel();
        splitPane.setRightComponent(formPanel);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
            BorderFactory.createEmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM)
        ));
        
        // Título
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel("👥");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        titlePanel.add(iconLabel);
        
        JLabel titleLabel = UIConstants.createSubtitleLabel("Gestión de Usuarios");
        titlePanel.add(titleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        
        JLabel searchLabel = UIConstants.createBodyLabel("Buscar:");
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(refreshButton);
        
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UIConstants.BACKGROUND_COLOR);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_SMALL));
        
        JScrollPane scrollPane = new JScrollPane(usuariosTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(UIConstants.PADDING_MEDIUM, UIConstants.PADDING_SMALL, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM));
        
        JPanel formCard = UIConstants.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(UIConstants.PADDING_SMALL, 0, UIConstants.PADDING_SMALL, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Título del formulario
        JLabel formTitle = UIConstants.createHeaderLabel("Información del Usuario");
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, UIConstants.PADDING_MEDIUM, 0);
        formCard.add(formTitle, gbc);
        
        // Campos del formulario
        gbc.gridwidth = 1;
        gbc.insets = new Insets(UIConstants.PADDING_SMALL, 0, UIConstants.PADDING_SMALL, 0);
        
        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(UIConstants.createBodyLabel("Nombre:"), gbc);
        gbc.gridy = 2;
        formCard.add(nombreField, gbc);
        
        // Tipo (antes Email)
        gbc.gridy = 3;
        formCard.add(UIConstants.createBodyLabel("Tipo:"), gbc);
        gbc.gridy = 4;
        formCard.add(tipoField, gbc);
        
        // Username (antes Teléfono)
        gbc.gridy = 5;
        formCard.add(UIConstants.createBodyLabel("Username:"), gbc);
        gbc.gridy = 6;
        formCard.add(usernameField, gbc);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL));
        buttonPanel.setBackground(UIConstants.CARD_BACKGROUND);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(UIConstants.PADDING_MEDIUM, 0, 0, 0);
        formCard.add(buttonPanel, gbc);
        
        formPanel.add(formCard);
        formPanel.add(Box.createVerticalGlue());
        
        return formPanel;
    }

    private void setupEventListeners() {
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });
        
        // Selección de tabla
        usuariosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedUsuario();
            }
        });
        
        // Botones
        addButton.addActionListener(e -> addUsuario());
        updateButton.addActionListener(e -> updateUsuario());
        deleteButton.addActionListener(e -> deleteUsuario());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadUsuarios());
    }


    private void loadUsuarios() {
        try {
            List<Usuario> usuarios = usuarioController.listarUsuarios();
            tableModel.setRowCount(0);
            
            for (Usuario usuario : usuarios) {
                Object[] row = {
                    usuario.getIdUsuario(),
                    usuario.getNombre(),
                    usuario.getTipo(),
                    usuario.getEstado(),
                    usuario.getUsername()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            showErrorMessage("Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void loadSelectedUsuario() {
        int selectedRow = usuariosTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = usuariosTable.convertRowIndexToModel(selectedRow);
            nombreField.setText(tableModel.getValueAt(modelRow, 1).toString());
            tipoField.setText(tableModel.getValueAt(modelRow, 2).toString()); // tipo
            usernameField.setText(tableModel.getValueAt(modelRow, 3).toString()); // estado
        }
    }

    private void addUsuario() {
        if (!validateFields()) return;
        
        try {
            usuarioController.registrarUsuario(
                nombreField.getText().trim(),
                tipoField.getText().trim(), // usando como tipo
                usernameField.getText().trim(), // usando como username
                "password123" // password por defecto
            );
            showSuccessMessage("Usuario agregado exitosamente");
            loadUsuarios();
            clearFields();
        } catch (Exception e) {
            showErrorMessage("Error al agregar usuario: " + e.getMessage());
        }
    }

    private void updateUsuario() {
        int selectedRow = usuariosTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarningMessage("Seleccione un usuario para actualizar");
            return;
        }
        
        if (!validateFields()) return;
        
        try {
            int modelRow = usuariosTable.convertRowIndexToModel(selectedRow);
            Integer id = (Integer) tableModel.getValueAt(modelRow, 0);
            
            usuarioController.activarUsuario(id);
            showSuccessMessage("Usuario actualizado exitosamente");
            loadUsuarios();
            clearFields();
        } catch (Exception e) {
            showErrorMessage("Error al actualizar usuario: " + e.getMessage());
        }
    }

    private void deleteUsuario() {
        int selectedRow = usuariosTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarningMessage("Seleccione un usuario para eliminar");
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar este usuario?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (option == JOptionPane.YES_OPTION) {
            try {
                int modelRow = usuariosTable.convertRowIndexToModel(selectedRow);
                Integer id = (Integer) tableModel.getValueAt(modelRow, 0);
                
                usuarioController.eliminarUsuario(id);
                showSuccessMessage("Usuario eliminado exitosamente");
                loadUsuarios();
                clearFields();
            } catch (Exception e) {
                showErrorMessage("Error al eliminar usuario: " + e.getMessage());
            }
        }
    }

    private boolean validateFields() {
        if (!Validador.esVacio(nombreField.getText())) {
            showWarningMessage("El nombre es requerido");
            nombreField.requestFocus();
            return false;
        }
        if (!Validador.esVacio(tipoField.getText())) {
            showWarningMessage("El tipo es requerido");
            tipoField.requestFocus();
            return false;
        }
        if (!Validador.esVacio(usernameField.getText())) {
            showWarningMessage("El username es requerido");
            usernameField.requestFocus();
            return false;
        }
        return true;
    }

    private void clearFields() {
        nombreField.setText("");
        tipoField.setText("");
        usernameField.setText("");
        usuariosTable.clearSelection();
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }
}
