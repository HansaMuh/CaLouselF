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
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    // Methods

    public PreparedStatement prepareStatement(String query, Object... values)
    {
        PreparedStatement statement = null;

        try
        {
            statement = connection.prepareStatement(query);

            int index = 1;
            for (Object val : values) {
                statement.setObject(index++, val);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return statement;
    }

}
