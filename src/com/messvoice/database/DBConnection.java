package com.messvoice.database;

import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/messvoice_db";
    private static final String USERNAME = "root";  // change if different
    private static final String PASSWORD = "#Hariom0810";      // your MySQL password
    
    private static Connection connection = null;
    
    // Get connection
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connected successfully");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
    
    // Close connection
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } 
    
    // Test connection (call this in main to verify)
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1");
            rs.close();
            stmt.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Database test failed: " + e.getMessage());
            return false;
        }
    }
}