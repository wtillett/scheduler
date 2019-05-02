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
import java.util.ArrayList;
import java.util.List;
import scheduler.database.Database;

/**
 *
 * @author Will Tillett
 */
public class CountryDao implements Dao<Country> {
    
    private final Connection conn;
    private static Timestamp now;
    
    private static final String DELETE = 
            "DELETE FROM country WHERE countryId = ?";
    private static final String GET_ALL = 
            "SELECT * FROM country ORDER BY countryId";
    private static final String GET = 
            "SELECT * FROM country WHERE countryId = ?";
    private static final String INSERT = 
            "INSERT INTO country VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE =
            "UPDATE country SET country = ?, lastUpdate = ?, lastUpdateBy = ? "
            + "WHERE countryId = ?";
            
    public CountryDao(Connection conn) {
        this.conn = conn;
        this.now = new Timestamp(System.currentTimeMillis());
    }
    
    @Override
    public Country get(int id) {
        try (PreparedStatement ps = conn.prepareStatement(GET)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return extractCountryFromResultSet(rs);
        } catch (SQLException e) {
            System.out.println("getCountry: " + e.getMessage());
        }
        return null;
    }
    
    @Override
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
    
    @Override
    public int insert(Country t) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setInt(1, t.getCountryId().getValue());
            ps.setString(2, t.getCountry().getValue());
            ps.setTimestamp(3, now);
            ps.setString(4, Database.getCurrentUser());
            ps.setTimestamp(5, now);
            ps.setString(6, Database.getCurrentUser());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("insertCountry: " + e.getMessage());
        }
        return result;
    }
    
    @Override
    public int update(Country t) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, t.getCountry().getValue());
            ps.setTimestamp(2, now);
            ps.setString(3, Database.getCurrentUser());
            ps.setInt(4, t.getCountryId().getValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateCountry: " + e.getMessage());
        }
        return result;
    }
    
    @Override
    public void delete(Country t) {
        try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, t.getCountryId().getValue());
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