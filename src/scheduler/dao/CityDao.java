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
import scheduler.model.City;

/**
 *
 * @author Will Tillett
 */
public class CityDao {

    private final Connection conn;
    private static Timestamp now;

    private static final String DELETE
            = "DELETE FROM city WHERE cityId = ?";
    private static final String GET_ALL
            = "SELECT * FROM city ORDER BY cityId";
    private static final String GET_ID
            = "SELECT cityId FROM city WHERE city = ?";
    private static final String GET
            = "SELECT * FROM city WHERE cityId = ?";
    private static final String INSERT
            = "INSERT INTO city (city, countryId, createDate, createdBy, "
            + "lastUpdate, lastUpdateBy) "
            + "SELECT ?, ?, ?, ?, ?, ? FROM DUAL "
            + "WHERE NOT EXISTS "
            + "(SELECT city FROM city WHERE city = ?)";
    private static final String UPDATE
            = "UPDATE city SET city = ?, "
            + "countryId = ?, lastUpdate = ?, lastUpdateBy = ? "
            + "WHERE cityId = ?";

    public CityDao(Connection conn) {
        this.conn = conn;
        this.now = new Timestamp(System.currentTimeMillis());
    }

    public City get(int id) {
        try (PreparedStatement ps = conn.prepareStatement(GET)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractCityFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("getCity: " + e.getMessage());
        }
        return null;
    }

    public int getId(String name) {
        int id = -1;
        try (PreparedStatement ps = conn.prepareStatement(GET_ID)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("cityId");
            }
        } catch (SQLException e) {
            System.out.println("getCityId: " + e.getMessage());
        }
        return id;
    }

    public ObservableList<City> getAll() {
        ObservableList<City> allCities = FXCollections.observableArrayList();
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

    public int insert(City city) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setString(1, city.getCity().getValue());
            ps.setInt(2, city.getCountryId().getValue());
            ps.setTimestamp(3, now);
            ps.setString(4, Database.getCurrentUserName());
            ps.setTimestamp(5, now);
            ps.setString(6, Database.getCurrentUserName());
            ps.setString(7, city.getCity().getValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("insertCity: " + e.getMessage());
        }
        return result;
    }

    public int update(City city) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, city.getCity().getValue());
            ps.setInt(2, city.getCountryId().getValue());
            ps.setTimestamp(3, now);
            ps.setString(4, Database.getCurrentUserName());
            ps.setInt(5, city.getCityId().getValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateCity: " + e.getMessage());
        }
        return result;
    }

    public void delete(City city) {
        try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, city.getCityId().getValue());
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
