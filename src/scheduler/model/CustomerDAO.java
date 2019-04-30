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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Will Tillett
 */
public class CustomerDAO {

    private final Connection conn;
    private User user;

    public CustomerDAO(Connection conn) {
        this.conn = conn;
    }

    public Customer getCustomer(int customerId) {
        final String query = "SELECT * "
                + "FROM customer "
                + "JOIN address ON customer.addressId = address.addressId "
                + "JOIN city ON address.cityId = city.cityId "
                + "JOIN country ON city.countryId = country.countryId "
                + "WHERE customerId = ?";
        final Customer c = new Customer();

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractCustomerFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Customer not found");
        }
        return null;
    }

    public ObservableList<Customer> getAllCustomers() {
        final String query = "SELECT * FROM customer";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            ObservableList customers = FXCollections.observableArrayList();
            while (rs.next()) {
                Customer customer = extractCustomerFromResultSet(rs);
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            System.out.println("No customers in database");
        }
        return null;
    }

    public boolean insertCustomer(Customer customer) {
        return true;
    }

    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer customer = new Customer();

        customer.setCustomerId(rs.getInt("customerId"));
        customer.setCustomerName(rs.getString("customerName"));
        customer.setAddressId(rs.getInt("addressId"));
        customer.setAddress(rs.getString("address"));
        customer.setAddress2(rs.getString("address2"));
        customer.setCityId(rs.getInt("cityId"));
        customer.setPostalCode(rs.getString("postalCode"));
        customer.setPhone(rs.getString("phone"));
        customer.setCity(rs.getString("city"));
        customer.setCountryId(rs.getInt("countryId"));
        customer.setCountry(rs.getString("country"));

        return customer;
    }
}
