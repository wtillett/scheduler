/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.view;

import scheduler.Database;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import scheduler.model.Address;
import scheduler.dao.AddressDao;
import scheduler.model.Appointment;
import scheduler.dao.AppointmentDao;
import scheduler.model.City;
import scheduler.dao.CityDao;
import scheduler.model.Country;
import scheduler.dao.CountryDao;
import scheduler.model.Customer;
import scheduler.dao.CustomerDao;
import scheduler.dao.UserDao;

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

    CustomerDao cDao;
    UserDao uDao;
    Connection conn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = Database.getConnection();
        cDao = new CustomerDao(conn);
        uDao = new UserDao(conn);
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
            Database.setCurrentUser(userName);
            System.out.println("Successfully logged in as: "
                    + Database.getCurrentUser());
            CountryDao countryDao = new CountryDao(conn);
            CityDao cityDao = new CityDao(conn);
            AddressDao aDao = new AddressDao(conn);
            AppointmentDao aptDao = new AppointmentDao(conn);
            for (Customer c : cDao.getAll()) {
                System.out.println(c.getCustomerName().getValue());
            }
            countryDao.insert(new Country("Ireland"));
            cityDao.insert(new City("Dublin", countryDao.getId("Ireland")));
            Address address = new Address("444 Real Ave", "", cityDao.getId("Dublin"),
                    "12345", "8002888739");
            aDao.insert(address);
            cDao.insert(new Customer("Tom Jones", aDao.getId("444 Real Ave")));
            for (Customer c : cDao.getAll()) {
                System.out.println(c.getCustomerName().getValue());
            }
            for (Appointment a : aptDao.getAll()) {
                System.out.println(a.getTitle().getValue());
            }
            Appointment apt = new Appointment(cDao.getId("Tom Jones"), "New Appointment",
                    "It's about lice.", "Home", "Bill Jillers", "www.goatse.cx",
                    LocalDateTime.of(2019, Month.JULY, 14, 0, 0, 0),
                    LocalDateTime.of(2019, Month.JULY, 15, 0, 0, 0),
                    "Informal", uDao.getId(Database.getCurrentUser()));
            aptDao.insert(apt);
            for (Appointment a : aptDao.getAll()) {
                System.out.println(a.getTitle().getValue());
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

}
