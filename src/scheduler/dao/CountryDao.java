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
import scheduler.model.Country;

/**
 *
 * @author Will Tillett
 */
public class CountryDao {

    private final Connection conn;
    private static Timestamp now;

    private static final String DELETE
            = "DELETE FROM country WHERE countryId = ?";
    private static final String GET_ALL
            = "SELECT * FROM country ORDER BY countryId";
    private static final String GET_ID
            = "SELECT countryId FROM country WHERE country = ?";
    private static final String GET
            = "SELECT * FROM country WHERE countryId = ?";
    private static final String INSERT
            = "INSERT INTO country (country, createDate, createdBy, "
            + "lastUpdate, lastUpdateBy) "
            + "SELECT ?, ?, ?, ?, ? FROM DUAL "
            + "WHERE NOT EXISTS "
            + "(SELECT country FROM country WHERE country = ?)";
    private static final String UPDATE
            = "UPDATE country SET country = ?, lastUpdate = ?, lastUpdateBy = ? "
            + "WHERE countryId = ?";

    public CountryDao(Connection conn) {
        this.conn = conn;
        this.now = new Timestamp(System.currentTimeMillis());
    }

    public Country get(int id) {
        try (PreparedStatement ps = conn.prepareStatement(GET)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractCountryFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("getCountry: " + e.getMessage());
        }
        return null;
    }

    public int getId(String name) {
        int id = -1;
        try (PreparedStatement ps = conn.prepareStatement(GET_ID)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("countryId");
            }
        } catch (SQLException e) {
            System.out.println("getCountryId: " + e.getMessage());
        }
        return id;
    }

    public List<Country> getAll() {
        List<Country> allCountries = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(GET_ALL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                allCountries.add(extractCountryFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("getAllCountries: " + e.getMessage());
        }
        return allCountries;
    }

    public int insert(Country country) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setString(1, country.getCountry().getValue());
            ps.setTimestamp(2, now);
            ps.setString(3, Database.getCurrentUserName());
            ps.setTimestamp(4, now);
            ps.setString(5, Database.getCurrentUserName());
            ps.setString(6, country.getCountry().getValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("insertCountry: " + e.getMessage());
        }
        return result;
    }

    public int update(Country country) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, country.getCountry().getValue());
            ps.setTimestamp(2, now);
            ps.setString(3, Database.getCurrentUserName());
            ps.setInt(4, country.getCountryId().getValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateCountry: " + e.getMessage());
        }
        return result;
    }

    public void delete(Country country) {
        try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, country.getCountryId().getValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleteCountry: " + e.getMessage());
        }
    }

    private Country extractCountryFromResultSet(ResultSet rs)
            throws SQLException {
        Country country = new Country();

        country.setCountryId(rs.getInt("countryId"));
        country.setCountry(rs.getString("country"));

        return country;
    }
}
