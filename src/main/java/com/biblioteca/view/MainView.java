package com.biblioteca.view;

import com.biblioteca.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private Usuario usuario;

    public MainView(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Sistema de Biblioteca - Menú Principal");
        setSize(450, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ===== Panel principal =====
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Etiqueta de bienvenida
        JLabel lblBienvenida = new JLabel("Bienvenido " + usuario.getNombre() + " (" + usuario.getTipo() + ")");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblBienvenida, BorderLayout.NORTH);

        // Panel central con botones
        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton btnUsuarios   = new JButton("Gestionar Usuarios");
        JButton btnEjemplares = new JButton("Gestionar Ejemplares");
        JButton btnPrestamos  = new JButton("Gestionar Préstamos");
        JButton btnReservas   = new JButton("Gestionar Reservas");
        JButton btnMultas     = new JButton("Gestionar Multas");

        panelBotones.add(btnUsuarios);
        panelBotones.add(btnEjemplares);
        panelBotones.add(btnPrestamos);
        panelBotones.add(btnReservas);
        panelBotones.add(btnMultas);

        panel.add(panelBotones, BorderLayout.CENTER);

        // Botón de salida
        JButton btnSalir = new JButton("Cerrar Sesión");
        panel.add(btnSalir, BorderLayout.SOUTH);

        getContentPane().add(panel);

        // ===== Eventos =====
        btnUsuarios.addActionListener(e -> {
            UsuarioView uv = new UsuarioView();
            uv.setLocationRelativeTo(this);
            uv.setVisible(true);
        });

        btnEjemplares.addActionListener(e -> {
            EjemplarView ev = new EjemplarView();
            ev.setLocationRelativeTo(this);
            ev.setVisible(true);
        });

        btnPrestamos.addActionListener(e -> {
            PrestamoView pv = new PrestamoView();
            pv.setLocationRelativeTo(this);
            pv.setVisible(true);
        });

        btnReservas.addActionListener(e -> {
            ReservaView rv = new ReservaView();
            rv.setLocationRelativeTo(this);
            rv.setVisible(true);
        });

        btnMultas.addActionListener(e -> {
            MultaView mv = new MultaView();
            mv.setLocationRelativeTo(this);
            mv.setVisible(true);
        });

        btnSalir.addActionListener(e -> {
            new LoginView().setVisible(true); // Regresar al login
            this.dispose();
        });
    }
}
