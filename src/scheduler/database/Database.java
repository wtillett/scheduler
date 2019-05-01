/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Will Tillett
 */
public abstract class Database {
    
    public static String currentUser;

    public static DataSource getDataSource() {
        Properties properties = new Properties();
        FileInputStream fis = null;
        MysqlDataSource ds = null;
        try {
            fis = new FileInputStream("src/res/db.properties");
            properties.load(fis);
            ds = new MysqlDataSource();

            ds.setURL(properties.getProperty("MYSQL_DB_URL"));
            ds.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            ds.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public static Connection getConnection() {
        Properties properties = new Properties();
        FileInputStream fis = null;
        MysqlDataSource ds = null;
        Connection conn = null;
        try {
            fis = new FileInputStream("src/res/db.properties");
            properties.load(fis);
            ds = new MysqlDataSource();

            ds.setURL(properties.getProperty("MYSQL_DB_URL"));
            ds.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            ds.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
            conn = ds.getConnection();
        } catch (IOException e) {
            System.out.println("Unable to connect to database");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
    
    public static void setCurrentUser(String userName) {
        currentUser = userName;
    }
    
    public static String getCurrentUser() {
        return currentUser;
    }
}
