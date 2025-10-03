package com.biblioteca.view;

import com.biblioteca.controller.EjemplarController;
import com.biblioteca.controller.LibroController;
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

public class EjemplarView extends JPanel {
    private EjemplarController ejemplarController;
    private LibroController libroController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtCodigo, txtUbicacion, txtBuscarCodigo;
    private JTextArea txtObservaciones;
    private JComboBox<String> cmbLibro, cmbEstado, cmbEstadoFisico;
    private JDateChooser dateAdquisicion;
    private JButton btnGuardar, btnNuevo, btnEditar, btnEliminar, btnCambiarEstado;
    private Long idEjemplarSeleccionado;
    
    public EjemplarView() {
        this.ejemplarController = new EjemplarController();
        this.libroController = new LibroController();
        initComponents();
        loadLibros();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        JLabel lblTitle = ComponentFactory.createTitleLabel("Gestión de Ejemplares");
        headerPanel.add(lblTitle, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(750);
        splitPane.setBorder(null);
        
        splitPane.setLeftComponent(createTablePanel());
        splitPane.setRightComponent(createFormPanel());
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createTablePanel() {
        JPanel panel = ComponentFactory.createCard();
        panel.setLayout(new BorderLayout(10, 10));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(Color.WHITE);
        
        JLabel lblBuscar = ComponentFactory.createLabel("Buscar por código:");
        txtBuscarCodigo = ComponentFactory.createTextField();
        txtBuscarCodigo.setPreferredSize(new Dimension(200, UIConstants.INPUT_HEIGHT));
        
        JButton btnBuscar = ComponentFactory.createPrimaryButton("🔍 Buscar");
        btnBuscar.addActionListener(e -> buscarPorCodigo());
        
        JButton btnMostrarTodos = ComponentFactory.createSecondaryButton("Ver Todos");
        btnMostrarTodos.addActionListener(e -> {
            txtBuscarCodigo.setText("");
            loadData();
        });
        
        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscarCodigo);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnMostrarTodos);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Código", "Libro", "Ubicación", "Estado", "Estado Físico", "Fecha Adquisición"};
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
                loadSelectedEjemplar();
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
        
        JLabel lblFormTitle = ComponentFactory.createSubtitleLabel("Datos del Ejemplar");
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
        fieldsPanel.add(ComponentFactory.createLabel("Libro:"), gbc);
        gbc.gridy = row++;
        cmbLibro = ComponentFactory.createComboBox(new String[]{});
        fieldsPanel.add(cmbLibro, gbc);
        
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Código:"), gbc);
        gbc.gridy = row++;
        txtCodigo = ComponentFactory.createTextField();
        fieldsPanel.add(txtCodigo, gbc);
        
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Ubicación:"), gbc);
        gbc.gridy = row++;
        txtUbicacion = ComponentFactory.createTextField();
        fieldsPanel.add(txtUbicacion, gbc);
        
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Estado:"), gbc);
        gbc.gridy = row++;
        cmbEstado = ComponentFactory.createComboBox(new String[]{"DISPONIBLE", "PRESTADO", "RESERVADO", "MANTENIMIENTO", "BAJA"});
        fieldsPanel.add(cmbEstado, gbc);
        
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Estado Físico:"), gbc);
        gbc.gridy = row++;
        cmbEstadoFisico = ComponentFactory.createComboBox(new String[]{"EXCELENTE", "BUENO", "REGULAR", "MALO"});
        fieldsPanel.add(cmbEstadoFisico, gbc);
        
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Fecha Adquisición:"), gbc);
        gbc.gridy = row++;
        dateAdquisicion = new JDateChooser();
        dateAdquisicion.setFont(UIConstants.NORMAL_FONT);
        dateAdquisicion.setPreferredSize(new Dimension(250, UIConstants.INPUT_HEIGHT));
        fieldsPanel.add(dateAdquisicion, gbc);
        
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Observaciones:"), gbc);
        gbc.gridy = row++;
        txtObservaciones = ComponentFactory.createTextArea(3, 20);
        JScrollPane scrollObs = new JScrollPane(txtObservaciones);
        scrollObs.setPreferredSize(new Dimension(250, 80));
        fieldsPanel.add(scrollObs, gbc);
        
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
        btnGuardar.addActionListener(e -> saveEjemplar());
        
        btnEditar = ComponentFactory.createPrimaryButton("Editar");
        btnEditar.addActionListener(e -> updateEjemplar());
        btnEditar.setEnabled(false);
        
        btnEliminar = ComponentFactory.createDangerButton("Eliminar");
        btnEliminar.addActionListener(e -> deleteEjemplar());
        btnEliminar.setEnabled(false);
        
        buttonPanel.add(btnNuevo);
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    @SuppressWarnings("unchecked")
    private void loadLibros() {
        SwingWorker<List<LibroDTO>, Void> worker = new SwingWorker<>() {
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
                        cmbLibro.removeAllItems();
                        for (LibroDTO libro : libros) {
                            cmbLibro.addItem(libro.getIdLibro() + " - " + libro.getTitulo());
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    @SuppressWarnings("unchecked")
    private void loadData() {
        tableModel.setRowCount(0);
        
        SwingWorker<List<EjemplarDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<EjemplarDTO> doInBackground() {
                Map<String, Object> response = ejemplarController.listarEjemplares();
                if ((Boolean) response.get("success")) {
                    return (List<EjemplarDTO>) response.get("data");
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    List<EjemplarDTO> ejemplares = get();
                    if (ejemplares != null) {
                        for (EjemplarDTO ej : ejemplares) {
                            tableModel.addRow(new Object[]{
                                ej.getIdEjemplar(),
                                ej.getCodigoEjemplar(),
                                ej.getTituloLibro(),
                                ej.getUbicacion(),
                                ej.getEstado(),
                                ej.getEstadoFisico(),
                                ej.getFechaAdquisicion()
                            });
                        }
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(EjemplarView.this, "Error al cargar ejemplares");
                }
            }
        };
        worker.execute();
    }
    
    private void buscarPorCodigo() {
        String codigo = txtBuscarCodigo.getText().trim();
        if (codigo.isEmpty()) {
            ComponentFactory.showWarning(this, "Ingrese un código para buscar");
            return;
        }
        
        SwingWorker<EjemplarDTO, Void> worker = new SwingWorker<>() {
            @Override
            protected EjemplarDTO doInBackground() {
                Map<String, Object> response = ejemplarController.buscarEjemplarPorCodigo(codigo);
                if ((Boolean) response.get("success")) {
                    return (EjemplarDTO) response.get("data");
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    EjemplarDTO ejemplar = get();
                    if (ejemplar != null) {
                        tableModel.setRowCount(0);
                        tableModel.addRow(new Object[]{
                            ejemplar.getIdEjemplar(),
                            ejemplar.getCodigoEjemplar(),
                            ejemplar.getTituloLibro(),
                            ejemplar.getUbicacion(),
                            ejemplar.getEstado(),
                            ejemplar.getEstadoFisico(),
                            ejemplar.getFechaAdquisicion()
                        });
                    } else {
                        ComponentFactory.showWarning(EjemplarView.this, "No se encontró el ejemplar");
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(EjemplarView.this, "Error en la búsqueda");
                }
            }
        };
        worker.execute();
    }
    
    private void loadSelectedEjemplar() {
        int row = table.getSelectedRow();
        if (row != -1) {
            idEjemplarSeleccionado = (Long) tableModel.getValueAt(row, 0);
            
            SwingWorker<EjemplarDTO, Void> worker = new SwingWorker<>() {
                @Override
                protected EjemplarDTO doInBackground() {
                    Map<String, Object> response = ejemplarController.obtenerEjemplar(idEjemplarSeleccionado);
                    if ((Boolean) response.get("success")) {
                        return (EjemplarDTO) response.get("data");
                    }
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        EjemplarDTO ejemplar = get();
                        if (ejemplar != null) {
                            txtCodigo.setText(ejemplar.getCodigoEjemplar());
                            txtUbicacion.setText(ejemplar.getUbicacion());
                            txtObservaciones.setText(ejemplar.getObservaciones());
                            cmbEstado.setSelectedItem(ejemplar.getEstado());
                            cmbEstadoFisico.setSelectedItem(ejemplar.getEstadoFisico());
                            
                            if (ejemplar.getFechaAdquisicion() != null) {
                                dateAdquisicion.setDate(java.sql.Date.valueOf(ejemplar.getFechaAdquisicion()));
                            }
                            
                            for (int i = 0; i < cmbLibro.getItemCount(); i++) {
                                String item = cmbLibro.getItemAt(i);
                                if (item.startsWith(ejemplar.getIdLibro() + " -")) {
                                    cmbLibro.setSelectedIndex(i);
                                    break;
                                }
                            }
                            
                            btnEditar.setEnabled(true);
                            btnEliminar.setEnabled(true);
                            btnGuardar.setEnabled(false);
                        }
                    } catch (Exception ex) {
                        ComponentFactory.showError(EjemplarView.this, "Error al cargar datos del ejemplar");
                    }
                }
            };
            worker.execute();
        }
    }
    
    private void saveEjemplar() {
        if (!validateForm()) return;
        
        EjemplarDTO dto = buildEjemplarDTO();
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return ejemplarController.crearEjemplar(dto);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(EjemplarView.this, "Ejemplar creado exitosamente");
                        clearForm();
                        loadData();
                    } else {
                        ComponentFactory.showError(EjemplarView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(EjemplarView.this, "Error al guardar ejemplar");
                }
            }
        };
        worker.execute();
    }
    
    private void updateEjemplar() {
        if (!validateForm() || idEjemplarSeleccionado == null) return;
        
        EjemplarDTO dto = buildEjemplarDTO();
        dto.setIdEjemplar(idEjemplarSeleccionado);
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return ejemplarController.actualizarEjemplar(dto);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(EjemplarView.this, "Ejemplar actualizado exitosamente");
                        clearForm();
                        loadData();
                    } else {
                        ComponentFactory.showError(EjemplarView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(EjemplarView.this, "Error al actualizar ejemplar");
                }
            }
        };
        worker.execute();
    }
    
    private void deleteEjemplar() {
        if (idEjemplarSeleccionado == null) return;
        
        if (!ComponentFactory.showConfirm(this, "¿Está seguro de eliminar este ejemplar?")) {
            return;
        }
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return ejemplarController.eliminarEjemplar(idEjemplarSeleccionado);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(EjemplarView.this, "Ejemplar eliminado exitosamente");
                        clearForm();
                        loadData();
                    } else {
                        ComponentFactory.showError(EjemplarView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(EjemplarView.this, "Error al eliminar ejemplar");
                }
            }
        };
        worker.execute();
    }
    
    private EjemplarDTO buildEjemplarDTO() {
        String libroStr = (String) cmbLibro.getSelectedItem();
        Long idLibro = Long.parseLong(libroStr.split(" - ")[0]);
        
        LocalDate fechaAdq = null;
        if (dateAdquisicion.getDate() != null) {
            fechaAdq = dateAdquisicion.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        }
        
        return EjemplarDTO.builder()
            .idLibro(idLibro)
            .codigoEjemplar(txtCodigo.getText().trim())
            .ubicacion(txtUbicacion.getText().trim())
            .estado((String) cmbEstado.getSelectedItem())
            .estadoFisico((String) cmbEstadoFisico.getSelectedItem())
            .fechaAdquisicion(fechaAdq)
            .observaciones(txtObservaciones.getText().trim())
            .build();
    }
    
    private boolean validateForm() {
        if (cmbLibro.getSelectedItem() == null) {
            ComponentFactory.showWarning(this, "Debe seleccionar un libro");
            return false;
        }
        if (txtCodigo.getText().trim().isEmpty()) {
            ComponentFactory.showWarning(this, "El código es obligatorio");
            return false;
        }
        return true;
    }
    
    private void clearForm() {
        idEjemplarSeleccionado = null;
        txtCodigo.setText("");
        txtUbicacion.setText("");
        txtObservaciones.setText("");
        dateAdquisicion.setDate(null);
        cmbEstado.setSelectedIndex(0);
        cmbEstadoFisico.setSelectedIndex(0);
        if (cmbLibro.getItemCount() > 0) {
            cmbLibro.setSelectedIndex(0);
        }
        table.clearSelection();
        
        btnGuardar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}
