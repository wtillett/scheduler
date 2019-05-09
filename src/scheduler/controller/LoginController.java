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
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scheduler.Scheduler;
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
}
