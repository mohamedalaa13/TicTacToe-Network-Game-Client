package clientPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306";
    private static final String DB_USER = "test";
    private static final String DB_PASSWORD = "12345678";
    private static final String PARAMS = "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String DB_Name = "tictactoe";
    private static Connection connection;

    public static void getDBStarted() {
        try {
            Class.forName(DB_DRIVER);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        try {
            connection = DriverManager.getConnection(DB_CONNECTION + PARAMS, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_Name);

            statement.close();
            connection.close();

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public static Connection getDBConnection() {

        try {
            connection = DriverManager.getConnection(DB_CONNECTION + "/" + DB_Name + PARAMS, DB_USER, DB_PASSWORD);
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }
        return connection;
    }
}
