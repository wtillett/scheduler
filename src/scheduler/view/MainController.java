/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author wtill
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
    private Button cancelBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void openAppointments(ActionEvent event) {
    }

    @FXML
    private void openCustomers(ActionEvent event) {
    }

    @FXML
    private void openReports(ActionEvent event) {
    }

    @FXML
    private void openLog(ActionEvent event) {
    }
    
}
