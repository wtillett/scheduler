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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.Database;
import scheduler.model.Customer;

/**
 *
 * @author Will Tillett
 */
public class CustomerDao {

    private final Connection conn;
    private static Timestamp now;

    private static final String DELETE
            = "DELETE FROM customer WHERE customerId = ?";
    private static final String GET_ALL
            = "SELECT * FROM customer ORDER BY customerId";
    private static final String GET_ID
            = "SELECT customerId FROM customer WHERE customerName = ?";
    private static final String GET
            = "SELECT * FROM customer WHERE customerId = ?";
    private static final String INSERT
            = "INSERT INTO customer (customerName, addressId, active, "
            + "createDate, createdBy, lastUpdate, lastUpdateBy) "
            + "SELECT ?, ?, 1, ?, ?, ?, ? FROM DUAL "
            + "WHERE NOT EXISTS "
            + "(SELECT customerName FROM customer WHERE customerName = ?)";
    private static final String UPDATE
            = "UPDATE customer SET customerName = ?, addressId = ?, "
            + "lastUpdate = ?, lastUpdateBy = ? "
            + "WHERE customerId = ?";

    public CustomerDao(Connection conn) {
        this.conn = conn;
        this.now = new Timestamp(System.currentTimeMillis());
    }

    public Customer get(int id) {
        try (PreparedStatement ps = conn.prepareStatement(GET)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractCustomerFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("getCustomer: " + e.getMessage());
        }
        return null;
    }

    public int getId(String name) {
        int id = -1;
        try (PreparedStatement ps = conn.prepareStatement(GET_ID)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("customerId");
            }
        } catch (SQLException e) {
            System.out.println("getCustomerId: " + e.getMessage());
        }
        return id;
    }

    public ObservableList<Customer> getAll() {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        try (PreparedStatement ps = conn.prepareStatement(GET_ALL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                allCustomers.add(extractCustomerFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("getAllCustomers: " + e.getMessage());
        }
        return allCustomers;
    }

    public int insert(Customer c) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setString(1, c.getCustomerName().getValue());
            ps.setInt(2, c.getAddressId().getValue());
            ps.setTimestamp(3, now);
            ps.setString(4, Database.getCurrentUserName());
            ps.setTimestamp(5, now);
            ps.setString(6, Database.getCurrentUserName());
            ps.setString(7, c.getCustomerName().getValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Customer: " + e.getMessage());
        }
        return result;
    }

    public int update(Customer c) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, c.getCustomerName().getValue());
            ps.setInt(2, c.getAddressId().getValue());
            ps.setTimestamp(3, now);
            ps.setString(4, Database.getCurrentUserName());
            ps.setInt(5, c.getCustomerId().getValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateCustomer: " + e.getMessage());
        }
        return result;
    }

    public void delete(Customer c) {
        try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, c.getCustomerId().getValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleteCustomer: " + e.getMessage());
        }
    }

    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer customer = new Customer();

        customer.setCustomerId(rs.getInt("customerId"));
        customer.setCustomerName(rs.getString("customerName"));
        customer.setAddressId(rs.getInt("addressId"));

        return customer;
    }
}
