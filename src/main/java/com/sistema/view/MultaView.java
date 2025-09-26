package com.biblioteca.view;

import javax.swing.*;
import java.awt.*;

public class MultaView extends JFrame {
    private JTextField txtUsuario, txtMonto, txtIdMulta;
    private JButton btnRegistrar, btnListar, btnPagar;
    private JTextArea txtResultados;

    public MultaView() {
        setTitle("Gestión de Multas");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("ID Usuario:"));
        txtUsuario = new JTextField(); form.add(txtUsuario);
        form.add(new JLabel("Monto:"));
        txtMonto = new JTextField(); form.add(txtMonto);
        form.add(new JLabel("ID Multa (para pagar):"));
        txtIdMulta = new JTextField(); form.add(txtIdMulta);

        btnRegistrar = new JButton("Registrar multa");
        btnListar = new JButton("Listar multas");
        btnPagar = new JButton("Pagar multa");

        JPanel botones = new JPanel();
        botones.add(btnRegistrar);
        botones.add(btnListar);
        botones.add(btnPagar);

        txtResultados = new JTextArea();
        txtResultados.setEditable(false);

        add(form, BorderLayout.NORTH);
        add(botones, BorderLayout.CENTER);
        add(new JScrollPane(txtResultados), BorderLayout.SOUTH);
    }

    public JButton getBtnRegistrar() { return btnRegistrar; }
    public JButton getBtnListar() { return btnListar; }
    public JButton getBtnPagar() { return btnPagar; }
    public String getIdUsuario() { return txtUsuario.getText(); }
    public double getMonto() { return Double.parseDouble(txtMonto.getText()); }
    public int getIdMulta() { return Integer.parseInt(txtIdMulta.getText()); }
    public void mostrarResultados(String texto) { txtResultados.setText(texto); }
}
