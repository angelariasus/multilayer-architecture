package com.biblioteca.view;

import com.biblioteca.controller.ReservaController;
import com.biblioteca.model.Reserva;
import com.biblioteca.util.UIConstants;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReservaView extends JFrame {
    private ReservaController reservaController;
    private JTable reservasTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextField usuarioIdField, ejemplarIdField;
    private JButton addButton, clearButton, refreshButton;
    private TableRowSorter<DefaultTableModel> sorter;

    public ReservaView() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.reservaController = new ReservaController();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadReservas();
        
        setTitle("Sistema de Biblioteca - Gestión de Reservas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);
    }

    private void initializeComponents() {
        String[] columnNames = {"ID Reserva", "ID Usuario", "ID Ejemplar", "Fecha Reserva"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        reservasTable = new JTable(tableModel);
        reservasTable.setFont(UIConstants.BODY_FONT);
        reservasTable.setRowHeight(35);
        reservasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reservasTable.getTableHeader().setFont(UIConstants.HEADER_FONT);
        reservasTable.getTableHeader().setBackground(UIConstants.BACKGROUND_COLOR);
        reservasTable.setGridColor(new Color(229, 231, 235));
        
        sorter = new TableRowSorter<>(tableModel);
        reservasTable.setRowSorter(sorter);
        
        searchField = UIConstants.createStyledTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        
        usuarioIdField = UIConstants.createStyledTextField();
        ejemplarIdField = UIConstants.createStyledTextField();
        
        addButton = UIConstants.createPrimaryButton("Crear Reserva");
        clearButton = UIConstants.createSecondaryButton("Limpiar");
        refreshButton = UIConstants.createSecondaryButton("Actualizar Lista");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);
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
        
        JLabel iconLabel = new JLabel("📅");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        titlePanel.add(iconLabel);
        
        JLabel titleLabel = UIConstants.createSubtitleLabel("Gestión de Reservas");
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
        
        JScrollPane scrollPane = new JScrollPane(reservasTable);
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
        JLabel formTitle = UIConstants.createHeaderLabel("Nueva Reserva");
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
        
        // Ejemplar ID
        gbc.gridy = 3;
        formCard.add(UIConstants.createBodyLabel("ID Ejemplar:"), gbc);
        gbc.gridy = 4;
        formCard.add(ejemplarIdField, gbc);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL));
        buttonPanel.setBackground(UIConstants.CARD_BACKGROUND);
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(UIConstants.PADDING_MEDIUM, 0, UIConstants.PADDING_SMALL, 0);
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
        reservasTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedReserva();
            }
        });
        
        // Botones
        addButton.addActionListener(e -> addReserva());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadReservas());
    }

    private void loadReservas() {
        try {
            List<Reserva> reservas = reservaController.listarReservas();
            tableModel.setRowCount(0);
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            for (Reserva reserva : reservas) {
                Object[] row = {
                    reserva.getIdReserva(),
                    reserva.getIdUsuario(),
                    reserva.getIdEjemplar(),
                    dateFormat.format(reserva.getFechaReserva())
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            showErrorMessage("Error al cargar reservas: " + e.getMessage());
        }
    }

    private void loadSelectedReserva() {
        int selectedRow = reservasTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = reservasTable.convertRowIndexToModel(selectedRow);
            usuarioIdField.setText(tableModel.getValueAt(modelRow, 1).toString());
            ejemplarIdField.setText(tableModel.getValueAt(modelRow, 2).toString());
        }
    }

    private void addReserva() {
        if (!validateFields()) return;
        
        try {
            int idUsuario = Integer.parseInt(usuarioIdField.getText().trim());
            int idEjemplar = Integer.parseInt(ejemplarIdField.getText().trim());
            
            reservaController.registrarReserva(idUsuario, idEjemplar);
            showSuccessMessage("Reserva creada exitosamente");
            loadReservas();
            clearFields();
        } catch (Exception e) {
            showErrorMessage("Error al crear reserva: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (usuarioIdField.getText().trim().isEmpty()) {
            showWarningMessage("El ID del usuario es requerido");
            usuarioIdField.requestFocus();
            return false;
        }
        if (ejemplarIdField.getText().trim().isEmpty()) {
            showWarningMessage("El ID del ejemplar es requerido");
            ejemplarIdField.requestFocus();
            return false;
        }
        try {
            Integer.parseInt(usuarioIdField.getText().trim());
            Integer.parseInt(ejemplarIdField.getText().trim());
        } catch (NumberFormatException e) {
            showWarningMessage("Los IDs deben ser números válidos");
            return false;
        }
        return true;
    }

    private void clearFields() {
        usuarioIdField.setText("");
        ejemplarIdField.setText("");
        reservasTable.clearSelection();
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
