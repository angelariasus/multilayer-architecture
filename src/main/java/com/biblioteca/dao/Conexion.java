package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe"; 
    private static final String USER = "C##angel"; 
    private static final String PASSWORD = "angel"; 

    private static Connection conexion;

    private Conexion() {}

    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver"); 
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión exitosa a Oracle");
            } catch (ClassNotFoundException e) {
                System.err.println("Error: No se encontró el driver de Oracle");
                e.printStackTrace();
            }
        }
        return conexion;
    }
}
