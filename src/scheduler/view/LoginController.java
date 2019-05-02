/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.view;

import scheduler.database.Database;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import scheduler.model.Customer;
import scheduler.model.CustomerDAO;
import scheduler.model.UserDAO;

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

    CustomerDAO cDAO;
    UserDAO uDAO;
    Connection conn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = Database.getConnection();
        cDAO = new CustomerDAO(conn);
        uDAO = new UserDAO(conn);
    }

    @FXML
    private void handleCancelBtn(ActionEvent event) {
        Customer c = cDAO.getCustomer(1);
        String s = c.getCustomerName().getValue() + " "
                + c.getAddress().getValue() + ", " + c.getCity().getValue();
        loginLabel.setText(s);
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
            ObservableList<Customer> customers = cDAO.getAllCustomers();
            for (Customer c : customers) {
                System.out.println(c.getCustomerName().getValue());
            }
            Customer cc = customers.get(0);
            cc.setCustomerName("Bill Phillips");
            cc.setCity("Cairo");
            System.out.println("cityId: " + cc.getCityId());
            System.out.println(cDAO.insertCustomer(cc));
            System.out.println("cityId: " + cc.getCityId());

            customers = cDAO.getAllCustomers();
            for (Customer c : customers) {
                System.out.println(c.getCustomerName().getValue());
            }
        } else {
            System.out.println("Incorrect login information");
        }
        // TODO: Finish login method
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
