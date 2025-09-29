package com.biblioteca.view;

import com.biblioteca.controller.PrestamoController;
import com.biblioteca.model.Prestamo;
import com.biblioteca.util.UIConstants;
import com.biblioteca.util.Fechas;
import com.biblioteca.util.Validador;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class PrestamoView extends JFrame {
    private PrestamoController prestamoController;
    private JTable prestamosTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextField usuarioIdField, ejemplarIdField;
    private JTextField fechaPrestamoField, fechaDevolucionField;
    private JComboBox<String> estadoComboBox;
    private JButton addButton, updateButton, deleteButton, clearButton, refreshButton;
    private JButton devolverButton;
    private TableRowSorter<DefaultTableModel> sorter;

    public PrestamoView() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.prestamoController = new PrestamoController();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadPrestamos();
        
        setTitle("Sistema de Biblioteca - Gestión de Préstamos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1500, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);
    }

    private void initializeComponents() {
        String[] columnNames = {"ID", "Usuario ID", "Ejemplar ID", "Fecha Préstamo", "Fecha Devolución", "Devuelto"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        prestamosTable = new JTable(tableModel);
        prestamosTable.setFont(UIConstants.BODY_FONT);
        prestamosTable.setRowHeight(35);
        prestamosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        prestamosTable.getTableHeader().setFont(UIConstants.HEADER_FONT);
        prestamosTable.getTableHeader().setBackground(UIConstants.BACKGROUND_COLOR);
        prestamosTable.setGridColor(new Color(229, 231, 235));
        
        sorter = new TableRowSorter<>(tableModel);
        prestamosTable.setRowSorter(sorter);
        
        searchField = UIConstants.createStyledTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        
        usuarioIdField = UIConstants.createStyledTextField();
        ejemplarIdField = UIConstants.createStyledTextField();
        fechaPrestamoField = UIConstants.createStyledTextField();
        fechaDevolucionField = UIConstants.createStyledTextField();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fechaPrestamoField.setText(sdf.format(new Date()));
        
        estadoComboBox = new JComboBox<>(new String[]{"N", "S"});
        estadoComboBox.setFont(UIConstants.BODY_FONT);
        estadoComboBox.setPreferredSize(new Dimension(0, 45));
        
        addButton = UIConstants.createPrimaryButton("Crear Préstamo");
        updateButton = UIConstants.createStyledButton("Actualizar", UIConstants.WARNING_COLOR);
        deleteButton = UIConstants.createStyledButton("Eliminar", UIConstants.ERROR_COLOR);
        clearButton = UIConstants.createSecondaryButton("Limpiar");
        refreshButton = UIConstants.createSecondaryButton("Actualizar Lista");
        
        devolverButton = UIConstants.createStyledButton("Devolver", UIConstants.SUCCESS_COLOR);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(950);
        splitPane.setDividerSize(1);
        splitPane.setBorder(null);
        
        JPanel tablePanel = createTablePanel();
        splitPane.setLeftComponent(tablePanel);
        
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
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel("📋");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        titlePanel.add(iconLabel);
        
        JLabel titleLabel = UIConstants.createSubtitleLabel("Gestión de Préstamos");
        titlePanel.add(titleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
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
        
        JScrollPane scrollPane = new JScrollPane(prestamosTable);
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
        
        JLabel formTitle = UIConstants.createHeaderLabel("Información del Préstamo");
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, UIConstants.PADDING_MEDIUM, 0);
        formCard.add(formTitle, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(UIConstants.PADDING_SMALL, 0, UIConstants.PADDING_SMALL, 0);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(UIConstants.createBodyLabel("ID Usuario:"), gbc);
        gbc.gridy = 2;
        formCard.add(usuarioIdField, gbc);
        
        gbc.gridy = 3;
        formCard.add(UIConstants.createBodyLabel("ID Ejemplar:"), gbc);
        gbc.gridy = 4;
        formCard.add(ejemplarIdField, gbc);
        
        gbc.gridy = 5;
        formCard.add(UIConstants.createBodyLabel("Fecha Préstamo (YYYY-MM-DD):"), gbc);
        gbc.gridy = 6;
        formCard.add(fechaPrestamoField, gbc);
        
        gbc.gridy = 7;
        formCard.add(UIConstants.createBodyLabel("Fecha Devolución (YYYY-MM-DD):"), gbc);
        gbc.gridy = 8;
        formCard.add(fechaDevolucionField, gbc);
        
        gbc.gridy = 9;
        formCard.add(UIConstants.createBodyLabel("Estado:"), gbc);
        gbc.gridy = 10;
        formCard.add(estadoComboBox, gbc);
        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL));
        buttonPanel.setBackground(UIConstants.CARD_BACKGROUND);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        gbc.gridy = 11;
        gbc.insets = new Insets(UIConstants.PADDING_MEDIUM, 0, UIConstants.PADDING_SMALL, 0);
        formCard.add(buttonPanel, gbc);
        
        JPanel actionPanel = new JPanel(new GridLayout(1, 2, UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL));
        actionPanel.setBackground(UIConstants.CARD_BACKGROUND);
        actionPanel.add(devolverButton);
        
        gbc.gridy = 12;
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
        
        prestamosTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedPrestamo();
            }
        });
        
        addButton.addActionListener(e -> addPrestamo());
        updateButton.addActionListener(e -> updatePrestamo());
        deleteButton.addActionListener(e -> deletePrestamo());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadPrestamos());
        
        devolverButton.addActionListener(e -> devolverPrestamo());
    }


    private void loadPrestamos() {
        try {
            List<Prestamo> prestamos = prestamoController.listarPrestamos();
            tableModel.setRowCount(0);
            
            for (Prestamo prestamo : prestamos) {
                Object[] row = {
                    prestamo.getIdPrestamo(),
                    prestamo.getIdUsuario(),
                    prestamo.getIdEjemplar(),
                    prestamo.getFechaPrestamo(),
                    prestamo.getFechaDevolucion(),
                    prestamo.getDevuelto()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            showErrorMessage("Error al cargar préstamos: " + e.getMessage());
        }
    }

    private void loadSelectedPrestamo() {
        int selectedRow = prestamosTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = prestamosTable.convertRowIndexToModel(selectedRow);
            usuarioIdField.setText(tableModel.getValueAt(modelRow, 1).toString());
            ejemplarIdField.setText(tableModel.getValueAt(modelRow, 2).toString());
            fechaPrestamoField.setText(tableModel.getValueAt(modelRow, 3).toString());
            fechaDevolucionField.setText(tableModel.getValueAt(modelRow, 4).toString());
            estadoComboBox.setSelectedItem(tableModel.getValueAt(modelRow, 5).toString());
        }
    }

    private void addPrestamo() {
        if (!validateFields()) return;
        
        try {
            int idUsuario = Integer.parseInt(usuarioIdField.getText().trim());
            int idEjemplar = Integer.parseInt(ejemplarIdField.getText().trim());
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaDevolucion = sdf.parse(fechaDevolucionField.getText().trim());
            
            prestamoController.prestarEjemplar(idUsuario, idEjemplar, fechaDevolucion);
            showSuccessMessage("Préstamo creado exitosamente");
            loadPrestamos();
            clearFields();
        } catch (Exception e) {
            showErrorMessage("Error al crear préstamo: " + e.getMessage());
        }
    }

    private void updatePrestamo() {
        int selectedRow = prestamosTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarningMessage("Seleccione un préstamo para actualizar");
            return;
        }
        
        showWarningMessage("La actualización de préstamos no está disponible en el controlador actual");
    }

    private void deletePrestamo() {
        int selectedRow = prestamosTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarningMessage("Seleccione un préstamo para eliminar");
            return;
        }
        
        showWarningMessage("La eliminación de préstamos no está disponible en el controlador actual");
    }

    private void devolverPrestamo() {
        int selectedRow = prestamosTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarningMessage("Seleccione un préstamo para devolver");
            return;
        }
        
        try {
            int modelRow = prestamosTable.convertRowIndexToModel(selectedRow);
            Integer idPrestamo = (Integer) tableModel.getValueAt(modelRow, 0);
            Integer idEjemplar = (Integer) tableModel.getValueAt(modelRow, 2);
            
            prestamoController.devolverEjemplar(idPrestamo, idEjemplar);
            showSuccessMessage("Préstamo devuelto exitosamente");
            loadPrestamos();
            clearFields();
        } catch (Exception e) {
            showErrorMessage("Error al devolver préstamo: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (!Validador.esNumero(usuarioIdField.getText())) {
            showWarningMessage("El ID del usuario debe ser un número válido");
            usuarioIdField.requestFocus();
            return false;
        }
        if (!Validador.esNumero(ejemplarIdField.getText())) {
            showWarningMessage("El ID del ejemplar debe ser un número válido");
            ejemplarIdField.requestFocus();
            return false;
        }
        if (!Fechas.validarFecha(fechaDevolucionField.getText())) {
            showWarningMessage("La fecha de devolución debe tener formato YYYY-MM-DD");
            fechaDevolucionField.requestFocus();
            return false;
        }
        return true;
    }

    private void clearFields() {
        usuarioIdField.setText("");
        ejemplarIdField.setText("");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fechaPrestamoField.setText(sdf.format(new Date()));
        fechaDevolucionField.setText("");
        estadoComboBox.setSelectedIndex(0);
        prestamosTable.clearSelection();
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
