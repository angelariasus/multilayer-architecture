package com.biblioteca.view;

import com.biblioteca.controller.MultaController;
import com.biblioteca.model.Multa;
import com.biblioteca.util.UIConstants;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MultaView extends JFrame {
    private MultaController multaController;
    private JTable multasTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextField usuarioIdField, montoField;
    private JComboBox<String> estadoComboBox;
    private JButton addButton, updateButton, deleteButton, clearButton, refreshButton;
    private JButton pagarButton;
    private TableRowSorter<DefaultTableModel> sorter;

    public MultaView() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.multaController = new MultaController();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadMultas();
        
        setTitle("Sistema de Biblioteca - Gestión de Multas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);
    }

    private void initializeComponents() {
        String[] columnNames = {"ID", "Usuario ID", "Monto", "Pagado", "Fecha Multa"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        multasTable = new JTable(tableModel);
        multasTable.setFont(UIConstants.BODY_FONT);
        multasTable.setRowHeight(35);
        multasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        multasTable.getTableHeader().setFont(UIConstants.HEADER_FONT);
        multasTable.getTableHeader().setBackground(UIConstants.BACKGROUND_COLOR);
        multasTable.setGridColor(new Color(229, 231, 235));
        
        sorter = new TableRowSorter<>(tableModel);
        multasTable.setRowSorter(sorter);
        
        searchField = UIConstants.createStyledTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        
        usuarioIdField = UIConstants.createStyledTextField();
        montoField = UIConstants.createStyledTextField();
        
        // ComboBox con estilo
        estadoComboBox = new JComboBox<>(new String[]{"N", "S"});
        estadoComboBox.setFont(UIConstants.BODY_FONT);
        estadoComboBox.setPreferredSize(new Dimension(0, 45));
        
        addButton = UIConstants.createPrimaryButton("Crear Multa");
        updateButton = UIConstants.createStyledButton("Actualizar", UIConstants.WARNING_COLOR);
        deleteButton = UIConstants.createStyledButton("Eliminar", UIConstants.ERROR_COLOR);
        clearButton = UIConstants.createSecondaryButton("Limpiar");
        refreshButton = UIConstants.createSecondaryButton("Actualizar Lista");
        
        // Botones específicos de multas
        pagarButton = UIConstants.createStyledButton("Pagar", UIConstants.SUCCESS_COLOR);
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
        
        JLabel iconLabel = new JLabel("💰");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        titlePanel.add(iconLabel);
        
        JLabel titleLabel = UIConstants.createSubtitleLabel("Gestión de Multas");
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
        
        JScrollPane scrollPane = new JScrollPane(multasTable);
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
        JLabel formTitle = UIConstants.createHeaderLabel("Información de la Multa");
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, UIConstants.PADDING_MEDIUM, 0);
        formCard.add(formTitle, gbc);
        
        // Campos del formulario
        gbc.gridwidth = 1;
        gbc.insets = new Insets(UIConstants.PADDING_SMALL, 0, UIConstants.PADDING_SMALL, 0);
        
        // Usuario ID
        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(UIConstants.createBodyLabel("ID Usuario:"), gbc);
        gbc.gridy = 2;
        formCard.add(usuarioIdField, gbc);
        
        // Monto
        gbc.gridy = 3;
        formCard.add(UIConstants.createBodyLabel("Monto:"), gbc);
        gbc.gridy = 4;
        formCard.add(montoField, gbc);
        
        // Estado
        gbc.gridy = 5;
        formCard.add(UIConstants.createBodyLabel("Estado:"), gbc);
        gbc.gridy = 6;
        formCard.add(estadoComboBox, gbc);
        
        // Panel de botones principales
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL));
        buttonPanel.setBackground(UIConstants.CARD_BACKGROUND);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(UIConstants.PADDING_MEDIUM, 0, UIConstants.PADDING_SMALL, 0);
        formCard.add(buttonPanel, gbc);
        
        // Panel de acciones específicas
        JPanel actionPanel = new JPanel(new GridLayout(1, 1, UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL));
        actionPanel.setBackground(UIConstants.CARD_BACKGROUND);
        actionPanel.add(pagarButton);
        
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 0, 0);
        formCard.add(actionPanel, gbc);
        
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
        multasTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedMulta();
            }
        });
        
        // Botones principales
        addButton.addActionListener(e -> addMulta());
        updateButton.addActionListener(e -> updateMulta());
        deleteButton.addActionListener(e -> deleteMulta());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadMultas());
        
        // Botones específicos
        pagarButton.addActionListener(e -> pagarMulta());
    }


    private void loadMultas() {
        try {
            List<Multa> multas = multaController.listarMultas();
            tableModel.setRowCount(0);
            
            for (Multa multa : multas) {
                Object[] row = {
                    multa.getIdMulta(),
                    multa.getIdUsuario(),
                    multa.getMonto(),
                    multa.getPagado(),
                    multa.getFechaMulta()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            showErrorMessage("Error al cargar multas: " + e.getMessage());
        }
    }

    private void loadSelectedMulta() {
        int selectedRow = multasTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = multasTable.convertRowIndexToModel(selectedRow);
            usuarioIdField.setText(tableModel.getValueAt(modelRow, 1).toString());
            montoField.setText(tableModel.getValueAt(modelRow, 2).toString());
            estadoComboBox.setSelectedItem(tableModel.getValueAt(modelRow, 3).toString());
        }
    }

    private void addMulta() {
        if (!validateFields()) return;
        
        try {
            int idUsuario = Integer.parseInt(usuarioIdField.getText().trim());
            double monto = Double.parseDouble(montoField.getText().trim());
            
            multaController.registrarMulta(idUsuario, monto);
            showSuccessMessage("Multa creada exitosamente");
            loadMultas();
            clearFields();
        } catch (Exception e) {
            showErrorMessage("Error al crear multa: " + e.getMessage());
        }
    }

    private void updateMulta() {
        int selectedRow = multasTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarningMessage("Seleccione una multa para actualizar");
            return;
        }
        
        showWarningMessage("La actualización de multas no está disponible en el controlador actual");
    }

    private void deleteMulta() {
        int selectedRow = multasTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarningMessage("Seleccione una multa para eliminar");
            return;
        }
        
        showWarningMessage("La eliminación de multas no está disponible en el controlador actual");
    }

    private void pagarMulta() {
        int selectedRow = multasTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarningMessage("Seleccione una multa para pagar");
            return;
        }
        
        try {
            int modelRow = multasTable.convertRowIndexToModel(selectedRow);
            Integer id = (Integer) tableModel.getValueAt(modelRow, 0);
            
            multaController.pagarMulta(id);
            showSuccessMessage("Multa pagada exitosamente");
            loadMultas();
            clearFields();
        } catch (Exception e) {
            showErrorMessage("Error al pagar multa: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (usuarioIdField.getText().trim().isEmpty()) {
            showWarningMessage("El ID del usuario es requerido");
            usuarioIdField.requestFocus();
            return false;
        }
        if (montoField.getText().trim().isEmpty()) {
            showWarningMessage("El monto es requerido");
            montoField.requestFocus();
            return false;
        }
        try {
            Integer.parseInt(usuarioIdField.getText().trim());
            Double.parseDouble(montoField.getText().trim());
        } catch (Exception e) {
            showWarningMessage("Verifique que el ID sea un número válido y el monto sea decimal");
            return false;
        }
        return true;
    }

    private void clearFields() {
        usuarioIdField.setText("");
        montoField.setText("");
        estadoComboBox.setSelectedIndex(0);
        multasTable.clearSelection();
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
