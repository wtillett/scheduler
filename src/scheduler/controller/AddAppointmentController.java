/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scheduler.Database;
import scheduler.Scheduler;
import scheduler.dao.AppointmentDao;
import scheduler.dao.CustomerDao;
import scheduler.model.Appointment;
import scheduler.model.Customer;

/**
 * FXML Controller class
 *
 * @author Will Tillett
 */
public class AddAppointmentController implements Initializable {

    @FXML
    private ComboBox<String> cboCustomer;
    @FXML
    private TextField titleField;
    @FXML
    private TextField typeField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField contactField;
    @FXML
    private TextField urlField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> cboStartHours;
    @FXML
    private ComboBox<String> cboStartMins;
    @FXML
    private ComboBox<String> cboEndHours;
    @FXML
    private ComboBox<String> cboEndMins;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;

    private static Connection conn;
    private Appointment appointment;
    protected AppointmentDao aDao;
    protected CustomerDao cDao;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = Database.getConnection();
        aDao = new AppointmentDao(conn);
        cDao = new CustomerDao(conn);
        appointment = new Appointment();
        cDao.getAll().forEach((Customer customer) -> {
            cboCustomer.getItems().add(customer.getCustomerName().getValue());
        });
        for (int i = 0; i < 24; i++) {
            cboStartHours.getItems().add(i < 10 ? "0" + i : "" + i);
            cboEndHours.getItems().add(i < 10 ? "0" + i : "" + i);
        }
        for (int i = 0; i < 60; i++) {
            cboStartMins.getItems().add(i < 10 ? "0" + i : "" + i);
            cboEndMins.getItems().add(i < 10 ? "0" + i : "" + i);
        }
    }

    @FXML
    private void handleSaveBtn(ActionEvent event) {
        if (isEveryInputValid() && isDateTimeValid()) {
            getFieldsFromInput();
            getDateAndTimes();
            addAppointment();
            handleSceneChange();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Appointment not valid");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancelBtn(ActionEvent event) {
        handleSceneChange();
    }

    private void addAppointment() {
        aDao.insert(appointment);
    }

    protected void handleSceneChange() {
        String fxml = "/scheduler/view/AppointmentList.fxml";
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            Stage stage = Scheduler.getStage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(AddAppointmentController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    protected boolean isEveryInputValid() {
        // The only optional field is TYPE
        if (cboCustomer.getSelectionModel().getSelectedItem().isEmpty() || titleField.getText().isEmpty() || descriptionField.getText().isEmpty() || locationField.getText().isEmpty() || contactField.getText().isEmpty() || urlField.getText().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    protected void getFieldsFromInput() {
        String customerName = cboCustomer.getSelectionModel().getSelectedItem();
        int id = cDao.getId(customerName);
        appointment.setCustomerId(id);
        appointment.setTitle(titleField.getText());
        appointment.setType(typeField.getText());
        appointment.setDescription(descriptionField.getText());
        appointment.setLocation(locationField.getText());
        appointment.setContact(contactField.getText());
        appointment.setUrl(urlField.getText());
    }

    protected void getDateAndTimes() {
        LocalDate date = datePicker.getValue();
        String sh = cboStartHours.getSelectionModel().getSelectedItem();
        String sm = cboStartMins.getSelectionModel().getSelectedItem();
        String eh = cboEndHours.getSelectionModel().getSelectedItem();
        String em = cboEndMins.getSelectionModel().getSelectedItem();
        LocalTime s = LocalTime.of(Integer.parseInt(sh), Integer.parseInt(sm));
        LocalTime e = LocalTime.of(Integer.parseInt(eh), Integer.parseInt(em));
        LocalDateTime start = LocalDateTime.of(date, s);
        LocalDateTime end = LocalDateTime.of(date, e);
        appointment.setStart(start);
        appointment.setEnd(end);
    }

    protected boolean isDateTimeValid() {
        if (cboStartHours.getSelectionModel().getSelectedItem() == null || cboStartMins.getSelectionModel().getSelectedItem() == null || cboEndHours.getSelectionModel().getSelectedItem() == null || cboEndMins.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            String sh = cboStartHours.getSelectionModel().getSelectedItem();
            String sm = cboStartMins.getSelectionModel().getSelectedItem();
            String eh = cboEndHours.getSelectionModel().getSelectedItem();
            String em = cboEndMins.getSelectionModel().getSelectedItem();
            if (datePicker.getValue() == null || sh.isEmpty() || sm.isEmpty() || eh.isEmpty() || em.isEmpty()) {
                return false;
            } else {
                LocalTime start = LocalTime.of(Integer.parseInt(sh), Integer.parseInt(sm));
                LocalTime end = LocalTime.of(Integer.parseInt(eh), Integer.parseInt(em));
                if (start.isAfter(end) || start.equals(end)) {
                    return false;
                }
            }
        }
        return true;
    }

}
