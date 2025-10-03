package com.biblioteca.view;

import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.util.ComponentFactory;
import com.biblioteca.util.UIConstants;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private UsuarioDTO currentUser;
    private JPanel contentPanel;
    private JLabel lblUserInfo;
    
    public MainView(UsuarioDTO user) {
        this.currentUser = user;
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Sistema de Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLayout(new BorderLayout());
        
        // Top bar
        JPanel topBar = createTopBar();
        add(topBar, BorderLayout.NORTH);
        
        // Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        
        // Content area
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        add(contentPanel, BorderLayout.CENTER);
        
        // Show dashboard by default
        showDashboard();
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(0, 70));
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.BORDER_COLOR));
        
        // Left side - Title
        JLabel lblTitle = ComponentFactory.createTitleLabel("  Sistema de Biblioteca");
        lblTitle.setForeground(UIConstants.PRIMARY_COLOR);
        topBar.add(lblTitle, BorderLayout.WEST);
        
        // Right side - User info
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        userPanel.setBackground(Color.WHITE);
        
        lblUserInfo = new JLabel(currentUser.getNombre() + " (" + currentUser.getTipo() + ")");
        lblUserInfo.setFont(UIConstants.NORMAL_FONT);
        lblUserInfo.setForeground(UIConstants.TEXT_PRIMARY);
        
        JButton btnLogout = ComponentFactory.createSecondaryButton("Cerrar Sesión");
        btnLogout.setPreferredSize(new Dimension(140, 35));
        btnLogout.addActionListener(e -> logout());
        
        userPanel.add(lblUserInfo);
        userPanel.add(btnLogout);
        topBar.add(userPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIConstants.PRIMARY_DARK);
        sidebar.setPreferredSize(new Dimension(250, 0));
        
        sidebar.add(Box.createVerticalStrut(20));
        
        // Menu items
        addMenuItem(sidebar, "📊 Dashboard", () -> showDashboard());
        addMenuItem(sidebar, "📚 Libros", () -> showView(new LibroView()));
        addMenuItem(sidebar, "📖 Ejemplares", () -> showView(new EjemplarView()));
        addMenuItem(sidebar, "👥 Usuarios", () -> showView(new UsuarioView()));
        addMenuItem(sidebar, "📋 Préstamos", () -> showView(new PrestamoView()));
        addMenuItem(sidebar, "🔖 Reservas", () -> showView(new ReservaView()));
        addMenuItem(sidebar, "💰 Multas", () -> showView(new MultaView()));
        addMenuItem(sidebar, "🏷️ Categorías", () -> showView(new CategoriaView()));
        addMenuItem(sidebar, "📈 Reportes", () -> showView(new ReporteView()));
        
        sidebar.add(Box.createVerticalGlue());
        
        return sidebar;
    }
    
    private void addMenuItem(JPanel sidebar, String text, Runnable action) {
        JButton menuItem = new JButton(text);
        menuItem.setFont(UIConstants.NORMAL_FONT);
        menuItem.setForeground(Color.WHITE);
        menuItem.setBackground(UIConstants.PRIMARY_DARK);
        menuItem.setHorizontalAlignment(SwingConstants.LEFT);
        menuItem.setBorderPainted(false);
        menuItem.setFocusPainted(false);
        menuItem.setMaximumSize(new Dimension(250, 50));
        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuItem.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        menuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuItem.setBackground(UIConstants.PRIMARY_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuItem.setBackground(UIConstants.PRIMARY_DARK);
            }
        });
        
        menuItem.addActionListener(e -> action.run());
        
        sidebar.add(menuItem);
    }
    
    private void showDashboard() {
        contentPanel.removeAll();
        contentPanel.add(new DashboardView());
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showView(JPanel view) {
        contentPanel.removeAll();
        contentPanel.add(view);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void logout() {
        if (ComponentFactory.showConfirm(this, "¿Está seguro que desea cerrar sesión?")) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            });
        }
    }
}
