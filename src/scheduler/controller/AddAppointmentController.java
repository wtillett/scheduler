/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import scheduler.Database;
import scheduler.dao.AppointmentDao;
import scheduler.dao.CustomerDao;
import scheduler.dao.UserDao;
import scheduler.model.Appointment;
import scheduler.model.Customer;
import scheduler.model.User;

/**
 * FXML Controller class
 *
 * @author Will Tillett
 */
public class AddAppointmentController implements Initializable {
    
    private static Connection conn;
    private AppointmentDao aDao;
    private CustomerDao cDao;
    private UserDao uDao;
    private static Appointment appointment;
    private static Customer customer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = Database.getConnection();
        aDao = new AppointmentDao(conn);
        cDao = new CustomerDao(conn);
        uDao = new UserDao(conn);
        appointment = new Appointment();
        customer = new Customer();
    }
}
