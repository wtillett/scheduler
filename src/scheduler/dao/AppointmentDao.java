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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.Database;
import scheduler.model.Appointment;

/**
 *
 * @author Will Tillett
 */
public class AppointmentDao {

    private final Connection conn;
    private static Timestamp now;

    private static final String DELETE
            = "DELETE FROM appointment WHERE appointmentId = ?";
    private static final String GET_ALL
            = "SELECT * FROM appointment ORDER BY appointmentId";
    private static final String GET_ID
            = "SELECT appointmentId FROM appointment WHERE title = ?";
    private static final String GET
            = "SELECT * FROM appointment WHERE appointmentId = ?";
    private static final String INSERT
            = "INSERT INTO appointment "
            + "(customerId, title, description, location, contact, url, "
            + "start, end, createDate, createdBy, lastUpdate, lastUpdateBy, "
            + "type, userId) "
            + "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? FROM DUAL "
            + "WHERE NOT EXISTS "
            + "(SELECT title FROM appointment WHERE title = ?)";
    private static final String UPDATE
            = "UPDATE user SET "
            + "customerId = ?, title = ?, description = ?, location = ?, "
            + "contact = ?, url = ?, start = ?, end = ?, lastUpdate = ?, "
            + "lastUpdateBy = ?, type = ?, userId = ? "
            + "WHERE appointmentId = ?";

    public AppointmentDao(Connection conn) {
        this.conn = conn;
        this.now = new Timestamp(System.currentTimeMillis());
    }

    public Appointment get(int id) {
        try (PreparedStatement ps = conn.prepareStatement(GET)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractAppointmentFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("getAppointment: " + e.getMessage());
        }
        return null;
    }

    public int getId(String title) {
        int id = -1;
        try (PreparedStatement ps = conn.prepareStatement(GET_ID)) {
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("appointmentId");
            }
        } catch (SQLException e) {
            System.out.println("getAppointmentId: " + e.getMessage());
        }
        return id;
    }

    public ObservableList<Appointment> getAll() {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        try (PreparedStatement ps = conn.prepareStatement(GET_ALL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                allAppointments.add(extractAppointmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("getAllAppointments: " + e.getMessage());
        }
        return allAppointments;
    }

    public int insert(Appointment a) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setInt(1, a.getCustomerId().getValue());
            ps.setString(2, a.getTitle().getValue());
            ps.setString(3, a.getDescription().getValue());
            ps.setString(4, a.getLocation().getValue());
            ps.setString(5, a.getContact().getValue());
            ps.setString(6, a.getUrl().getValue());
            ps.setTimestamp(7, Timestamp.valueOf(a.getStart()));
            ps.setTimestamp(8, Timestamp.valueOf(a.getEnd()));
            ps.setTimestamp(9, now);
            ps.setString(10, Database.getCurrentUserName());
            ps.setTimestamp(11, now);
            ps.setString(12, Database.getCurrentUserName());
            ps.setString(13, a.getType().getValue());
            ps.setInt(14, a.getUserId().getValue());
            ps.setString(15, a.getTitle().getValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("insertAppointment: " + e.getMessage());
        }
        return result;
    }

    public int update(Appointment a) {
        int result = 0;
        try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setInt(1, a.getCustomerId().getValue());
            ps.setString(2, a.getTitle().getValue());
            ps.setString(3, a.getDescription().getValue());
            ps.setString(4, a.getLocation().getValue());
            ps.setString(5, a.getContact().getValue());
            ps.setString(6, a.getUrl().getValue());
            ps.setTimestamp(7, Timestamp.valueOf(a.getStart()));
            ps.setTimestamp(8, Timestamp.valueOf(a.getEnd()));
            ps.setTimestamp(9, now);
            ps.setString(10, Database.getCurrentUserName());
            ps.setString(11, a.getType().getValue());
            ps.setInt(12, a.getUserId().getValue());
            ps.setInt(13, a.getAppointmentId().getValue());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateAppointment: " + e.getMessage());
        }
        return result;
    }

    public void delete(Appointment a) {
        try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, a.getAppointmentId().getValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("deleteAppointment: " + e.getMessage());
        }
    }

    private Appointment extractAppointmentFromResultSet(ResultSet rs)
            throws SQLException {
        Appointment appointment = new Appointment();

        appointment.setAppointmentId(rs.getInt("appointmentId"));
        appointment.setCustomerId(rs.getInt("customerId"));
        appointment.setTitle(rs.getString("title"));
        appointment.setDescription(rs.getString("description"));
        appointment.setLocation(rs.getString("location"));
        appointment.setContact(rs.getString("contact"));
        appointment.setUrl(rs.getString("url"));
        appointment.setStart(rs.getTimestamp("start").toLocalDateTime());
        appointment.setEnd(rs.getTimestamp("end").toLocalDateTime());
        appointment.setType(rs.getString("type"));
        appointment.setUserId(rs.getInt("userId"));

        return appointment;
    }

}
