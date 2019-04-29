/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.view;

import database.Database;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.sql.DataSource;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private void handleButtonAction(ActionEvent event) throws SQLException {
        DataSource ds = Database.getDataSource();
        ResultSet rs = null;
        Statement statement = null;
        try {
            Connection conn = ds.getConnection();
            statement = conn.createStatement();
            rs = statement.executeQuery("SELECT * FROM city");
            while (rs.next()) {
                System.out.println(rs.getString("city"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) statement.close();
        }
    }

    @FXML
    private void handleCancelBtn(ActionEvent event) {
    }

    @FXML
    private void handleLoginBtn(ActionEvent event) throws SQLException {
        DataSource ds = Database.getDataSource();
        Database.testQuery(ds.getConnection());
    }

}
