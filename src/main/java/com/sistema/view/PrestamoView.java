package com.biblioteca.view;

import javax.swing.*;
import java.awt.*;

public class PrestamoView extends JFrame {
    private JTextField txtUsuario, txtEjemplar;
    private JButton btnPrestar, btnDevolver, btnListar;
    private JTextArea txtResultados;

    public PrestamoView() {
        setTitle("Gestión de Préstamos");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        panel.add(new JLabel("ID Usuario:"));
        txtUsuario = new JTextField();
        panel.add(txtUsuario);

        panel.add(new JLabel("ID Ejemplar:"));
        txtEjemplar = new JTextField();
        panel.add(txtEjemplar);

        btnPrestar = new JButton("Prestar");
        panel.add(btnPrestar);

        btnDevolver = new JButton("Devolver");
        panel.add(btnDevolver);

        btnListar = new JButton("Listar préstamos");
        panel.add(btnListar);

        txtResultados = new JTextArea();
        txtResultados.setEditable(false);
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(txtResultados), BorderLayout.CENTER);
    }

    public JButton getBtnPrestar() { return btnPrestar; }
    public JButton getBtnDevolver() { return btnDevolver; }
    public JButton getBtnListar() { return btnListar; }
    public String getIdUsuario() { return txtUsuario.getText(); }
    public String getIdEjemplar() { return txtEjemplar.getText(); }
    public void mostrarResultados(String texto) { txtResultados.setText(texto); }
}
