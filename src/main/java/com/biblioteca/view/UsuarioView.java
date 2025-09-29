package com.biblioteca.view;

import com.biblioteca.controller.UsuarioController;
import com.biblioteca.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioView extends JFrame {
    private JTextField txtNombre, txtUsername, txtPassword;
    private JComboBox<String> cmbTipo;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    private UsuarioController usuarioCtrl;

    public UsuarioView() {
        usuarioCtrl = new UsuarioController();

        setTitle("Gestión de Usuarios");
        setSize(700, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ===== Panel superior: Formulario =====
        JPanel panelForm = new JPanel(new GridLayout(2, 4, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Registrar Usuario"));

        txtNombre = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JTextField();
        cmbTipo = new JComboBox<>(new String[]{"Alumno", "Docente", "Administrativo"});

        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(txtNombre);
        panelForm.add(new JLabel("Username:"));
        panelForm.add(txtUsername);

        panelForm.add(new JLabel("Contraseña:"));
        panelForm.add(txtPassword);
        panelForm.add(new JLabel("Tipo:"));
        panelForm.add(cmbTipo);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> registrarUsuario());

        // ===== Panel central: Tabla =====
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Tipo", "Username", "Estado"}, 0);
        tablaUsuarios = new JTable(modeloTabla);

        JScrollPane scrollTabla = new JScrollPane(tablaUsuarios);

        // ===== Panel inferior: Botones de acción =====
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnListar = new JButton("Cargar Usuarios");
        JButton btnBloquear = new JButton("Bloquear");
        JButton btnActivar = new JButton("Activar");
        JButton btnEliminar = new JButton("Eliminar");

        btnListar.addActionListener(e -> cargarUsuarios());
        btnBloquear.addActionListener(e -> cambiarEstado("Bloquear"));
        btnActivar.addActionListener(e -> cambiarEstado("Activar"));
        btnEliminar.addActionListener(e -> eliminarUsuario());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnListar);
        panelBotones.add(btnBloquear);
        panelBotones.add(btnActivar);
        panelBotones.add(btnEliminar);

        // ===== Layout principal =====
        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(panelForm, BorderLayout.NORTH);
        getContentPane().add(scrollTabla, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);
    }

    private void registrarUsuario() {
        String nombre = txtNombre.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();

        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        usuarioCtrl.registrarUsuario(nombre, tipo, username, password);
        JOptionPane.showMessageDialog(this, "Usuario registrado con éxito");
        limpiarFormulario();
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Usuario> usuarios = usuarioCtrl.listarUsuarios();
        for (Usuario u : usuarios) {
            modeloTabla.addRow(new Object[]{u.getIdUsuario(), u.getNombre(), u.getTipo(), u.getUsername(), u.getEstado()});
        }
    }

    private void cambiarEstado(String accion) {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);

        if (accion.equals("Bloquear")) {
            usuarioCtrl.bloquearUsuario(id);
            JOptionPane.showMessageDialog(this, "Usuario bloqueado");
        } else if (accion.equals("Activar")) {
            usuarioCtrl.activarUsuario(id);
            JOptionPane.showMessageDialog(this, "Usuario activado");
        }
        cargarUsuarios();
    }

    private void eliminarUsuario() {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        usuarioCtrl.eliminarUsuario(id);
        JOptionPane.showMessageDialog(this, "Usuario eliminado");
        cargarUsuarios();
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cmbTipo.setSelectedIndex(0);
    }
}
