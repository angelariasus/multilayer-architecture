package com.biblioteca.view;

import com.biblioteca.controller.ReporteController;
import com.biblioteca.dto.ReporteDTO;
import com.biblioteca.util.ComponentFactory;
import com.biblioteca.util.UIConstants;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

public class ReporteView extends JPanel {
    private ReporteController controller;
    private JTextArea txtReporte;
    private JDateChooser dateFechaInicio, dateFechaFin;
    
    public ReporteView() {
        this.controller = new ReporteController();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        JLabel lblTitle = ComponentFactory.createTitleLabel("Reportes del Sistema");
        headerPanel.add(lblTitle, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        
        // Control panel
        JPanel controlPanel = ComponentFactory.createCard();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setPreferredSize(new Dimension(300, 0));
        
        JLabel lblTipoReporte = ComponentFactory.createHeaderLabel("Tipo de Reporte");
        lblTipoReporte.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(lblTipoReporte);
        controlPanel.add(Box.createVerticalStrut(15));
        
        JButton btnReporteGeneral = ComponentFactory.createPrimaryButton("Reporte General");
        btnReporteGeneral.setMaximumSize(new Dimension(250, UIConstants.BUTTON_HEIGHT));
        btnReporteGeneral.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnReporteGeneral.addActionListener(e -> generarReporteGeneral());
        controlPanel.add(btnReporteGeneral);
        controlPanel.add(Box.createVerticalStrut(10));
        
        JButton btnReporteUsuarios = ComponentFactory.createPrimaryButton("Reporte de Usuarios");
        btnReporteUsuarios.setMaximumSize(new Dimension(250, UIConstants.BUTTON_HEIGHT));
        btnReporteUsuarios.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnReporteUsuarios.addActionListener(e -> generarReporteUsuarios());
        controlPanel.add(btnReporteUsuarios);
        controlPanel.add(Box.createVerticalStrut(10));
        
        JButton btnReportePrestamos = ComponentFactory.createPrimaryButton("Reporte de Préstamos");
        btnReportePrestamos.setMaximumSize(new Dimension(250, UIConstants.BUTTON_HEIGHT));
        btnReportePrestamos.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnReportePrestamos.addActionListener(e -> mostrarDialogoReportePrestamos());
        controlPanel.add(btnReportePrestamos);
        controlPanel.add(Box.createVerticalStrut(30));
        
        JSeparator separator = ComponentFactory.createSeparator();
        separator.setMaximumSize(new Dimension(250, 1));
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(separator);
        controlPanel.add(Box.createVerticalStrut(20));
        
        JLabel lblAcciones = ComponentFactory.createHeaderLabel("Acciones");
        lblAcciones.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(lblAcciones);
        controlPanel.add(Box.createVerticalStrut(15));
        
        JButton btnLimpiar = ComponentFactory.createSecondaryButton("Limpiar");
        btnLimpiar.setMaximumSize(new Dimension(250, UIConstants.BUTTON_HEIGHT));
        btnLimpiar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLimpiar.addActionListener(e -> txtReporte.setText(""));
        controlPanel.add(btnLimpiar);
        controlPanel.add(Box.createVerticalStrut(10));
        
        JButton btnExportar = ComponentFactory.createSuccessButton("Exportar");
        btnExportar.setMaximumSize(new Dimension(250, UIConstants.BUTTON_HEIGHT));
        btnExportar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnExportar.addActionListener(e -> ComponentFactory.showWarning(this, "Funcionalidad de exportación en desarrollo"));
        controlPanel.add(btnExportar);
        
        controlPanel.add(Box.createVerticalGlue());
        
        mainPanel.add(controlPanel, BorderLayout.WEST);
        
        // Report display panel
        JPanel reportPanel = ComponentFactory.createCard();
        reportPanel.setLayout(new BorderLayout());
        
        JLabel lblReporte = ComponentFactory.createSubtitleLabel("Resultado del Reporte");
        lblReporte.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        reportPanel.add(lblReporte, BorderLayout.NORTH);
        
        txtReporte = new JTextArea();
        txtReporte.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtReporte.setEditable(false);
        txtReporte.setLineWrap(false);
        txtReporte.setText("Seleccione un tipo de reporte para generar...");
        
        JScrollPane scrollPane = new JScrollPane(txtReporte);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.BORDER_COLOR));
        reportPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(reportPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void generarReporteGeneral() {
        txtReporte.setText("Generando reporte general...");
        
        SwingWorker<ReporteDTO, Void> worker = new SwingWorker<>() {
            @Override
            protected ReporteDTO doInBackground() {
                Map<String, Object> response = controller.generarReporteGeneral();
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
                        StringBuilder sb = new StringBuilder();
                        sb.append("═══════════════════════════════════════════════════════════════\n");
                        sb.append("                    REPORTE GENERAL DEL SISTEMA\n");
                        sb.append("═══════════════════════════════════════════════════════════════\n\n");
                        
                        sb.append("ESTADÍSTICAS GENERALES\n");
                        sb.append("───────────────────────────────────────────────────────────────\n");
                        sb.append(String.format("Total de Libros:           %d\n", reporte.getTotalLibros()));
                        sb.append(String.format("Total de Ejemplares:       %d\n", reporte.getTotalEjemplares()));
                        sb.append(String.format("Total de Usuarios:         %d\n", reporte.getTotalUsuarios()));
                        sb.append(String.format("Total de Categorías:       %d\n\n", reporte.getTotalCategorias()));
                        
                        sb.append("ESTADÍSTICAS DE PRÉSTAMOS\n");
                        sb.append("───────────────────────────────────────────────────────────────\n");
                        sb.append(String.format("Préstamos Activos:         %d\n", reporte.getPrestamosActivos()));
                        sb.append(String.format("Préstamos Vencidos:        %d\n", reporte.getPrestamosVencidos()));
                        sb.append(String.format("Préstamos del Mes:         %d\n", reporte.getPrestamosDelMes()));
                        sb.append(String.format("Préstamos del Año:         %d\n\n", reporte.getPrestamosDelAno()));
                        
                        sb.append("ESTADÍSTICAS DE RESERVAS\n");
                        sb.append("───────────────────────────────────────────────────────────────\n");
                        sb.append(String.format("Reservas Activas:          %d\n", reporte.getReservasActivas()));
                        sb.append(String.format("Reservas Vencidas:         %d\n", reporte.getReservasVencidas()));
                        sb.append(String.format("Reservas Cumplidas:        %d\n\n", reporte.getReservasCumplidas()));
                        
                        sb.append("ESTADÍSTICAS DE MULTAS\n");
                        sb.append("───────────────────────────────────────────────────────────────\n");
                        sb.append(String.format("Multas Pendientes:         %d\n", reporte.getMultasPendientes()));
                        sb.append(String.format("Multas Pagadas:            %d\n", reporte.getMultasPagadas()));
                        sb.append(String.format("Monto Total Multas:        $%.2f\n", reporte.getMontoTotalMultas()));
                        sb.append(String.format("Monto Multas Pendientes:   $%.2f\n\n", reporte.getMontoMultasPendientes()));
                        
                        sb.append("ESTADÍSTICAS DE USUARIOS\n");
                        sb.append("───────────────────────────────────────────────────────────────\n");
                        sb.append(String.format("Usuarios Activos:          %d\n", reporte.getUsuariosActivos()));
                        sb.append(String.format("Usuarios Bloqueados:       %d\n", reporte.getUsuariosBloqueados()));
                        sb.append(String.format("Estudiantes Activos:       %d\n", reporte.getEstudiantesActivos()));
                        sb.append(String.format("Docentes Activos:          %d\n\n", reporte.getDocentesActivos()));
                        
                        sb.append("ESTADÍSTICAS DE LIBROS\n");
                        sb.append("───────────────────────────────────────────────────────────────\n");
                        sb.append(String.format("Libros Disponibles:        %d\n", reporte.getLibrosDisponibles()));
                        sb.append(String.format("Libros Agotados:           %d\n\n", reporte.getLibrosAgotados()));
                        
                        sb.append("═══════════════════════════════════════════════════════════════\n");
                        sb.append(String.format("Fecha de Generación: %s\n", reporte.getFechaGeneracion()));
                        sb.append("═══════════════════════════════════════════════════════════════\n");
                        
                        txtReporte.setText(sb.toString());
                        txtReporte.setCaretPosition(0);
                    } else {
                        txtReporte.setText("Error al generar el reporte");
                    }
                } catch (Exception ex) {
                    txtReporte.setText("Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }
    
    private void generarReporteUsuarios() {
        txtReporte.setText("Generando reporte de usuarios...");
        
        SwingWorker<ReporteDTO, Void> worker = new SwingWorker<>() {
            @Override
            protected ReporteDTO doInBackground() {
                Map<String, Object> response = controller.generarReporteUsuarios();
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
                        StringBuilder sb = new StringBuilder();
                        sb.append("═══════════════════════════════════════════════════════════════\n");
                        sb.append("                    REPORTE DE USUARIOS\n");
                        sb.append("═══════════════════════════════════════════════════════════════\n\n");
                        
                        sb.append("RESUMEN DE USUARIOS\n");
                        sb.append("───────────────────────────────────────────────────────────────\n");
                        sb.append(String.format("Total de Usuarios:         %d\n", reporte.getTotalUsuarios()));
                        sb.append(String.format("Usuarios Activos:          %d\n", reporte.getUsuariosActivos()));
                        sb.append(String.format("Usuarios Bloqueados:       %d\n\n", reporte.getUsuariosBloqueados()));
                        
                        sb.append("DISTRIBUCIÓN POR TIPO\n");
                        sb.append("───────────────────────────────────────────────────────────────\n");
                        sb.append(String.format("Estudiantes:               %d\n", reporte.getEstudiantesActivos()));
                        sb.append(String.format("Docentes:                  %d\n\n", reporte.getDocentesActivos()));
                        
                        sb.append("═══════════════════════════════════════════════════════════════\n");
                        sb.append(String.format("Fecha de Generación: %s\n", reporte.getFechaGeneracion()));
                        sb.append("═══════════════════════════════════════════════════════════════\n");
                        
                        txtReporte.setText(sb.toString());
                        txtReporte.setCaretPosition(0);
                    } else {
                        txtReporte.setText("Error al generar el reporte");
                    }
                } catch (Exception ex) {
                    txtReporte.setText("Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }
    
    private void mostrarDialogoReportePrestamos() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Reporte de Préstamos", true);
        dialog.setLayout(new BorderLayout(20, 20));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        
        gbc.gridy = 0;
        panel.add(ComponentFactory.createLabel("Fecha Inicio:"), gbc);
        gbc.gridy = 1;
        dateFechaInicio = new JDateChooser();
        dateFechaInicio.setFont(UIConstants.NORMAL_FONT);
        dateFechaInicio.setPreferredSize(new Dimension(300, UIConstants.INPUT_HEIGHT));
        panel.add(dateFechaInicio, gbc);
        
        gbc.gridy = 2;
        panel.add(ComponentFactory.createLabel("Fecha Fin:"), gbc);
        gbc.gridy = 3;
        dateFechaFin = new JDateChooser();
        dateFechaFin.setFont(UIConstants.NORMAL_FONT);
        dateFechaFin.setPreferredSize(new Dimension(300, UIConstants.INPUT_HEIGHT));
        panel.add(dateFechaFin, gbc);
        
        dialog.add(panel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnGenerar = ComponentFactory.createPrimaryButton("Generar");
        btnGenerar.addActionListener(e -> {
            if (dateFechaInicio.getDate() != null && dateFechaFin.getDate() != null) {
                LocalDate inicio = dateFechaInicio.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate fin = dateFechaFin.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                dialog.dispose();
                generarReportePrestamos(inicio, fin);
            } else {
                ComponentFactory.showWarning(dialog, "Debe seleccionar ambas fechas");
            }
        });
        
        JButton btnCancelar = ComponentFactory.createSecondaryButton("Cancelar");
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnGenerar);
        buttonPanel.add(btnCancelar);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void generarReportePrestamos(LocalDate fechaInicio, LocalDate fechaFin) {
        txtReporte.setText("Generando reporte de préstamos...");
        
        SwingWorker<ReporteDTO, Void> worker = new SwingWorker<>() {
            @Override
            protected ReporteDTO doInBackground() {
                Map<String, Object> response = controller.generarReportePrestamos(fechaInicio, fechaFin);
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
                        StringBuilder sb = new StringBuilder();
                        sb.append("═══════════════════════════════════════════════════════════════\n");
                        sb.append("                    REPORTE DE PRÉSTAMOS\n");
                        sb.append("═══════════════════════════════════════════════════════════════\n\n");
                        
                        sb.append(String.format("Período: %s a %s\n\n", fechaInicio, fechaFin));
                        
                        sb.append("ESTADÍSTICAS DE PRÉSTAMOS\n");
                        sb.append("───────────────────────────────────────────────────────────────\n");
                        sb.append(String.format("Préstamos Activos:         %d\n", reporte.getPrestamosActivos()));
                        sb.append(String.format("Préstamos Vencidos:        %d\n", reporte.getPrestamosVencidos()));
                        sb.append(String.format("Total del Período:         %d\n\n", reporte.getPrestamosDelMes()));
                        
                        sb.append("═══════════════════════════════════════════════════════════════\n");
                        sb.append(String.format("Fecha de Generación: %s\n", reporte.getFechaGeneracion()));
                        sb.append("═══════════════════════════════════════════════════════════════\n");
                        
                        txtReporte.setText(sb.toString());
                        txtReporte.setCaretPosition(0);
                    } else {
                        txtReporte.setText("Error al generar el reporte");
                    }
                } catch (Exception ex) {
                    txtReporte.setText("Error: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }
}
