package singleton;

import java.sql.*;

public class Database {

    // Constructor

    private Database() {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    // Fields

    private static Database instance;
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String DATABASE = "calouself_db";
    private final String HOST = "localhost:3306";
    private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

    // Properties

    private Connection connection;

    // Getters

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }

        return instance;
    }

    // Methods

    public PreparedStatement prepareStatement(String query)
    {
        try
        {
            return connection.prepareStatement(query);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    public static Connection getConnection() {
        return instance.connection;
    }

}
