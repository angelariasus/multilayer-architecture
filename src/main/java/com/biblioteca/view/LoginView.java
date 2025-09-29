package com.biblioteca.view;

import com.biblioteca.controller.AuthController;
import com.biblioteca.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnSalir;

    private AuthController authCtrl;

    public LoginView() {
        authCtrl = new AuthController();

        setTitle("Login - Sistema de Biblioteca");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ===== Panel principal =====
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Usuario
        panel.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        panel.add(txtUsuario);

        // Contraseña
        panel.add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        // Botones
        btnLogin = new JButton("Ingresar");
        btnSalir = new JButton("Salir");
        panel.add(btnLogin);
        panel.add(btnSalir);

        getContentPane().add(panel, BorderLayout.CENTER);

        // ===== Eventos =====
        btnLogin.addActionListener(e -> loginAction());
        btnSalir.addActionListener(e -> System.exit(0));
    }

    private void loginAction() {
        String username = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuarioLogueado = authCtrl.login(username, password);

        if (usuarioLogueado == null) {
            JOptionPane.showMessageDialog(this, "Credenciales inválidas o usuario inactivo.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Bienvenido " + usuarioLogueado.getNombre(),
                    "Acceso correcto", JOptionPane.INFORMATION_MESSAGE);

            // Abrir menú principal
            new MainView(usuarioLogueado).setVisible(true);
            this.dispose();
        }
    }
}
