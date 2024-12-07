package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    // Static instance for Singleton pattern
    private static Database instance;

    // Database credentials and connection string
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String DATABASE = "calouself_db";
    private static final String HOST = "localhost:3306";
    private static final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

    // Connection property
    private static Connection connection;

    // Private constructor to prevent instantiation
    private Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Public static method to get the singleton instance
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // Method to get the connection
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
                System.out.println("Database connected successfully.");
            } catch (SQLException e) {
                System.out.println("Failed to connect to database.");
                throw e;
            }
        }
        return connection;
    }
}
