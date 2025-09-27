package com.biblioteca;

import com.biblioteca.controller.*;
import com.biblioteca.model.*;

import java.util.*;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        UsuarioController usuarioCtrl = new UsuarioController();
        EjemplarController ejemplarCtrl = new EjemplarController();
        PrestamoController prestamoCtrl = new PrestamoController();
        ReservaController reservaCtrl = new ReservaController();
        MultaController multaCtrl = new MultaController();

        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n=== SISTEMA DE BIBLIOTECA ===");
            System.out.println("1. Gestionar Usuarios");
            System.out.println("2. Gestionar Ejemplares");
            System.out.println("3. Gestionar Préstamos");
            System.out.println("4. Gestionar Reservas");
            System.out.println("5. Gestionar Multas");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1 -> menuUsuarios(sc, usuarioCtrl);
                case 2 -> menuEjemplares(sc, ejemplarCtrl);
                case 3 -> menuPrestamos(sc, prestamoCtrl);
                case 4 -> menuReservas(sc, reservaCtrl);
                case 5 -> menuMultas(sc, multaCtrl);
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida.");
            }
        }

        sc.close();
    }

    private static void menuUsuarios(Scanner sc, UsuarioController usuarioCtrl) {
        System.out.println("\n--- Gestión de Usuarios ---");
        System.out.println("1. Registrar usuario");
        System.out.println("2. Listar usuarios");
        System.out.println("3. Bloquear usuario");
        System.out.println("4. Activar usuario");
        System.out.println("5. Eliminar usuario");
        System.out.print("Opción: ");
        int op = Integer.parseInt(sc.nextLine());

        switch (op) {
            case 1 -> {
                System.out.print("Nombre: ");
                String nombre = sc.nextLine();
                System.out.print("Tipo (Alumno/Docente/Administrativo): ");
                String tipo = sc.nextLine();
                usuarioCtrl.registrarUsuario(nombre, tipo);
            }
            case 2 -> usuarioCtrl.listarUsuarios().forEach(System.out::println);
            case 3 -> {
                System.out.print("ID de usuario a bloquear: ");
                int id = Integer.parseInt(sc.nextLine());
                usuarioCtrl.bloquearUsuario(id);
            }
            case 4 -> {
                System.out.print("ID de usuario a activar: ");
                int id = Integer.parseInt(sc.nextLine());
                usuarioCtrl.activarUsuario(id);
            }
            case 5 -> {
                System.out.print("ID de usuario a eliminar: ");
                int id = Integer.parseInt(sc.nextLine());
                usuarioCtrl.eliminarUsuario(id);
            }
        }
    }

    private static void menuEjemplares(Scanner sc, EjemplarController ejemplarCtrl) {
        System.out.println("\n--- Gestión de Ejemplares ---");
        System.out.println("1. Registrar ejemplar");
        System.out.println("2. Listar ejemplares");
        System.out.println("3. Eliminar ejemplar");
        System.out.print("Opción: ");
        int op = Integer.parseInt(sc.nextLine());

        switch (op) {
            case 1 -> {
                System.out.print("Título: ");
                String titulo = sc.nextLine();
                System.out.print("Autor: ");
                String autor = sc.nextLine();
                System.out.print("Editorial: ");
                String editorial = sc.nextLine();
                System.out.print("Año: ");
                int anio = Integer.parseInt(sc.nextLine());
                System.out.print("Categoría: ");
                String categoria = sc.nextLine();
                ejemplarCtrl.registrarEjemplar(titulo, autor, editorial, anio, categoria);
            }
            case 2 -> ejemplarCtrl.listarEjemplares().forEach(System.out::println);
            case 3 -> {
                System.out.print("ID del ejemplar a eliminar: ");
                int id = Integer.parseInt(sc.nextLine());
                ejemplarCtrl.eliminarEjemplar(id);
            }
        }
    }

    private static void menuPrestamos(Scanner sc, PrestamoController prestamoCtrl) {
        System.out.println("\n--- Gestión de Préstamos ---");
        System.out.println("1. Realizar préstamo");
        System.out.println("2. Devolver ejemplar");
        System.out.println("3. Listar préstamos");
        System.out.print("Opción: ");
        int op = Integer.parseInt(sc.nextLine());

        switch (op) {
            case 1 -> {
                System.out.print("ID Usuario: ");
                int idUsuario = Integer.parseInt(sc.nextLine());
                System.out.print("ID Ejemplar: ");
                int idEjemplar = Integer.parseInt(sc.nextLine());
                System.out.print("Días de préstamo: ");
                int dias = Integer.parseInt(sc.nextLine());
                Date fechaDevolucion = new Date(System.currentTimeMillis() + (long) dias * 24 * 60 * 60 * 1000);
                prestamoCtrl.prestarEjemplar(idUsuario, idEjemplar, fechaDevolucion);
            }
            case 2 -> {
                System.out.print("ID Préstamo: ");
                int idPrestamo = Integer.parseInt(sc.nextLine());
                System.out.print("ID Ejemplar: ");
                int idEjemplar = Integer.parseInt(sc.nextLine());
                prestamoCtrl.devolverEjemplar(idPrestamo, idEjemplar);
            }
            case 3 -> prestamoCtrl.listarPrestamos().forEach(System.out::println);
        }
    }

    private static void menuReservas(Scanner sc, ReservaController reservaCtrl) {
        System.out.println("\n--- Gestión de Reservas ---");
        System.out.println("1. Registrar reserva");
        System.out.println("2. Listar reservas");
        System.out.print("Opción: ");
        int op = Integer.parseInt(sc.nextLine());

        switch (op) {
            case 1 -> {
                System.out.print("ID Usuario: ");
                int idUsuario = Integer.parseInt(sc.nextLine());
                System.out.print("ID Ejemplar: ");
                int idEjemplar = Integer.parseInt(sc.nextLine());
                reservaCtrl.registrarReserva(idUsuario, idEjemplar);
            }
            case 2 -> reservaCtrl.listarReservas().forEach(System.out::println);
        }
    }

    private static void menuMultas(Scanner sc, MultaController multaCtrl) {
        System.out.println("\n--- Gestión de Multas ---");
        System.out.println("1. Registrar multa");
        System.out.println("2. Listar multas");
        System.out.println("3. Pagar multa");
        System.out.print("Opción: ");
        int op = Integer.parseInt(sc.nextLine());

        switch (op) {
            case 1 -> {
                System.out.print("ID Usuario: ");
                int idUsuario = Integer.parseInt(sc.nextLine());
                System.out.print("Monto: ");
                double monto = Double.parseDouble(sc.nextLine());
                multaCtrl.registrarMulta(idUsuario, monto);
            }
            case 2 -> multaCtrl.listarMultas().forEach(System.out::println);
            case 3 -> {
                System.out.print("ID Multa: ");
                int idMulta = Integer.parseInt(sc.nextLine());
                multaCtrl.pagarMulta(idMulta);
            }
        }
    }
}
