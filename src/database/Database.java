/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.sql.DataSource;

/**
 *
 * @author Will Tillett
 */
public class Database {

    public static void testQuery(Connection conn) throws SQLException {
        DataSource ds = getDataSource();
        ResultSet rs = null;
        try (Statement statement = conn.createStatement()) {
            rs = statement.executeQuery("SELECT * FROM city");
            while (rs.next()) {
                System.out.println(rs.getString("city"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
}