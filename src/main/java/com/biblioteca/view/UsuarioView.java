package com.biblioteca.view;

import javax.swing.*;
import java.awt.*;

public class UsuarioView extends JFrame {
    private JTextField txtNombre, txtTipo;
    private JButton btnRegistrar, btnListar, btnBloquear, btnEliminar;
    private JTextArea txtResultados;

    public UsuarioView() {
        setTitle("Gestión de Usuarios");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        form.add(txtNombre);

        form.add(new JLabel("Tipo (Alumno/Docente/Administrativo):"));
        txtTipo = new JTextField();
        form.add(txtTipo);

        btnRegistrar = new JButton("Registrar");
        btnListar = new JButton("Listar");
        btnBloquear = new JButton("Bloquear");
        btnEliminar = new JButton("Eliminar");

        JPanel botones = new JPanel();
        botones.add(btnRegistrar);
        botones.add(btnListar);
        botones.add(btnBloquear);
        botones.add(btnEliminar);

        txtResultados = new JTextArea();
        txtResultados.setEditable(false);

        add(form, BorderLayout.NORTH);
        add(botones, BorderLayout.CENTER);
        add(new JScrollPane(txtResultados), BorderLayout.SOUTH);
    }

    public JButton getBtnRegistrar() { return btnRegistrar; }
    public JButton getBtnListar() { return btnListar; }
    public JButton getBtnBloquear() { return btnBloquear; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public String getNombre() { return txtNombre.getText(); }
    public String getTipo() { return txtTipo.getText(); }
    public void mostrarResultados(String texto) { txtResultados.setText(texto); }
}
