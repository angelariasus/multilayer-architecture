package com.biblioteca.view;

import javax.swing.*;
import java.awt.*;

public class EjemplarView extends JFrame {
    private JTextField txtTitulo, txtAutor, txtEditorial, txtAnio, txtCategoria;
    private JButton btnRegistrar, btnListar, btnEliminar;
    private JTextArea txtResultados;

    public EjemplarView() {
        setTitle("Gestión de Ejemplares");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.add(new JLabel("Título:"));
        txtTitulo = new JTextField(); form.add(txtTitulo);
        form.add(new JLabel("Autor:"));
        txtAutor = new JTextField(); form.add(txtAutor);
        form.add(new JLabel("Editorial:"));
        txtEditorial = new JTextField(); form.add(txtEditorial);
        form.add(new JLabel("Año:"));
        txtAnio = new JTextField(); form.add(txtAnio);
        form.add(new JLabel("Categoría:"));
        txtCategoria = new JTextField(); form.add(txtCategoria);

        btnRegistrar = new JButton("Registrar");
        btnListar = new JButton("Listar");
        btnEliminar = new JButton("Eliminar");

        JPanel botones = new JPanel();
        botones.add(btnRegistrar);
        botones.add(btnListar);
        botones.add(btnEliminar);

        txtResultados = new JTextArea();
        txtResultados.setEditable(false);

        add(form, BorderLayout.NORTH);
        add(botones, BorderLayout.CENTER);
        add(new JScrollPane(txtResultados), BorderLayout.SOUTH);
    }

    public JButton getBtnRegistrar() { return btnRegistrar; }
    public JButton getBtnListar() { return btnListar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public String getTitulo() { return txtTitulo.getText(); }
    public String getAutor() { return txtAutor.getText(); }
    public String getEditorial() { return txtEditorial.getText(); }
    public int getAnio() { return Integer.parseInt(txtAnio.getText()); }
    public String getCategoria() { return txtCategoria.getText(); }
    public void mostrarResultados(String texto) { txtResultados.setText(texto); }
}
