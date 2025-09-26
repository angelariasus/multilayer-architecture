package com.biblioteca.view;

import javax.swing.*;
import java.awt.*;

public class ReservaView extends JFrame {
    private JTextField txtUsuario, txtEjemplar;
    private JButton btnReservar, btnListar;
    private JTextArea txtResultados;

    public ReservaView() {
        setTitle("Gestión de Reservas");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("ID Usuario:"));
        txtUsuario = new JTextField(); form.add(txtUsuario);
        form.add(new JLabel("ID Ejemplar:"));
        txtEjemplar = new JTextField(); form.add(txtEjemplar);

        btnReservar = new JButton("Reservar");
        btnListar = new JButton("Listar reservas");

        JPanel botones = new JPanel();
        botones.add(btnReservar);
        botones.add(btnListar);

        txtResultados = new JTextArea();
        txtResultados.setEditable(false);

        add(form, BorderLayout.NORTH);
        add(botones, BorderLayout.CENTER);
        add(new JScrollPane(txtResultados), BorderLayout.SOUTH);
    }

    public JButton getBtnReservar() { return btnReservar; }
    public JButton getBtnListar() { return btnListar; }
    public String getIdUsuario() { return txtUsuario.getText(); }
    public String getIdEjemplar() { return txtEjemplar.getText(); }
    public void mostrarResultados(String texto) { txtResultados.setText(texto); }
}
