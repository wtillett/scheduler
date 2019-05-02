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
public class CityDao implements Dao<City> {
    
    private final Connection conn;
    private static Timestamp now;
    
    private static final String DELETE = 
            "DELETE FROM city WHERE cityId = ?";
    private static final String GET_ALL = 
            "SELECT * FROM city ORDER BY cityId";
    private static final String GET = 
            "SELECT * FROM city WHERE cityId = ?";
    private static final String INSERT = 
            "INSERT INTO city VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = 
            "UPDATE city SET city = ?, "
            + "countryId = ?, lastUpdate = ?, lastUpdateBy = ? "
            + "WHERE cityId = ?";
        
    public CityDao(Connection conn) {
        this.conn = conn;
        this.now = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public City get(int id) {
        try (PreparedStatement ps = conn.prepareStatement(GET)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return extractCityFromResultSet(rs);
        } catch (SQLException e) {
            System.out.println("getCity: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<City> getAll() {
        List<City> allCities = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(GET_ALL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                allCities.add(extractCityFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("getAllCities: " + e.getMessage());
        }
        return allCities;
    }

    @Override
    public int insert(City t) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setInt(1, t.getCityId().getValue());
            ps.setString(2, t.getCity().getValue());
            ps.setInt(3, t.getCountryId().getValue());
            ps.setTimestamp(4, now);
            ps.setString(5, Database.getCurrentUser());
            ps.setTimestamp(6, now);
            ps.setString(7, Database.getCurrentUser());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("insertCity: " + e.getMessage());
        }
        return result;
    }

    @Override
    public int update(City t) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, t.getCity().getValue());
            ps.setInt(2, t.getCountryId().getValue());
            ps.setTimestamp(3, now);
            ps.setString(4, Database.getCurrentUser());
            ps.setInt(5, t.getCityId().getValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateCity: " + e.getMessage());
        }
        return result;
    }

    @Override
    public void delete(City t) {
        try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, t.getCityId().getValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleteCity: " + e.getMessage());
        }
    }
    
    private City extractCityFromResultSet(ResultSet rs) throws SQLException {
        City city = new City();
        city.setCityId(rs.getInt("cityId"));
        city.setCity(rs.getString("city"));
        city.setCountryId(rs.getInt("countryId"));
        
        return city;
    }
}
