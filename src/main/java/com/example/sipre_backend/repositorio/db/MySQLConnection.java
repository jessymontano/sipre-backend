package com.example.sipre_backend.repositorio.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private static final String URL = "jdbc:mysql://5.183.8.21:47623/sipre";
    private static final String USER = "developer";
    private static final String PASSWORD = "T4s#nV8p@Xy1!LcZ";

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }
}