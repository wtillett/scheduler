/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.controller;

import java.io.IOException;
import scheduler.Database;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scheduler.Scheduler;
import scheduler.dao.AppointmentDao;
import scheduler.dao.CustomerDao;
import scheduler.dao.UserDao;
import scheduler.model.Appointment;

/**
 *
 * @author Will Tillett
 */
public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button loginBtn;
    @FXML
    private Label loginLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;

    AppointmentDao aDao;
    CustomerDao cDao;
    UserDao uDao;
    Connection conn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("res/login", locale);
        cancelBtn.setText(rb.getString("CANCEL_BUTTON"));
        loginBtn.setText(rb.getString("LOGIN_BUTTON"));
        loginLabel.setText(rb.getString("TITLE"));
        usernameLabel.setText(rb.getString("USERNAME"));
        passwordLabel.setText(rb.getString("PASSWORD"));

        conn = Database.getConnection();
        aDao = new AppointmentDao(conn);
        cDao = new CustomerDao(conn);
        uDao = new UserDao(conn);

        // TODO: CHANGE THIS
        usernameField.setText("test");
        passwordField.setText("test");
    }

    @FXML
    private void handleCancelBtn(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void handleLoginBtn(ActionEvent event) {
        String user = usernameField.getText();
        String pass = passwordField.getText();
        login(user, pass);
    }

    private void login(String userName, String password) {
        if (checkCredentials(userName, password)) {
            Database.setCurrentUser(uDao.getByUserName(userName));
            System.out.println("Successfully logged in as: "
                    + Database.getCurrentUserName());
            checkForAppointments();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass()
                        .getResource("/scheduler/view/Main.fxml"));
                Scene scene = new Scene(root);
                Stage stage = Scheduler.getStage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.out.println("Unable to switch scene "
                        + e.getMessage());
            }
        } else {
            System.out.println("Incorrect login information");
        }
    }

    private boolean checkCredentials(String userName, String password) {
        if (userName.isEmpty() || password.isEmpty()) {
            return false;
        }
        final String query = "SELECT password "
                + "FROM user "
                + "WHERE userName = ?";
        String dbPassword = "";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dbPassword = rs.getString("password");
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
            return false;
        }
        return password.equals(dbPassword);
    }

    private void checkForAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        List<Appointment> soon = new ArrayList<>();
        aDao.getAll().stream()
                .filter(appointment
                        -> appointment.getUserId().getValue()
                == Database.getCurrentUserId())
                .forEach(appointments::add);
        LocalDateTime now = LocalDateTime.now();
        appointments.stream().filter((Appointment appointment) -> {
            long minutes = now.until(appointment.getStart(), ChronoUnit.MINUTES);
            return minutes > 0 && minutes < 15;
        }).forEach(soon::add);
        if (soon.size() > 0) {
            soon.stream().forEach((Appointment appointment) -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                String name = cDao.get(appointment.getCustomerId().getValue())
                        .getCustomerName().getValue();
                long minutes = now.until(appointment.getStart(), ChronoUnit.MINUTES);
                alert.setTitle("Upcoming Appointment");
                alert.setContentText("Appointment with " + name + " in "
                        + minutes + " minutes!");
                alert.showAndWait();
            });
        }
    }
}
