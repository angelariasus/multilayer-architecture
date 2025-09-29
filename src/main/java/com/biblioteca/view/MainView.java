package com.biblioteca.view;

import com.biblioteca.dao.StatsDAO;
import com.biblioteca.util.UIConstants;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private JPanel contentPanel;
    private JLabel welcomeLabel;
    private JLabel statusLabel;

    public MainView() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        setTitle("Sistema de Biblioteca - Panel Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 800));
        getContentPane().setBackground(UIConstants.BACKGROUND_COLOR);
    }

    private void initializeComponents() {
        welcomeLabel = UIConstants.createTitleLabel("¡Bienvenido al Sistema de Biblioteca!");
        statusLabel = UIConstants.createBodyLabel("Sistema operativo - Todos los servicios funcionando correctamente");
        statusLabel.setForeground(UIConstants.SUCCESS_COLOR);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(280);
        splitPane.setDividerSize(1);
        splitPane.setBorder(null);
        
        // Panel de navegación lateral
        JPanel sidePanel = createSidePanel();
        splitPane.setLeftComponent(sidePanel);
        
        // Panel de contenido principal
        contentPanel = createContentPanel();
        splitPane.setRightComponent(contentPanel);
        
        add(splitPane, BorderLayout.CENTER);
        
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        
        // Logo y título
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(Color.WHITE);
        
        JLabel logoLabel = new JLabel("📚");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        logoPanel.add(logoLabel);
        
        JLabel titleLabel = UIConstants.createSubtitleLabel("Sistema de Biblioteca");
        logoPanel.add(titleLabel);
        
        headerPanel.add(logoPanel, BorderLayout.WEST);
        
        // Panel de usuario
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(Color.WHITE);
        
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        userPanel.add(userIcon);
        
        JLabel userLabel = UIConstants.createBodyLabel("Type user");
        userPanel.add(userLabel);
        
        JButton logoutButton = UIConstants.createSecondaryButton("Cerrar Sesión");
        logoutButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro que desea cerrar sesión?", 
                "Confirmar", 
                JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                dispose();
                new LoginView().setVisible(true);
            }
        });
        userPanel.add(logoutButton);
        
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(229, 231, 235)));
        
        JPanel navHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        navHeader.setBackground(Color.WHITE);
        navHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JLabel navTitle = UIConstants.createHeaderLabel("Navegación");
        navHeader.add(navTitle);
        sidePanel.add(navHeader);
        
        sidePanel.add(UIConstants.createSeparator());
        
        String[][] menuItems = {
            {"👥", "Gestión de Usuarios", "usuarios"},
            {"📖", "Gestión de Ejemplares", "ejemplares"},
            {"📋", "Gestión de Préstamos", "prestamos"},
            {"📅", "Gestión de Reservas", "reservas"},
            {"💰", "Gestión de Multas", "multas"}
        };
        
        for (String[] item : menuItems) {
            JButton menuButton = createMenuButton(item[0], item[1], item[2]);
            sidePanel.add(menuButton);
        }
        
        // Espaciador
        sidePanel.add(Box.createVerticalGlue());
        
        return sidePanel;
    }
    
    private JButton createMenuButton(String icon, String text, String action) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel buttonContent = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonContent.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        buttonContent.add(iconLabel);
        
        JLabel textLabel = UIConstants.createBodyLabel(text);
        textLabel.setForeground(UIConstants.TEXT_PRIMARY);
        buttonContent.add(textLabel);
        
        button.add(buttonContent, BorderLayout.CENTER);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(UIConstants.BACKGROUND_COLOR);
                buttonContent.setBackground(UIConstants.BACKGROUND_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                buttonContent.setBackground(Color.WHITE);
            }
        });
        
        button.addActionListener(e -> openModule(action));
        
        return button;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE, UIConstants.PADDING_LARGE));
        
        JPanel welcomePanel = UIConstants.createCardPanel();
        welcomePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, UIConstants.PADDING_MEDIUM, 0);
        welcomePanel.add(welcomeLabel, gbc);
        
        JLabel descLabel = UIConstants.createBodyLabel("Seleccione una opción del menú lateral para comenzar");
        gbc.gridy = 1;
        welcomePanel.add(descLabel, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(UIConstants.PADDING_LARGE, 0, 0, 0);
        welcomePanel.add(statusLabel, gbc);
        
        panel.add(welcomePanel, BorderLayout.NORTH);
        
        JPanel statsPanel = createStatsPanel();
        panel.add(statsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_MEDIUM));
        statsPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        String[][] stats = {
            {"📚", "Total Ejemplares", String.valueOf(StatsDAO.contarEjemplares()), UIConstants.PRIMARY_COLOR.toString()},
            {"👥", "Usuarios Activos", String.valueOf(StatsDAO.contarUsuariosActivos()), UIConstants.SUCCESS_COLOR.toString()},
            {"📋", "Préstamos Activos", String.valueOf(StatsDAO.contarPrestamosActivos()), UIConstants.WARNING_COLOR.toString()},
            {"💰", "Multas Pendientes", String.valueOf(StatsDAO.contarMultasPendientes()), UIConstants.ERROR_COLOR.toString()}
        };

        for (String[] stat : stats) {
            JPanel statCard = createStatCard(stat[0], stat[1], stat[2]);
            statsPanel.add(statCard);
        }

        return statsPanel;
    }

    
    private JPanel createStatCard(String icon, String title, String value) {
        JPanel card = UIConstants.createCardPanel();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, UIConstants.PADDING_SMALL, 0);
        card.add(iconLabel, gbc);
        
        JLabel valueLabel = UIConstants.createTitleLabel(value);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        card.add(valueLabel, gbc);
        
        JLabel titleLabel = UIConstants.createBodyLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(titleLabel, gbc);
        
        return card;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(229, 231, 235)),
            BorderFactory.createEmptyBorder(UIConstants.PADDING_SMALL, UIConstants.PADDING_MEDIUM, UIConstants.PADDING_SMALL, UIConstants.PADDING_MEDIUM)
        ));
        
        JLabel footerLabel = UIConstants.createBodyLabel("© 2025 Sistema de Biblioteca");
        footerPanel.add(footerLabel, BorderLayout.WEST);
        
        JLabel timeLabel = UIConstants.createBodyLabel("Conectado desde: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        footerPanel.add(timeLabel, BorderLayout.EAST);
        
        return footerPanel;
    }

    private void setupEventListeners() {
        // Los event listeners se configuran en los métodos de creación de componentes
    }
    
    private void openModule(String module) {
        try {
            switch (module) {
                case "usuarios":
                    new UsuarioView().setVisible(true);
                    break;
                case "ejemplares":
                    new EjemplarView().setVisible(true);
                    break;
                case "prestamos":
                    new PrestamoView().setVisible(true);
                    break;
                case "reservas":
                    new ReservaView().setVisible(true);
                    break;
                case "multas":
                    new MultaView().setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Módulo en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al abrir el módulo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
