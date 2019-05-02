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

/**
 *
 * @author Will Tillett
 */
public class AddressDao {

    private final Connection conn;
    private static Timestamp now;

    public AddressDao(Connection conn) {
        this.conn = conn;
        this.now = new Timestamp(System.currentTimeMillis());
    }

    public Address getAddress(int addressId) {
        final String query = "SELECT * FROM address "
                + "WHERE addressId = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, addressId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractAddressFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("getAddress: " + e.getMessage());
        }
        return null;
    }

    public ObservableList<Address> getAllAddresses() {
        final String query = "SELECT * FROM address";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            ObservableList addresses = FXCollections.observableArrayList();
            while (rs.next()) {
                Address address = extractAddressFromResultSet(rs);
                addresses.add(address);
            }
            return addresses;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Address extractAddressFromResultSet(ResultSet rs) throws SQLException {
        Address address = new Address();

        address.setAddressId(rs.getInt("addressId"));
        address.setAddress(rs.getString("address"));
        address.setAddress2(rs.getString("address2"));
        address.setCityId(rs.getInt("cityId"));
        address.setPostalCode(rs.getString("postalCode"));
        address.setPhone(rs.getString("phone"));

        return address;
    }
}
