/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.view;

import scheduler.database.Database;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.sql.DataSource;
import scheduler.model.Customer;
import scheduler.model.CustomerDAO;

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
    Connection conn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DataSource ds = Database.getDataSource();
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }
        cDAO = new CustomerDAO(conn);
    }

    @FXML
    private void handleCancelBtn(ActionEvent event) {
        Customer c = cDAO.findCustomer(1);
        String s = c.getCustomerName().getValue() + " " + c.getAddress().getValue() + ", " + c.getCity().getValue();
        loginLabel.setText(s);
    }

    @FXML
    private void handleLoginBtn(ActionEvent event) throws SQLException {
        Database.testQuery(conn);
    }

}
