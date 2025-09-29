package com.biblioteca.util;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Constantes y utilidades para el diseño de la interfaz de usuario
 */
public class UIConstants {
    
    // Colores principales
    public static final Color PRIMARY_COLOR = new Color(59, 130, 246);      // Azul moderno
    public static final Color PRIMARY_DARK = new Color(37, 99, 235);        // Azul oscuro
    public static final Color SECONDARY_COLOR = new Color(107, 114, 128);   // Gris medio
    public static final Color BACKGROUND_COLOR = new Color(249, 250, 251);  // Gris muy claro
    public static final Color CARD_BACKGROUND = Color.WHITE;                // Blanco para tarjetas
    public static final Color TEXT_PRIMARY = new Color(17, 24, 39);         // Texto principal
    public static final Color TEXT_SECONDARY = new Color(107, 114, 128);    // Texto secundario
    public static final Color SUCCESS_COLOR = new Color(34, 197, 94);       // Verde éxito
    public static final Color ERROR_COLOR = new Color(239, 68, 68);         // Rojo error
    public static final Color WARNING_COLOR = new Color(245, 158, 11);      // Amarillo advertencia
    
    // Fuentes
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    
    // Dimensiones
    public static final int PADDING_SMALL = 8;
    public static final int PADDING_MEDIUM = 16;
    public static final int PADDING_LARGE = 24;
    public static final int BORDER_RADIUS = 8;
    
    // Bordes
    public static final Border CARD_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
        BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM)
    );
    
    public static final Border SECTION_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
        BorderFactory.createEmptyBorder(PADDING_MEDIUM, 0, PADDING_MEDIUM, 0)
    );
    
    /**
     * Crea un botón con estilo moderno
     */
    public static JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(BODY_FONT);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }
    
    /**
     * Crea un botón primario
     */
    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, PRIMARY_COLOR);
    }
    
    /**
     * Crea un botón secundario
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = createStyledButton(text, BACKGROUND_COLOR);
        button.setForeground(TEXT_PRIMARY);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(11, 23, 11, 23)
        ));
        return button;
    }
    
    /**
     * Crea un campo de texto con estilo moderno
     */
    public static JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(BODY_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        return field;
    }
    
    /**
     * Crea una etiqueta de título
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    /**
     * Crea una etiqueta de subtítulo
     */
    public static JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(SUBTITLE_FONT);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    /**
     * Crea una etiqueta de encabezado
     */
    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HEADER_FONT);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    /**
     * Crea una etiqueta de cuerpo
     */
    public static JLabel createBodyLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BODY_FONT);
        label.setForeground(TEXT_SECONDARY);
        return label;
    }
    
    /**
     * Crea un panel con fondo de tarjeta
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(CARD_BORDER);
        return panel;
    }
    
    /**
     * Crea un separador horizontal
     */
    public static JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(229, 231, 235));
        return separator;
    }
}
