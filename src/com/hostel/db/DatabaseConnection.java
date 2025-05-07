package com.hostel.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/hostel_management?useSSL=false&allowPublicKeyRetrieval=true&autoReconnect=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    
    private static Connection connection;
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    System.out.println("Database connection established successfully!");
                } catch (ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(null,
                        "MySQL JDBC Driver not found. Please add the MySQL Connector JAR to your project.\n" +
                        "Download from: https://dev.mysql.com/downloads/connector/j/",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return null;
                } catch (SQLException e) {
                    String errorMessage = "Failed to connect to database.\n\n" +
                        "Please check:\n" +
                        "1. MySQL server is running\n" +
                        "2. Database 'hostel_management' exists\n" +
                        "3. Username and password are correct\n\n" +
                        "Error details: " + e.getMessage();
                    
                    JOptionPane.showMessageDialog(null,
                        errorMessage,
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return null;
                }
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Add this method to check if connection is valid
    public static boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
}