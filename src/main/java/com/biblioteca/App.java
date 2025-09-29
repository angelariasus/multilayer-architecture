package com.biblioteca;

import com.biblioteca.view.LoginView;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Asegura que la UI se ejecute en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setVisible(true);
        });
    }
}