/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import scheduler.model.User;

/**
 *
 * @author Will Tillett
 */
public abstract class Database {

    private static User currentUser;

    public static DataSource getDataSource() {
        Properties properties = new Properties();
        FileInputStream fis;
        MysqlDataSource ds = null;
        try {
            fis = new FileInputStream("src/res/db.properties");
            properties.load(fis);
            ds = new MysqlDataSource();

            ds.setURL(properties.getProperty("MYSQL_DB_URL"));
            ds.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            ds.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
        } catch (IOException e) {
            e.getMessage();
        }
        return ds;
    }

    public static Connection getConnection() {
        Connection conn = null;
        DataSource ds = getDataSource();
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static String getCurrentUserName() {
        return currentUser.getUserName().getValue();
    }
    
    public static int getCurrentUserId() {
        return currentUser.getUserId().getValue();
    }

}
