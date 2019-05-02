/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Will Tillett
 */
public class UserDao {
    
    private final Connection conn;
    
    public UserDao(Connection conn) {
        this.conn = conn;
    }
    
    public User getUser(int userId) {
        final String query = "SELECT * "
                + "FROM user "
                + "WHERE userId = ?";
        final User user = new User();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setUserId(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println("User not found");
        }
        return null;
    }
    
}
