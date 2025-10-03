package com.biblioteca.view;

import com.biblioteca.controller.UsuarioController;
import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.util.ComponentFactory;
import com.biblioteca.util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblError;
    private UsuarioController usuarioController;
    
    public LoginView() {
        this.usuarioController = new UsuarioController();
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Sistema de Biblioteca - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setResizable(false);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, UIConstants.PRIMARY_COLOR, 0, h, UIConstants.PRIMARY_DARK);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        // Login card
        JPanel loginCard = ComponentFactory.createCard();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setPreferredSize(new Dimension(400, 450));
        
        // Logo/Title section
        JLabel lblTitle = ComponentFactory.createTitleLabel("Sistema de Biblioteca");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setForeground(UIConstants.PRIMARY_COLOR);
        
        JLabel lblSubtitle = new JLabel("Inicie sesión para continuar");
        lblSubtitle.setFont(UIConstants.NORMAL_FONT);
        lblSubtitle.setForeground(UIConstants.TEXT_SECONDARY);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        loginCard.add(Box.createVerticalStrut(20));
        loginCard.add(lblTitle);
        loginCard.add(Box.createVerticalStrut(10));
        loginCard.add(lblSubtitle);
        loginCard.add(Box.createVerticalStrut(40));
        
        // Username field
        JLabel lblUsername = ComponentFactory.createLabel("Usuario");
        lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtUsername = ComponentFactory.createTextField();
        txtUsername.setMaximumSize(new Dimension(350, UIConstants.INPUT_HEIGHT));
        
        loginCard.add(lblUsername);
        loginCard.add(Box.createVerticalStrut(8));
        loginCard.add(txtUsername);
        loginCard.add(Box.createVerticalStrut(20));
        
        // Password field
        JLabel lblPassword = ComponentFactory.createLabel("Contraseña");
        lblPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtPassword = new JPasswordField();
        txtPassword.setFont(UIConstants.NORMAL_FONT);
        txtPassword.setMaximumSize(new Dimension(350, UIConstants.INPUT_HEIGHT));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        loginCard.add(lblPassword);
        loginCard.add(Box.createVerticalStrut(8));
        loginCard.add(txtPassword);
        loginCard.add(Box.createVerticalStrut(10));
        
        // Error label
        lblError = new JLabel(" ");
        lblError.setFont(UIConstants.SMALL_FONT);
        lblError.setForeground(UIConstants.DANGER_COLOR);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(lblError);
        loginCard.add(Box.createVerticalStrut(20));
        
        // Login button
        btnLogin = ComponentFactory.createPrimaryButton("Iniciar Sesión");
        btnLogin.setPreferredSize(new Dimension(350, UIConstants.BUTTON_HEIGHT));
        btnLogin.setMaximumSize(new Dimension(350, UIConstants.BUTTON_HEIGHT));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> handleLogin());
        
        loginCard.add(btnLogin);
        loginCard.add(Box.createVerticalStrut(20));
        
        // Add Enter key listener
        txtPassword.addActionListener(e -> handleLogin());
        
        mainPanel.add(loginCard);
        add(mainPanel);
    }
    
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Por favor complete todos los campos");
            return;
        }
        
        btnLogin.setEnabled(false);
        btnLogin.setText("Iniciando...");
        
        SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<>() {
            @Override
            protected Map<String, Object> doInBackground() {
                return usuarioController.login(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    Map<String, Object> response = get();
                    if ((Boolean) response.get("success")) {
                        UsuarioDTO usuario = (UsuarioDTO) response.get("data");
                        dispose();
                        SwingUtilities.invokeLater(() -> {
                            MainView mainView = new MainView(usuario);
                            mainView.setVisible(true);
                        });
                    } else {
                        lblError.setText((String) response.get("message"));
                        btnLogin.setEnabled(true);
                        btnLogin.setText("Iniciar Sesión");
                    }
                } catch (Exception ex) {
                    lblError.setText("Error al iniciar sesión");
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Iniciar Sesión");
                }
            }
        };
        worker.execute();
    }
}
