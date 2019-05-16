/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import scheduler.Database;
import scheduler.Scheduler;

/**
 * FXML Controller class
 *
 * @author Will Tillett
 */
public class MainController implements Initializable {

    @FXML
    private Button appointmentsBtn;
    @FXML
    private Button customersBtn;
    @FXML
    private Button reportsBtn;
    @FXML
    private Button logBtn;
    @FXML
    private Button logoutBtn;

    private static final int APPOINTMENTS = 0;
    private static final int CUSTOMERS = 1;
    private static final int REPORTS = 2;
    private static final int CANCEL = 3;

    private static final File log = new File("log.txt");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!log.exists()) {
            logBtn.setDisable(true);
        }
    }

    @FXML
    private void handleLogoutBtn(ActionEvent event) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(log, true))) {
            String content = "[" + LocalDateTime.now() + "] - Logout by "
                    + Database.getCurrentUserName();
            pw.println(content);
        } catch (IOException e) {
            Logger.getLogger(MainController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        Database.setCurrentUser(null);
        handleSceneChange(CANCEL);
    }

    @FXML
    private void handleAppointmentsBtn(ActionEvent event) {
        handleSceneChange(APPOINTMENTS);
    }

    @FXML
    private void handleCustomersBtn(ActionEvent event) {
        handleSceneChange(CUSTOMERS);
    }

    @FXML
    private void handleReportsBtn(ActionEvent event) {
    }

    @FXML
    private void handleLogBtn(ActionEvent event) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.open(log);
    }

    private void handleSceneChange(int action) {
        String fxml = "/scheduler/view/";
        switch (action) {
            case APPOINTMENTS:
                fxml += "AppointmentList.fxml";
                break;
            case CUSTOMERS:
                fxml += "CustomerList.fxml";
                break;
            case REPORTS:
                fxml += "Reports.fxml";
                break;
            case CANCEL:
                fxml += "Login.fxml";
                break;
        }
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            Stage stage = Scheduler.getStage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(MainController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

}
