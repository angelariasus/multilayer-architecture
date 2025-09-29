package com.biblioteca.view;

import com.biblioteca.controller.AuthController;
import com.biblioteca.model.Usuario;
import com.biblioteca.util.UIConstants;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private AuthController authController;

    public LoginView() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.authController = new AuthController();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        setTitle("Sistema de Biblioteca - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);
    }

    private void initializeComponents() {
        usernameField = UIConstants.createStyledTextField();
        usernameField.setPreferredSize(new Dimension(300, 45));
        
        passwordField = new JPasswordField();
        passwordField.setFont(UIConstants.BODY_FONT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        passwordField.setPreferredSize(new Dimension(300, 45));
        
        loginButton = UIConstants.createPrimaryButton("Iniciar Sesión");
        loginButton.setPreferredSize(new Dimension(300, 45));
        
        cancelButton = UIConstants.createSecondaryButton("Cancelar");
        cancelButton.setPreferredSize(new Dimension(300, 45));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JPanel loginCard = UIConstants.createCardPanel();
        loginCard.setLayout(new GridBagLayout());
        loginCard.setPreferredSize(new Dimension(380, 450));
        
        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.insets = new Insets(UIConstants.PADDING_MEDIUM, 0, UIConstants.PADDING_MEDIUM, 0);
        
        // Logo/Icono del sistema
        JLabel logoLabel = new JLabel("📚", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        cardGbc.gridx = 0; cardGbc.gridy = 0;
        cardGbc.gridwidth = 2;
        loginCard.add(logoLabel, cardGbc);
        
        // Título
        JLabel titleLabel = UIConstants.createTitleLabel("Sistema de Biblioteca");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardGbc.gridy = 1;
        cardGbc.insets = new Insets(0, 0, UIConstants.PADDING_SMALL, 0);
        loginCard.add(titleLabel, cardGbc);
        
        // Subtítulo
        JLabel subtitleLabel = UIConstants.createBodyLabel("Ingresa tus credenciales para continuar");
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardGbc.gridy = 2;
        cardGbc.insets = new Insets(0, 0, UIConstants.PADDING_LARGE, 0);
        loginCard.add(subtitleLabel, cardGbc);
        
        // Campo usuario
        JLabel userLabel = UIConstants.createHeaderLabel("Usuario");
        cardGbc.gridy = 3;
        cardGbc.gridwidth = 1;
        cardGbc.anchor = GridBagConstraints.WEST;
        cardGbc.insets = new Insets(0, 0, UIConstants.PADDING_SMALL, 0);
        loginCard.add(userLabel, cardGbc);
        
        cardGbc.gridy = 4;
        cardGbc.gridwidth = 2;
        cardGbc.fill = GridBagConstraints.HORIZONTAL;
        cardGbc.insets = new Insets(0, 0, UIConstants.PADDING_MEDIUM, 0);
        loginCard.add(usernameField, cardGbc);
        
        // Campo contraseña
        JLabel passLabel = UIConstants.createHeaderLabel("Contraseña");
        cardGbc.gridy = 5;
        cardGbc.gridwidth = 1;
        cardGbc.fill = GridBagConstraints.NONE;
        cardGbc.anchor = GridBagConstraints.WEST;
        cardGbc.insets = new Insets(0, 0, UIConstants.PADDING_SMALL, 0);
        loginCard.add(passLabel, cardGbc);
        
        cardGbc.gridy = 6;
        cardGbc.gridwidth = 2;
        cardGbc.fill = GridBagConstraints.HORIZONTAL;
        cardGbc.insets = new Insets(0, 0, UIConstants.PADDING_LARGE, 0);
        loginCard.add(passwordField, cardGbc);
        
        // Botones
        cardGbc.gridy = 7;
        cardGbc.insets = new Insets(0, 0, UIConstants.PADDING_SMALL, 0);
        loginCard.add(loginButton, cardGbc);
        
        cardGbc.gridy = 8;
        cardGbc.insets = new Insets(0, 0, 0, 0);
        loginCard.add(cancelButton, cardGbc);
        
        // Agregar la tarjeta al panel principal
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(loginCard, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        JPanel footerPanel = new JPanel(new FlowLayout());
        footerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        JLabel footerLabel = UIConstants.createBodyLabel("© 2025 Sistema de Biblioteca v1.0");
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        getRootPane().setDefaultButton(loginButton);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showErrorMessage("Por favor, complete todos los campos.");
            return;
        }

        try {
            Usuario isAuthenticated = authController.login(username, password);
            if (isAuthenticated != null) {
                showSuccessMessage("¡Bienvenido al sistema!");
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new MainView().setVisible(true);
                });
            } else {
                showErrorMessage("Credenciales incorrectas. Intente nuevamente.");
                passwordField.setText("");
                usernameField.requestFocus();
            }
        } catch (Exception ex) {
            showErrorMessage("Error de conexión: " + ex.getMessage());
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
