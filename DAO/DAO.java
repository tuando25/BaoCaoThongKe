package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {
    public static Connection con;

    public DAO() {
        if (con == null) {
            String dbUrl = "jdbc:mysql://localhost:3306/db_restaurant?autoReconnect=true&useSSL=false&characterEncoding=utf8";
            // Support both old and new Driver class names
            String[] dbClasses = {
                "com.mysql.cj.jdbc.Driver",
                "com.mysql.jdbc.Driver"
            };
            
            boolean loaded = false;
            for (String dbClass : dbClasses) {
                try {
                    Class.forName(dbClass);
                    loaded = true;
                    break;
                } catch (ClassNotFoundException e) {
                    // try next driver
                }
            }
            
            if (!loaded) {
                System.err.println("Warning: MySQL JDBC Driver not found in classpath.");
            }

            try {
                con = DriverManager.getConnection(dbUrl, "root", "");
            } catch (Exception e) {
                System.err.println("Database connection failed. Please check dbUrl, username, and password in dao/DAO.java");
                e.printStackTrace();
            }
        }
    }
}
