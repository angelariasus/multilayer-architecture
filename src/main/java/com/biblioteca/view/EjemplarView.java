package com.biblioteca.view;

import com.biblioteca.controller.EjemplarController;
import com.biblioteca.model.Ejemplar;
import com.biblioteca.util.UIConstants;
import com.biblioteca.util.Validador;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class EjemplarView extends JFrame {
    private EjemplarController ejemplarController;
    private JTable ejemplaresTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextField tituloField, autorField, isbnField, editorialField, anioField;
    private JComboBox<String> estadoComboBox;
    private JButton addButton, updateButton, deleteButton, clearButton, refreshButton;
    private TableRowSorter<DefaultTableModel> sorter;

    public EjemplarView() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.ejemplarController = new EjemplarController();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadEjemplares();
        
        setTitle("Sistema de Biblioteca - Gestión de Ejemplares");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);
    }

    private void initializeComponents() {
        String[] columnNames = {"ID", "Título", "Autor", "Editorial", "Editorial", "Año", "Estado", "Categoría"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        ejemplaresTable = new JTable(tableModel);
        ejemplaresTable.setFont(UIConstants.BODY_FONT);
        ejemplaresTable.setRowHeight(35);
        ejemplaresTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ejemplaresTable.getTableHeader().setFont(UIConstants.HEADER_FONT);
        ejemplaresTable.getTableHeader().setBackground(UIConstants.BACKGROUND_COLOR);
        ejemplaresTable.setGridColor(new Color(229, 231, 235));
        
        sorter = new TableRowSorter<>(tableModel);
        ejemplaresTable.setRowSorter(sorter);
        
        searchField = UIConstants.createStyledTextField();
        searchField.setPreferredSize(new Dimension(300, 40));
        
        tituloField = UIConstants.createStyledTextField();
        autorField = UIConstants.createStyledTextField();
        isbnField = UIConstants.createStyledTextField(); // será usado para categoría
        editorialField = UIConstants.createStyledTextField();
        anioField = UIConstants.createStyledTextField();
        
        estadoComboBox = new JComboBox<>(new String[]{"Disponible", "Prestado", "Reservado", "Mantenimiento"});
        estadoComboBox.setFont(UIConstants.BODY_FONT);
        estadoComboBox.setPreferredSize(new Dimension(0, 45));
        
        addButton = UIConstants.createPrimaryButton("Agregar Ejemplar");
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
        
        JLabel iconLabel = new JLabel("📖");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        titlePanel.add(iconLabel);
        
        JLabel titleLabel = UIConstants.createSubtitleLabel("Gestión de Ejemplares");
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
        
        JScrollPane scrollPane = new JScrollPane(ejemplaresTable);
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
        
        JLabel formTitle = UIConstants.createHeaderLabel("Información del Ejemplar");
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, UIConstants.PADDING_MEDIUM, 0);
        formCard.add(formTitle, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(UIConstants.PADDING_SMALL, 0, UIConstants.PADDING_SMALL, 0);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(UIConstants.createBodyLabel("Título:"), gbc);
        gbc.gridy = 2;
        formCard.add(tituloField, gbc);
        
        gbc.gridy = 3;
        formCard.add(UIConstants.createBodyLabel("Autor:"), gbc);
        gbc.gridy = 4;
        formCard.add(autorField, gbc);
        
        gbc.gridy = 5;
        formCard.add(UIConstants.createBodyLabel("Categoría:"), gbc); // antes era ISBN
        gbc.gridy = 6;
        formCard.add(isbnField, gbc);

        gbc.gridy = 7;
        formCard.add(UIConstants.createBodyLabel("Editorial:"), gbc);
        gbc.gridy = 8;
        formCard.add(editorialField, gbc);
        
        gbc.gridy = 9;
        formCard.add(UIConstants.createBodyLabel("Año:"), gbc);
        gbc.gridy = 10;
        formCard.add(anioField, gbc);
        
        gbc.gridy = 11;
        formCard.add(UIConstants.createBodyLabel("Estado:"), gbc);
        gbc.gridy = 12;
        formCard.add(estadoComboBox, gbc);
        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, UIConstants.PADDING_SMALL, UIConstants.PADDING_SMALL));
        buttonPanel.setBackground(UIConstants.CARD_BACKGROUND);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        gbc.gridy = 13;
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
        
        ejemplaresTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedEjemplar();
            }
        });
        
        addButton.addActionListener(e -> addEjemplar());
        updateButton.addActionListener(e -> updateEjemplar());
        deleteButton.addActionListener(e -> deleteEjemplar());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadEjemplares());
    }


    private void loadEjemplares() {
        try {
            List<Ejemplar> ejemplares = ejemplarController.listarEjemplares();
            tableModel.setRowCount(0);
            
            for (Ejemplar ejemplar : ejemplares) {
                Object[] row = {
                    ejemplar.getIdEjemplar(),
                    ejemplar.getTitulo(),
                    ejemplar.getAutor(),
                    ejemplar.getEditorial(), // no hay ISBN en el modelo real
                    ejemplar.getEditorial(),
                    ejemplar.getAnio(),
                    ejemplar.getEstado(),
                    ejemplar.getCategoria() // usando categoría en lugar de fecha
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            showErrorMessage("Error al cargar ejemplares: " + e.getMessage());
        }
    }

    private void loadSelectedEjemplar() {
        int selectedRow = ejemplaresTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = ejemplaresTable.convertRowIndexToModel(selectedRow);
            tituloField.setText(tableModel.getValueAt(modelRow, 1).toString());
            autorField.setText(tableModel.getValueAt(modelRow, 2).toString());
            isbnField.setText(tableModel.getValueAt(modelRow, 3).toString()); // editorial
            editorialField.setText(tableModel.getValueAt(modelRow, 4).toString());
            anioField.setText(tableModel.getValueAt(modelRow, 5).toString());
            estadoComboBox.setSelectedItem(tableModel.getValueAt(modelRow, 6).toString());
        }
    }

    private void addEjemplar() {
        if (!validateFields()) return;
        
        try {
            ejemplarController.registrarEjemplar(
                tituloField.getText().trim(),
                autorField.getText().trim(),
                editorialField.getText().trim(),
                Integer.parseInt(anioField.getText().trim()),
                isbnField.getText().trim() // usando como categoría
            );
            showSuccessMessage("Ejemplar agregado exitosamente");
            loadEjemplares();
            clearFields();
        } catch (Exception e) {
            showErrorMessage("Error al agregar ejemplar: " + e.getMessage());
        }
    }

    private void updateEjemplar() {
        int selectedRow = ejemplaresTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarningMessage("Seleccione un ejemplar para actualizar");
            return;
        }
        
        if (!validateFields()) return;
        
        try {
            int modelRow = ejemplaresTable.convertRowIndexToModel(selectedRow);
            Integer id = (Integer) tableModel.getValueAt(modelRow, 0);
            
            ejemplarController.cambiarEstado(id, estadoComboBox.getSelectedItem().toString());
            showSuccessMessage("Ejemplar actualizado exitosamente");
            loadEjemplares();
            clearFields();
        } catch (Exception e) {
            showErrorMessage("Error al actualizar ejemplar: " + e.getMessage());
        }
    }

    private void deleteEjemplar() {
        int selectedRow = ejemplaresTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarningMessage("Seleccione un ejemplar para eliminar");
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar este ejemplar?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (option == JOptionPane.YES_OPTION) {
            try {
                int modelRow = ejemplaresTable.convertRowIndexToModel(selectedRow);
                Integer id = (Integer) tableModel.getValueAt(modelRow, 0);
                
                ejemplarController.eliminarEjemplar(id);
                showSuccessMessage("Ejemplar eliminado exitosamente");
                loadEjemplares();
                clearFields();
            } catch (Exception e) {
                showErrorMessage("Error al eliminar ejemplar: " + e.getMessage());
            }
        }
    }

    private boolean validateFields() {
        if (!Validador.esVacio(tituloField.getText())) {
            showWarningMessage("El título es requerido");
            tituloField.requestFocus();
            return false;
        }
        if (!Validador.esVacio(autorField.getText())) {
            showWarningMessage("El autor es requerido");
            autorField.requestFocus();
            return false;
        }
        if (!Validador.esVacio(isbnField.getText())) {
            showWarningMessage("La categoría es requerida");
            isbnField.requestFocus();
            return false;
        }
        if (!Validador.esVacio(anioField.getText())) {
            showWarningMessage("El año debe ser un número válido");
            anioField.requestFocus();
            return false;
        }
        return true;
    }

    private void clearFields() {
        tituloField.setText("");
        autorField.setText("");
        isbnField.setText("");
        editorialField.setText("");
        anioField.setText("");
        estadoComboBox.setSelectedIndex(0);
        ejemplaresTable.clearSelection();
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
