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
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.database.Database;

/**
 *
 * @author Will Tillett
 */
public class CustomerDAO {

    private final Connection conn;
    private static Timestamp now;

    public CustomerDAO(Connection conn) {
        this.conn = conn;
        this.now = new Timestamp(System.currentTimeMillis());
    }

    public Customer getCustomer(int customerId) {
        final String query = "SELECT * "
                + "FROM customer "
                + "JOIN address ON customer.addressId = address.addressId "
                + "JOIN city ON address.cityId = city.cityId "
                + "JOIN country ON city.countryId = country.countryId "
                + "WHERE customerId = ?";
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
        final String query = "SELECT * FROM customer "
                + "JOIN address ON customer.addressId = address.addressId "
                + "JOIN city ON address.cityId = city.cityId "
                + "JOIN country ON city.countryId = country.countryId";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            ObservableList customers = FXCollections.observableArrayList();
            while (rs.next()) {
                Customer customer = extractCustomerFromResultSet(rs);
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean insertCustomer(Customer customer) {
        checkCountry(customer);
        checkCity(customer);
        checkAddress(customer);
        String customerName = customer.getCustomerName().getValue();
        int addressId = customer.getAddressId().getValue();
        int result = 0;
        String query = "INSERT INTO customer "
                + "(customerName, addressId, active, "
                + "createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "SELECT * FROM (SELECT ?, ?, ?, ?, ?, ?, ?) AS temp "
                + "WHERE NOT EXISTS (SELECT customerName "
                + "FROM customer WHERE customerName = ?) LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, customerName);
            ps.setInt(2, addressId);
            ps.setInt(3, new Integer(1));
            ps.setTimestamp(4, now);
            ps.setString(5, Database.getCurrentUser());
            ps.setTimestamp(6, now);
            ps.setString(7, Database.getCurrentUser());
            ps.setString(8, customerName);
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("insertCustomer: " + e.getMessage());
        }
        return result == 1;
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

    private void checkCountry(Customer customer) {
        String country = customer.getCountry().getValue();
        String query = "INSERT INTO country (country, "
                + "createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "SELECT * FROM (SELECT ?, ?, ?, ?, ?) AS temp "
                + "WHERE NOT EXISTS (SELECT country "
                + "FROM country WHERE country = ?) LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, country);
            ps.setTimestamp(2, now);
            ps.setString(3, Database.getCurrentUser());
            ps.setTimestamp(4, now);
            ps.setString(5, Database.getCurrentUser());
            ps.setString(6, country);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("checkCountry: " + e.getMessage());
        }
    }

    private void checkCity(Customer customer) {
        String city = customer.getCity().getValue();
        int countryId = customer.getCountryId().getValue();
        String query = "INSERT INTO city (city, countryId) "
                + "SELECT * FROM (SELECT ?, ?) AS temp "
                + "WHERE NOT EXISTS (SELECT city "
                + "FROM city WHERE city = ?) LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, city);
            ps.setInt(2, countryId);
            ps.setString(3, city);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("checkCity: " + e.getMessage());
        }
    }

    private void checkAddress(Customer customer) {
        String address = customer.getAddress().getValue();
        String address2 = customer.getAddress2().getValue();
        String postalCode = customer.getPostalCode().getValue();
        String phone = customer.getPhone().getValue();
        int cityId = customer.getCityId().getValue();
        String query = "INSERT INTO address "
                + "(address, address2, postalCode, phone, cityId) "
                + "SELECT * FROM (SELECT ?, ?, ?, ?, ?) AS temp "
                + "WHERE NOT EXISTS (SELECT address "
                + "FROM address WHERE address = ?) LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, address);
            ps.setString(2, address2);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, cityId);
            ps.setString(6, address);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("checkAddress: " + e.getMessage());
        }
    }

}
