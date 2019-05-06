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
import scheduler.model.Address;

/**
 *
 * @author Will Tillett
 */
public class AddressDao implements Dao<Address> {

    private final Connection conn;
    private static Timestamp now;

    private static final String DELETE
            = "DELETE FROM address WHERE addressId = ?";
    private static final String GET_ALL
            = "SELECT * FROM address ORDER BY addressId";
    private static final String GET_ID
            = "SELECT addressId FROM address WHERE address = ?";
    private static final String GET
            = "SELECT * FROM address WHERE addressId = ?";
    private static final String INSERT
            = "INSERT INTO address (address, address2, cityId, postalCode, "
            + "phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
            + "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ? FROM DUAL "
            + "WHERE NOT EXISTS "
            + "(SELECT address FROM address WHERE address = ?)";
    private static final String UPDATE
            = "UPDATE address SET address = ?, address2 = ?, cityId = ?, "
            + "postalCode = ?, phone = ?, lastUpdate = ?, lastUpdateBy = ? "
            + "WHERE addressId = ?";

    public AddressDao(Connection conn) {
        this.conn = conn;
        this.now = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public Address get(int id) {
        try (PreparedStatement ps = conn.prepareStatement(GET)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractAddressFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("getAddress: " + e.getMessage());
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
                id = rs.getInt("addressId");
            }
        } catch (SQLException e) {
            System.out.println("getAddressId: " + e.getMessage());
        }
        return id;
    }

    @Override
    public List<Address> getAll() {
        List<Address> allAddresses = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(GET_ALL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                allAddresses.add(extractAddressFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("getAllAddresses: " + e.getMessage());
        }
        return allAddresses;
    }

    @Override
    public int insert(Address address) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setString(1, address.getAddress().getValue());
            ps.setString(2, address.getAddress2().getValue());
            ps.setInt(3, address.getCityId().getValue());
            ps.setString(4, address.getPostalCode().getValue());
            ps.setString(5, address.getPhone().getValue());
            ps.setTimestamp(6, now);
            ps.setString(7, Database.getCurrentUserName());
            ps.setTimestamp(8, now);
            ps.setString(9, Database.getCurrentUserName());
            ps.setString(10, address.getAddress().getValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("insertAddress: " + e.getMessage());
        }
        return result;
    }

    @Override
    public int update(Address address) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, address.getAddress().getValue());
            ps.setString(2, address.getAddress2().getValue());
            ps.setInt(3, address.getCityId().getValue());
            ps.setString(4, address.getPostalCode().getValue());
            ps.setString(5, address.getPhone().getValue());
            ps.setTimestamp(6, now);
            ps.setString(7, Database.getCurrentUserName());
            ps.setInt(8, address.getAddressId().getValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateAddress: " + e.getMessage());
        }
        return result;
    }

    @Override
    public void delete(Address address) {
        try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, address.getAddressId().getValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleteAddress: " + e.getMessage());
        }
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
