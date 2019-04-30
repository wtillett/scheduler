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
public class CustomerDAO {

    private final Connection conn;

    public CustomerDAO(Connection conn) {
        this.conn = conn;
    }

    public Customer findCustomer(int customerId) {
        final String query = "SELECT * "
                + "FROM customer "
                + "JOIN address ON customer.addressId = address.addressId "
                + "JOIN city ON address.cityId = city.cityId "
                + "JOIN country ON city.countryId = country.countryId "
                + "WHERE customerId = ?";
        final Customer c = new Customer();

        try (PreparedStatement findCustomer = conn.prepareStatement(query)) {            
            findCustomer.setInt(1, customerId);
            ResultSet rs = findCustomer.executeQuery();
            while (rs.next()) {
                c.setCustomerId(customerId);
                c.setCustomerName(rs.getString("customerName"));
                c.setAddressId(rs.getInt("addressId"));
                c.setAddress(rs.getString("address"));
                c.setAddress2(rs.getString("address2"));
                c.setCityId(rs.getInt("cityId"));
                c.setPostalCode(rs.getString("postalCode"));
                c.setPhone(rs.getString("phone"));
                c.setCity(rs.getString("city"));
                c.setCountryId(rs.getInt("countryId"));
                c.setCountry(rs.getString("country"));
            }
        } catch (SQLException e) {
        }
        return c;
    }
}
