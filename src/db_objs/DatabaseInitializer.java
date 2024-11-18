package db_objs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    // Database connection credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Saman@12345";
    private static final String DB_NAME = "bank_app";
    private static final String DDL_FILE = "src/resources/schema.ddl";

    public static void initializeDatabase() {
        Thread initThread = new Thread(() -> {
            try {
                if (!doesDatabaseExist()) {
                    createDatabase();
                    System.out.println("Database created successfully.");
                }
                if(!doesTablesExist()){
                    createTablesFromDDL();
                    System.out.println("Tables created successfully.");
                }

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
        initThread.start();
        try {
            initThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    // Method to check if the database exists
    private static boolean doesDatabaseExist() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            String checkDbQuery = "SHOW DATABASES LIKE '" + DB_NAME + "';";
            ResultSet resultSet = statement.executeQuery(checkDbQuery);
            return resultSet.next();
        }
    }

    private static void createDatabase() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            String createDbQuery = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            statement.executeUpdate(createDbQuery);
        }
    }

    private static void createTablesFromDDL() throws SQLException, IOException {
        String ddlQuery = readDDLFile();
        try (Connection connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            for (String query : ddlQuery.split(";")) {
                if (!query.trim().isEmpty()) {
                    statement.execute(query.trim());
                }
            }
        }
    }

    private static boolean doesTablesExist() throws SQLException{
        try (Connection connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            String checkTableQuery = "SHOW TABLES LIKE 'users';";
            ResultSet resultSet = statement.executeQuery(checkTableQuery);
            return resultSet.next();
        }
    }

    private static String readDDLFile() throws IOException {
        StringBuilder ddlContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(DDL_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ddlContent.append(line).append("\n");
            }
        }
        return ddlContent.toString();
    }


    public static Connection dbConnection() {
        try {
            return DriverManager.getConnection(DB_URL+DB_NAME, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
