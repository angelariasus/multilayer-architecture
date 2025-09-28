package com.biblioteca.view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private JButton btnUsuarios, btnEjemplares, btnPrestamos, btnReservas, btnMultas, btnSalir;

    public MainView() {
        setTitle("Sistema Biblioteca - Menú Principal");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        btnUsuarios = new JButton("Usuarios");
        btnEjemplares = new JButton("Ejemplares");
        btnPrestamos = new JButton("Préstamos");
        btnReservas = new JButton("Reservas");
        btnMultas = new JButton("Multas");
        btnSalir = new JButton("Salir");

        panel.add(btnUsuarios);
        panel.add(btnEjemplares);
        panel.add(btnPrestamos);
        panel.add(btnReservas);
        panel.add(btnMultas);
        panel.add(btnSalir);

        add(panel);
    }

    public JButton getBtnUsuarios() { return btnUsuarios; }
    public JButton getBtnEjemplares() { return btnEjemplares; }
    public JButton getBtnPrestamos() { return btnPrestamos; }
    public JButton getBtnReservas() { return btnReservas; }
    public JButton getBtnMultas() { return btnMultas; }
    public JButton getBtnSalir() { return btnSalir; }
}
