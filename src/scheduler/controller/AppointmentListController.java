/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import scheduler.Database;
import scheduler.dao.AppointmentDao;
import scheduler.dao.CustomerDao;
import scheduler.dao.UserDao;
import scheduler.model.Appointment;
import scheduler.model.Customer;

/**
 * FXML Controller class
 *
 * @author wtill
 */
public class AppointmentListController implements Initializable {

    @FXML
    private TableView<AppointmentTableRow> table;
    @FXML
    private TableColumn<AppointmentTableRow, String> colCustomerName;
    @FXML
    private TableColumn<AppointmentTableRow, String> colTitle;
    @FXML
    private TableColumn<AppointmentTableRow, String> colLocation;
    @FXML
    private TableColumn<AppointmentTableRow, String> colStart;
    @FXML
    private TableColumn<AppointmentTableRow, String> colEnd;
    @FXML
    private Button newAppointmentBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button goBackBtn;

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

        List<Appointment> allAppointments = aDao.getAll();
        ObservableList<AppointmentTableRow> appointmentList
                = FXCollections.observableArrayList();
        for (Appointment apt : allAppointments) {
            if (apt.getUserId().getValue() == Database.getCurrentUserId()) {
                appointmentList.add(new AppointmentTableRow(apt));
            }
        }

        colCustomerName.setCellValueFactory(cellData
                -> cellData.getValue().colCustomerName);
        colTitle.setCellValueFactory(cellData
                -> cellData.getValue().colTitle);
        colLocation.setCellValueFactory(cellData
                -> cellData.getValue().colLocation);
        colStart.setCellValueFactory(cellData
                -> cellData.getValue().colStart);
        colEnd.setCellValueFactory(cellData
                -> cellData.getValue().colEnd);
        table.setItems(appointmentList);

    }

    @FXML
    private void handleNewAppointmentBtn(ActionEvent event) {
    }

    @FXML
    private void handleEditBtn(ActionEvent event) {
    }

    @FXML
    private void handleDeleteBtn(ActionEvent event) {
    }

    @FXML
    private void handleGoBackBtn(ActionEvent event) {
    }

    private class AppointmentTableRow {

        int appointmentId;
        SimpleStringProperty colCustomerName = new SimpleStringProperty();
        SimpleStringProperty colTitle = new SimpleStringProperty();
        SimpleStringProperty colLocation = new SimpleStringProperty();
        SimpleStringProperty colStart;
        SimpleStringProperty colEnd;

        AppointmentTableRow(Appointment appointment) {
            this.appointmentId = appointment.getAppointmentId().getValue();
            Customer customer = cDao.get(appointment
                    .getCustomerId().getValue());
            this.colCustomerName = customer.getCustomerName();
            this.colTitle = appointment.getTitle();
            this.colLocation = appointment.getLocation();
            this.colStart = new SimpleStringProperty(appointment.getStart().toString());
            this.colEnd = new SimpleStringProperty(appointment.getEnd().toString());
        }
    }
}
