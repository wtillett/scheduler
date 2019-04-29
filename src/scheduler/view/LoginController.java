/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.view;

import database.Database;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private TextField passwordField;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button loginBtn;
    @FXML
    private Label loginLabel;
    
    CustomerDAO cDAO;
    DataSource ds;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ds = Database.getDataSource();
        cDAO = new CustomerDAO(ds);
    }

    @FXML
    private void handleCancelBtn(ActionEvent event) {
        Customer c = cDAO.findCustomer(1);
        String s = c.getCustomerName().getValue() + " " + c.getAddress().getValue() + ", " + c.getCity().getValue();
        loginLabel.setText(s);
    }

    @FXML
    private void handleLoginBtn(ActionEvent event) throws SQLException{
        Database.testQuery(ds.getConnection());
    }

}
