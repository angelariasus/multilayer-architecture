package com.biblioteca.view;

import com.biblioteca.controller.LibroController;
import com.biblioteca.controller.CategoriaController;
import com.biblioteca.dto.LibroDTO;
import com.biblioteca.dto.CategoriaDTO;
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

public class LibroView extends JPanel {
    private LibroController libroController;
    private CategoriaController categoriaController;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTitulo, txtAutor, txtISBN, txtEditorial, txtUbicacion, txtBuscar;
    private JTextArea txtDescripcion;
    private JComboBox<String> cmbCategoria, cmbEstado;
    private JDateChooser datePublicacion;
    private JButton btnGuardar, btnNuevo, btnEditar, btnEliminar, btnBuscar;
    private Long idLibroSeleccionado;
    
    public LibroView() {
        this.libroController = new LibroController();
        this.categoriaController = new CategoriaController();
        initComponents();
        loadCategorias();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JLabel lblTitle = ComponentFactory.createTitleLabel("Gestión de Libros");
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
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
        
        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(Color.WHITE);
        
        JLabel lblBuscar = ComponentFactory.createLabel("Buscar:");
        txtBuscar = ComponentFactory.createTextField();
        txtBuscar.setPreferredSize(new Dimension(250, UIConstants.INPUT_HEIGHT));
        
        btnBuscar = ComponentFactory.createPrimaryButton("🔍 Buscar");
        btnBuscar.addActionListener(e -> searchLibros());
        
        JButton btnMostrarTodos = ComponentFactory.createSecondaryButton("Ver Todos");
        btnMostrarTodos.addActionListener(e -> {
            txtBuscar.setText("");
            loadData();
        });
        
        JButton btnDisponibles = ComponentFactory.createSuccessButton("Disponibles");
        btnDisponibles.addActionListener(e -> loadDisponibles());
        
        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnMostrarTodos);
        searchPanel.add(btnDisponibles);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Título", "Autor", "ISBN", "Categoría", "Editorial", "Estado", "Disponibles"};
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
                loadSelectedLibro();
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
        
        JLabel lblFormTitle = ComponentFactory.createSubtitleLabel("Datos del Libro");
        lblFormTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(lblFormTitle, BorderLayout.NORTH);
        
        // Form fields in scroll pane
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        
        int row = 0;
        
        // Título
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Título:"), gbc);
        gbc.gridy = row++;
        txtTitulo = ComponentFactory.createTextField();
        fieldsPanel.add(txtTitulo, gbc);
        
        // Autor
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Autor:"), gbc);
        gbc.gridy = row++;
        txtAutor = ComponentFactory.createTextField();
        fieldsPanel.add(txtAutor, gbc);
        
        // ISBN
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("ISBN:"), gbc);
        gbc.gridy = row++;
        txtISBN = ComponentFactory.createTextField();
        fieldsPanel.add(txtISBN, gbc);
        
        // Categoría
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Categoría:"), gbc);
        gbc.gridy = row++;
        cmbCategoria = ComponentFactory.createComboBox(new String[]{});
        fieldsPanel.add(cmbCategoria, gbc);
        
        // Editorial
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Editorial:"), gbc);
        gbc.gridy = row++;
        txtEditorial = ComponentFactory.createTextField();
        fieldsPanel.add(txtEditorial, gbc);
        
        // Fecha Publicación
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Fecha Publicación:"), gbc);
        gbc.gridy = row++;
        datePublicacion = new JDateChooser();
        datePublicacion.setFont(UIConstants.NORMAL_FONT);
        datePublicacion.setPreferredSize(new Dimension(250, UIConstants.INPUT_HEIGHT));
        fieldsPanel.add(datePublicacion, gbc);
        
        // Ubicación
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Ubicación:"), gbc);
        gbc.gridy = row++;
        txtUbicacion = ComponentFactory.createTextField();
        fieldsPanel.add(txtUbicacion, gbc);
        
        // Estado
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Estado:"), gbc);
        gbc.gridy = row++;
        cmbEstado = ComponentFactory.createComboBox(new String[]{"ACTIVO", "INACTIVO"});
        fieldsPanel.add(cmbEstado, gbc);
        
        // Descripción
        gbc.gridy = row++;
        fieldsPanel.add(ComponentFactory.createLabel("Descripción:"), gbc);
        gbc.gridy = row++;
        txtDescripcion = ComponentFactory.createTextArea(3, 20);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(250, 80));
        fieldsPanel.add(scrollDesc, gbc);
        
        JScrollPane scrollFields = new JScrollPane(fieldsPanel);
        scrollFields.setBorder(null);
        scrollFields.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollFields, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        buttonPanel.setBackground(Color.WHITE);
        
        btnNuevo = ComponentFactory.createSecondaryButton("Nuevo");
        btnNuevo.addActionListener(e -> clearForm());
        
        btnGuardar = ComponentFactory.createSuccessButton("Guardar");
        btnGuardar.addActionListener(e -> saveLibro());
        
        btnEditar = ComponentFactory.createPrimaryButton("Editar");
        btnEditar.addActionListener(e -> updateLibro());
        btnEditar.setEnabled(false);
        
        btnEliminar = ComponentFactory.createDangerButton("Eliminar");
        btnEliminar.addActionListener(e -> deleteLibro());
        btnEliminar.setEnabled(false);
        
        buttonPanel.add(btnNuevo);
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    @SuppressWarnings("unchecked")
    private void loadCategorias() {
        SwingWorker<List<CategoriaDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<CategoriaDTO> doInBackground() {
                Map<String, Object> response = categoriaController.listarCategoriasActivas();
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
                        cmbCategoria.removeAllItems();
                        for (CategoriaDTO cat : categorias) {
                            cmbCategoria.addItem(cat.getIdCategoria() + " - " + cat.getNombre());
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
                        for (LibroDTO libro : libros) {
                            tableModel.addRow(new Object[]{
                                libro.getIdLibro(),
                                libro.getTitulo(),
                                libro.getAutor(),
                                libro.getIsbn(),
                                libro.getNombreCategoria(),
                                libro.getEditorial(),
                                libro.getEstado(),
                                libro.getCantidadDisponible() != null ? libro.getCantidadDisponible() : 0
                            });
                        }
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(LibroView.this, "Error al cargar libros");
                }
            }
        };
        worker.execute();
    }
    @SuppressWarnings("unchecked")
    private void searchLibros() {
        String searchTerm = txtBuscar.getText().trim();
        if (searchTerm.isEmpty()) {
            loadData();
            return;
        }
        
        tableModel.setRowCount(0);
        
        SwingWorker<List<LibroDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<LibroDTO> doInBackground() {
                Map<String, Object> response = libroController.buscarLibros(searchTerm);
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
                        for (LibroDTO libro : libros) {
                            tableModel.addRow(new Object[]{
                                libro.getIdLibro(),
                                libro.getTitulo(),
                                libro.getAutor(),
                                libro.getIsbn(),
                                libro.getNombreCategoria(),
                                libro.getEditorial(),
                                libro.getEstado(),
                                libro.getCantidadDisponible()
                            });
                        }
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(LibroView.this, "Error en la búsqueda");
                }
            }
        };
        worker.execute();
    }
    @SuppressWarnings("unchecked")
    private void loadDisponibles() {
        tableModel.setRowCount(0);
        
        SwingWorker<List<LibroDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<LibroDTO> doInBackground() {
                Map<String, Object> response = libroController.listarLibrosDisponibles();
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
                        for (LibroDTO libro : libros) {
                            tableModel.addRow(new Object[]{
                                libro.getIdLibro(),
                                libro.getTitulo(),
                                libro.getAutor(),
                                libro.getIsbn(),
                                libro.getNombreCategoria(),
                                libro.getEditorial(),
                                libro.getEstado(),
                                libro.getCantidadDisponible()
                            });
                        }
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(LibroView.this, "Error al cargar libros disponibles");
                }
            }
        };
        worker.execute();
    }
    
    private void loadSelectedLibro() {
        int row = table.getSelectedRow();
        if (row != -1) {
            idLibroSeleccionado = (Long) tableModel.getValueAt(row, 0);
            
            SwingWorker<LibroDTO, Void> worker = new SwingWorker<>() {
                @Override
                protected LibroDTO doInBackground() {
                    Map<String, Object> response = libroController.obtenerLibro(idLibroSeleccionado);
                    if ((Boolean) response.get("success")) {
                        return (LibroDTO) response.get("data");
                    }
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        LibroDTO libro = get();
                        if (libro != null) {
                            txtTitulo.setText(libro.getTitulo());
                            txtAutor.setText(libro.getAutor());
                            txtISBN.setText(libro.getIsbn());
                            txtEditorial.setText(libro.getEditorial());
                            txtUbicacion.setText(libro.getUbicacion());
                            txtDescripcion.setText(libro.getDescripcion());
                            cmbEstado.setSelectedItem(libro.getEstado());
                            
                            if (libro.getFechaPublicacion() != null) {
                                datePublicacion.setDate(java.sql.Date.valueOf(libro.getFechaPublicacion()));
                            }
                            
                            // Select category
                            for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
                                String item = cmbCategoria.getItemAt(i);
                                if (item.startsWith(libro.getIdCategoria() + " -")) {
                                    cmbCategoria.setSelectedIndex(i);
                                    break;
                                }
                            }
                            
                            btnEditar.setEnabled(true);
                            btnEliminar.setEnabled(true);
                            btnGuardar.setEnabled(false);
                        }
                    } catch (Exception ex) {
                        ComponentFactory.showError(LibroView.this, "Error al cargar datos del libro");
                    }
                }
            };
            worker.execute();
        }
    }
    
    private void saveLibro() {
        if (!validateForm()) return;
        
        LibroDTO dto = buildLibroDTO();
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return libroController.crearLibro(dto);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(LibroView.this, "Libro creado exitosamente");
                        clearForm();
                        loadData();
                    } else {
                        ComponentFactory.showError(LibroView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(LibroView.this, "Error al guardar libro");
                }
            }
        };
        worker.execute();
    }
    
    private void updateLibro() {
        if (!validateForm() || idLibroSeleccionado == null) return;
        
        LibroDTO dto = buildLibroDTO();
        dto.setIdLibro(idLibroSeleccionado);
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return libroController.actualizarLibro(dto);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(LibroView.this, "Libro actualizado exitosamente");
                        clearForm();
                        loadData();
                    } else {
                        ComponentFactory.showError(LibroView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(LibroView.this, "Error al actualizar libro");
                }
            }
        };
        worker.execute();
    }
    
    private void deleteLibro() {
        if (idLibroSeleccionado == null) return;
        
        if (!ComponentFactory.showConfirm(this, "¿Está seguro de eliminar este libro?")) {
            return;
        }
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return libroController.eliminarLibro(idLibroSeleccionado);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        ComponentFactory.showSuccess(LibroView.this, "Libro eliminado exitosamente");
                        clearForm();
                        loadData();
                    } else {
                        ComponentFactory.showError(LibroView.this, (String) response.get("message"));
                    }
                } catch (Exception ex) {
                    ComponentFactory.showError(LibroView.this, "Error al eliminar libro");
                }
            }
        };
        worker.execute();
    }
    
    private LibroDTO buildLibroDTO() {
        String categoriaStr = (String) cmbCategoria.getSelectedItem();
        Long idCategoria = Long.parseLong(categoriaStr.split(" - ")[0]);
        
        LocalDate fechaPub = null;
        if (datePublicacion.getDate() != null) {
            fechaPub = datePublicacion.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        }
        
        return LibroDTO.builder()
            .titulo(txtTitulo.getText().trim())
            .autor(txtAutor.getText().trim())
            .isbn(txtISBN.getText().trim())
            .idCategoria(idCategoria)
            .editorial(txtEditorial.getText().trim())
            .fechaPublicacion(fechaPub)
            .ubicacion(txtUbicacion.getText().trim())
            .descripcion(txtDescripcion.getText().trim())
            .estado((String) cmbEstado.getSelectedItem())
            .build();
    }
    
    private boolean validateForm() {
        if (txtTitulo.getText().trim().isEmpty()) {
            ComponentFactory.showWarning(this, "El título es obligatorio");
            return false;
        }
        if (txtAutor.getText().trim().isEmpty()) {
            ComponentFactory.showWarning(this, "El autor es obligatorio");
            return false;
        }
        if (cmbCategoria.getSelectedItem() == null) {
            ComponentFactory.showWarning(this, "Debe seleccionar una categoría");
            return false;
        }
        return true;
    }
    
    private void clearForm() {
        idLibroSeleccionado = null;
        txtTitulo.setText("");
        txtAutor.setText("");
        txtISBN.setText("");
        txtEditorial.setText("");
        txtUbicacion.setText("");
        txtDescripcion.setText("");
        datePublicacion.setDate(null);
        cmbEstado.setSelectedIndex(0);
        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }
        table.clearSelection();
        
        btnGuardar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}
