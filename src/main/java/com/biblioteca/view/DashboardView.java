package com.biblioteca.view;

import com.biblioteca.controller.ReporteController;
import com.biblioteca.dto.ReporteDTO;
import com.biblioteca.util.ComponentFactory;
import com.biblioteca.util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DashboardView extends JPanel {
    private ReporteController reporteController;
    private JPanel statsPanel;
    
    public DashboardView() {
        this.reporteController = new ReporteController();
        initComponents();
        loadDashboardData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND_COLOR);
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIConstants.BACKGROUND_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));
        
        JLabel lblTitle = ComponentFactory.createTitleLabel("Dashboard");
        header.add(lblTitle, BorderLayout.WEST);
        
        JButton btnRefresh = ComponentFactory.createSecondaryButton("🔄 Actualizar");
        btnRefresh.addActionListener(e -> loadDashboardData());
        header.add(btnRefresh, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);
        
        // Stats panel
        statsPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        statsPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        
        JScrollPane scrollPane = new JScrollPane(statsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadDashboardData() {
        statsPanel.removeAll();
        
        // Show loading
        JLabel loading = new JLabel("Cargando estadísticas...", SwingConstants.CENTER);
        loading.setFont(UIConstants.HEADER_FONT);
        statsPanel.add(loading);
        statsPanel.revalidate();
        statsPanel.repaint();
        
        SwingWorker<ReporteDTO, Void> worker = new SwingWorker<>() {
            @Override
            protected ReporteDTO doInBackground() {
                Map<String, Object> response = reporteController.generarReporteGeneral();
                if ((Boolean) response.get("success")) {
                    return (ReporteDTO) response.get("data");
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    ReporteDTO reporte = get();
                    if (reporte != null) {
                        displayStats(reporte);
                    } else {
                        statsPanel.removeAll();
                        JLabel error = new JLabel("Error al cargar estadísticas", SwingConstants.CENTER);
                        statsPanel.add(error);
                        statsPanel.revalidate();
                        statsPanel.repaint();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    
    private void displayStats(ReporteDTO reporte) {
        statsPanel.removeAll();
        
        // Create stat cards
        statsPanel.add(createStatCard("Total Libros", String.valueOf(reporte.getTotalLibros()), 
                                     UIConstants.PRIMARY_COLOR, "📚"));
        statsPanel.add(createStatCard("Total Ejemplares", String.valueOf(reporte.getTotalEjemplares()), 
                                     UIConstants.SECONDARY_COLOR, "📖"));
        statsPanel.add(createStatCard("Total Usuarios", String.valueOf(reporte.getTotalUsuarios()), 
                                     UIConstants.ACCENT_COLOR, "👥"));
        statsPanel.add(createStatCard("Categorías", String.valueOf(reporte.getTotalCategorias()), 
                                     new Color(155, 89, 182), "🏷️"));
        
        statsPanel.add(createStatCard("Préstamos Activos", String.valueOf(reporte.getPrestamosActivos()), 
                                     new Color(52, 152, 219), "📋"));
        statsPanel.add(createStatCard("Préstamos Vencidos", String.valueOf(reporte.getPrestamosVencidos()), 
                                     UIConstants.DANGER_COLOR, "⚠️"));
        statsPanel.add(createStatCard("Reservas Activas", String.valueOf(reporte.getReservasActivas()), 
                                     new Color(26, 188, 156), "🔖"));
        statsPanel.add(createStatCard("Multas Pendientes", String.valueOf(reporte.getMultasPendientes()), 
                                     UIConstants.WARNING_COLOR, "💰"));
        
        statsPanel.revalidate();
        statsPanel.repaint();
    }
    
    private JPanel createStatCard(String title, String value, Color color, String icon) {
        JPanel card = ComponentFactory.createCard();
        card.setLayout(new BorderLayout(10, 10));
        card.setPreferredSize(new Dimension(250, 120));
        
        // Icon
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcon.setOpaque(true);
        lblIcon.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        lblIcon.setPreferredSize(new Dimension(80, 80));
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIConstants.SMALL_FONT);
        lblTitle.setForeground(UIConstants.TEXT_SECONDARY);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValue.setForeground(color);
        
        infoPanel.add(lblTitle);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(lblValue);
        
        card.add(lblIcon, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        
        return card;
    }
}
