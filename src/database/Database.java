/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Will Tillett
 */
public class Database {

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String DBNAME = "U05A0v";
    private static final String URL = "jdbc:mysql://52.206.157.109";
    private static final String USER = "U05A0v";
    private static final String PASS = "53688442933";
    private static Connection connection;

    public static void connect() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Connected to database: " + DBNAME);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found " + e.getMessage());
        }
    }

    public static void disconnect() {
        try {
            connection.close();
            System.out.println("Disconnected from " + DBNAME);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
