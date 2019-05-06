/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import scheduler.Database;
import scheduler.model.User;

/**
 *
 * @author Will Tillett
 */
public class UserDao implements Dao<User> {

    private final Connection conn;
    private static Timestamp now;

    private static final String DELETE
            = "DELETE FROM user WHERE userId = ?";
    private static final String GET_ALL
            = "SELECT * FROM user ORDER BY userId";
    private static final String GET_BY_NAME
            = "SELECT * FROM user WHERE userName = ?";
    private static final String GET_ID
            = "SELECT userId FROM user WHERE userName = ?";
    private static final String GET
            = "SELECT * FROM user WHERE userId = ?";
    private static final String INSERT
            = "INSERT INTO user (userName, password, active, "
            + "createDate, createdBy, lastUpdate, lastUpdateBy) "
            + "SELECT ?, ?, 1, ?, ?, ?, ? FROM DUAL "
            + "WHERE NOT EXISTS "
            + "(SELECT userName FROM user WHERE userName = ?)";
    private static final String UPDATE
            = "UPDATE user SET userName = ?, password = ?, "
            + "lastUpdate = ?, lastUpdateBy = ? "
            + "WHERE userId = ?";

    public UserDao(Connection conn) {
        this.conn = conn;
        this.now = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public User get(int id) {
        try (PreparedStatement ps = conn.prepareStatement(GET)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("getUser: " + e.getMessage());
        }
        return null;
    }
    
    public User getByUserName(String userName) {
        try (PreparedStatement ps = conn.prepareStatement(GET_BY_NAME)) {
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("getByUserName: " + e.getMessage());
        }
        return null;
    }

    @Override
    public int getId(String name) {
        int id = -1;
        try (PreparedStatement ps = conn.prepareStatement(GET_ID)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("userId");
            }
        } catch (SQLException e) {
            System.out.println("getUserId: " + e.getMessage());
        }
        return id;
    }

    @Override
    public List<User> getAll() {
        List<User> allUsers = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(GET_ALL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                allUsers.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("getAllUsers: " + e.getMessage());
        }
        return allUsers;
    }

    @Override
    public int insert(User user) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setString(1, user.getUserName().getValue());
            ps.setString(2, user.getPassword().getValue());
            ps.setTimestamp(3, now);
            ps.setString(4, Database.getCurrentUserName());
            ps.setTimestamp(5, now);
            ps.setString(6, Database.getCurrentUserName());
            ps.setString(7, user.getUserName().getValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("insertUser: " + e.getMessage());
        }
        return result;
    }

    @Override
    public int update(User user) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, user.getUserName().getValue());
            ps.setString(2, user.getPassword().getValue());
            ps.setTimestamp(3, now);
            ps.setString(4, Database.getCurrentUserName());
            ps.setInt(5, user.getUserId().getValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateUser: " + e.getMessage());
        }
        return result;
    }

    @Override
    public void delete(User user) {
        try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, user.getUserId().getValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleteUser: " + e.getMessage());
        }
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();

        user.setUserId(rs.getInt("userId"));
        user.setUserName(rs.getString("userName"));
        user.setPassword(rs.getString("password"));

        return user;
    }

}
