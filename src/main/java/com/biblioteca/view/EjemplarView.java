package com.biblioteca.view;

import com.biblioteca.controller.EjemplarController;
import com.biblioteca.model.Ejemplar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EjemplarView extends JFrame {
    private JTextField txtTitulo, txtAutor, txtEditorial, txtAnio, txtCategoria;
    private JTable tablaEjemplares;
    private DefaultTableModel modeloTabla;

    private EjemplarController ejemplarCtrl;

    public EjemplarView() {
        ejemplarCtrl = new EjemplarController();

        setTitle("Gestión de Ejemplares");
        setSize(750, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ===== Panel superior: Formulario =====
        JPanel panelForm = new JPanel(new GridLayout(2, 5, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Registrar Ejemplar"));

        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtEditorial = new JTextField();
        txtAnio = new JTextField();
        txtCategoria = new JTextField();

        panelForm.add(new JLabel("Título:"));
        panelForm.add(txtTitulo);
        panelForm.add(new JLabel("Autor:"));
        panelForm.add(txtAutor);
        panelForm.add(new JLabel("Editorial:"));
        panelForm.add(txtEditorial);

        panelForm.add(new JLabel("Año:"));
        panelForm.add(txtAnio);
        panelForm.add(new JLabel("Categoría:"));
        panelForm.add(txtCategoria);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> registrarEjemplar());

        // ===== Panel central: Tabla =====
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Título", "Autor", "Editorial", "Año", "Categoría"}, 0);
        tablaEjemplares = new JTable(modeloTabla);

        JScrollPane scrollTabla = new JScrollPane(tablaEjemplares);

        // ===== Panel inferior: Botones =====
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnListar = new JButton("Cargar Ejemplares");
        JButton btnEliminar = new JButton("Eliminar");

        btnListar.addActionListener(e -> cargarEjemplares());
        btnEliminar.addActionListener(e -> eliminarEjemplar());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnListar);
        panelBotones.add(btnEliminar);

        // ===== Layout principal =====
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(panelForm, BorderLayout.NORTH);
        getContentPane().add(scrollTabla, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);
    }

    private void registrarEjemplar() {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String editorial = txtEditorial.getText().trim();
        String anioStr = txtAnio.getText().trim();
        String categoria = txtCategoria.getText().trim();

        if (titulo.isEmpty() || autor.isEmpty() || editorial.isEmpty() || anioStr.isEmpty() || categoria.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int anio;
        try {
            anio = Integer.parseInt(anioStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El año debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ejemplarCtrl.registrarEjemplar(titulo, autor, editorial, anio, categoria);
        JOptionPane.showMessageDialog(this, "Ejemplar registrado con éxito");
        limpiarFormulario();
        cargarEjemplares();
    }

    private void cargarEjemplares() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Ejemplar> ejemplares = ejemplarCtrl.listarEjemplares();
        for (Ejemplar e : ejemplares) {
            modeloTabla.addRow(new Object[]{e.getIdEjemplar(), e.getTitulo(), e.getAutor(),
                    e.getEditorial(), e.getAnio(), e.getCategoria()});
        }
    }

    private void eliminarEjemplar() {
        int fila = tablaEjemplares.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un ejemplar de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        ejemplarCtrl.eliminarEjemplar(id);
        JOptionPane.showMessageDialog(this, "Ejemplar eliminado");
        cargarEjemplares();
    }

    private void limpiarFormulario() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtEditorial.setText("");
        txtAnio.setText("");
        txtCategoria.setText("");
    }
}
